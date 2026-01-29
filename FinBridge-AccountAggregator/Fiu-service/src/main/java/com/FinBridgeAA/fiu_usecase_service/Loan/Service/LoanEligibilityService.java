package com.FinBridgeAA.fiu_usecase_service.Loan.Service;

import com.FinBridgeAA.fiu_usecase_service.Loan.DTO.LoanRequest;
import com.FinBridgeAA.fiu_usecase_service.Loan.DTO.LoanResponse;
import com.FinBridgeAA.fiu_usecase_service.Loan.DTO.FinancialSummary;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.BankAccountDTO;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.TransactionDTO;
import com.FinBridgeAA.fiu_usecase_service.common.Enums.TransactionType;
import com.FinBridgeAA.fiu_usecase_service.integration.AaDataClient;
import com.FinBridgeAA.fiu_usecase_service.integration.CmsClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.FinBridgeAA.fiu_usecase_service.analytics.credit.DTO.CreditScoreResponse;
import com.FinBridgeAA.fiu_usecase_service.analytics.credit.Service.CreditScoreService;
import com.FinBridgeAA.fiu_usecase_service.budget.Service.BudgetAnalysisService;

@Service
public class LoanEligibilityService {

    private final AaDataClient aaDataClient;
    private final CmsClient cmsClient;
    private final DataProcessor dataProcessor;
    private final CreditScoreService creditScoreService;
    private final BudgetAnalysisService budgetAnalysisService;
    private final com.FinBridgeAA.fiu_usecase_service.Loan.Repository.LoanApplicationRepository loanRepository;

    public LoanEligibilityService(AaDataClient aaDataClient, CmsClient cmsClient, DataProcessor dataProcessor,
            CreditScoreService creditScoreService, BudgetAnalysisService budgetAnalysisService,
            com.FinBridgeAA.fiu_usecase_service.Loan.Repository.LoanApplicationRepository loanRepository) {

        this.aaDataClient = aaDataClient;
        this.cmsClient = cmsClient;
        this.dataProcessor = dataProcessor;
        this.creditScoreService = creditScoreService;
        this.budgetAnalysisService = budgetAnalysisService;
        this.loanRepository = loanRepository;
    }

    public List<com.FinBridgeAA.fiu_usecase_service.Loan.Entity.LoanApplication> getHistory(String userId) {
        return loanRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public LoanResponse isEligible(LoanRequest request) {
        System.out.println("iseligible Hit");
        // 1. Check Consent
        UUID userUuid = UUID.fromString(request.getUserId());
        System.out.println("DEBUG: Checking Eligibility for User UUID: " + userUuid);

        String status = "NOT_FOUND";
        if (!request.isForceNewConsent()) {
            Map<String, Object> consentStatus = cmsClient.getActiveConsent(userUuid);
            status = (String) consentStatus.get("status");
            System.out.println("DEBUG: Consent Status from CMS: " + status);
        } else {
            System.out.println("DEBUG: Forcing New Consent as requested.");
        }

        if ("NOT_FOUND".equals(status) || !"ACTIVE".equals(status)) {
            // Create Consent
            Map<String, String> consentRequest = new HashMap<>();
            consentRequest.put("userId", request.getUserId());

            Map<String, Object> newConsent = cmsClient.createConsent(consentRequest);
            String redirectUrl = "http://localhost:8083/consents/" + newConsent.get("consentId") + "/approve";

            saveApplication(request, "CONSENT_REQUIRED", "Please approve consent.", 0);

            return new LoanResponse(
                    false,
                    "CONSENT_REQUIRED",
                    "Please approve consent at: " + redirectUrl);
        }

        // 2. Fetch Data (Consent Exists)
        Map<String, String> dataRequest = new HashMap<>();
        dataRequest.put("userId", request.getUserId());
        List<BankAccountDTO> accounts = aaDataClient.fetchFinancialData(dataRequest);

        // 3. Process Data
        FinancialSummary summary = dataProcessor.process(accounts);
        CreditScoreResponse creditScore = creditScoreService.calculateCreditScore(accounts);

        // 4. Apply Rules
        if ("HIGH_RISK".equals(creditScore.getRiskLevel())) {
            String remark = "Credit Score too low (" + creditScore.getCreditScore() + ") - HIGH RISK";
            saveApplication(request, "REJECTED", remark, 0);
            return new LoanResponse(false, "REJECTED", remark);
        }

        if (summary.getMonthlyIncome() < 30000) {
            String remark = "Insufficient monthly income. Income: " + summary.getMonthlyIncome();
            saveApplication(request, "REJECTED", remark, 0);
            return new LoanResponse(false, "REJECTED", remark);
        }

        if (summary.getMonthlyExpense() > (summary.getMonthlyIncome() * 0.5)) {
            String remark = "High Expense Ratio (>50%).";
            saveApplication(request, "REJECTED", remark, 0);
            return new LoanResponse(false, "REJECTED", remark);
        }

        if (summary.getStabilityScore() < 0.6) {
            String remark = "Unstable cash flow.";
            saveApplication(request, "REJECTED", remark, 0);
            return new LoanResponse(false, "REJECTED", remark);
        }

        int maxLoanAmount = (int) (summary.getMonthlyIncome() * 10);
        int requestedAmount = request.getRequestedAmount();

        if (requestedAmount > maxLoanAmount) {
            String remark = "Requested amount (" + requestedAmount + ") exceeds Maximum Eligible Limit ("
                    + maxLoanAmount + ")";
            saveApplication(request, "REJECTED", remark, 0);
            return new LoanResponse(false, "REJECTED", remark);
        }

        // Approved - but only for the requested amount (as per user request)
        String remark = "Eligible. Approved Amount: " + requestedAmount + " (Max Limit: " + maxLoanAmount + ")";
        saveApplication(request, "APPROVED", remark, requestedAmount);

        return new LoanResponse(
                true,
                "APPROVED",
                remark);
    }

    private void saveApplication(LoanRequest request, String status, String remarks, int approvedAmount) {
        try {
            com.FinBridgeAA.fiu_usecase_service.Loan.Entity.LoanApplication app = new com.FinBridgeAA.fiu_usecase_service.Loan.Entity.LoanApplication();
            app.setUserId(request.getUserId());
            app.setRequestedAmount(request.getRequestedAmount());
            app.setTenureMonths(request.getTenureMonths());
            app.setStatus(status);
            app.setRemarks(remarks);
            app.setApprovedAmount(approvedAmount);
            app.setCreatedAt(java.time.LocalDateTime.now());
            loanRepository.save(app);
            System.out.println("DEBUG: Saved Loan Application History: " + status);
        } catch (Exception e) {
            System.err.println("ERROR: Failed to save loan history: " + e.getMessage());
            e.printStackTrace();
        }
    }
}