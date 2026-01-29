package com.FinBridgeAA.fiu_usecase_service.Loan.Service;

import com.FinBridgeAA.fiu_usecase_service.common.DTO.BankAccountDTO;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.TransactionDTO;
import com.FinBridgeAA.fiu_usecase_service.common.Enums.TransactionType;
import com.FinBridgeAA.fiu_usecase_service.integration.AaDataClient;
import com.FinBridgeAA.fiu_usecase_service.Loan.DTO.LoanEvaluationResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanEvaluationService {

    private final AaDataClient aaDataClient;

    public LoanEvaluationService(AaDataClient aaDataClient) {
        this.aaDataClient = aaDataClient;
    }

    public LoanEvaluationResult evaluate(String userId) {

        java.util.Map<String, String> dataRequest = new java.util.HashMap<>();
        dataRequest.put("userId", userId);
        List<BankAccountDTO> accounts = aaDataClient.fetchFinancialData(dataRequest);

        int monthlyIncome = 0;
        int monthlyExpense = 0;

        for (BankAccountDTO account : accounts) {
            for (TransactionDTO txn : account.getTransactions()) {

                if (txn.getType() == TransactionType.CREDIT) {
                    monthlyIncome += txn.getAmount();
                } else {
                    monthlyExpense += txn.getAmount();
                }
            }
        }

        int surplus = monthlyIncome - monthlyExpense;

        // Risk logic (simple but realistic)
        String risk;
        double interestRate;
        int maxLoan;

        if (surplus > 50000) {
            risk = "LOW";
            interestRate = 10.5;
            maxLoan = surplus * 20;
        } else if (surplus > 30000) {
            risk = "MEDIUM";
            interestRate = 13.5;
            maxLoan = surplus * 15;
        } else {
            risk = "HIGH";
            interestRate = 18.0;
            maxLoan = surplus * 8;
        }

        return new LoanEvaluationResult(
                risk,
                maxLoan,
                interestRate,
                "Based on transaction cash-flow analysis");
    }
}