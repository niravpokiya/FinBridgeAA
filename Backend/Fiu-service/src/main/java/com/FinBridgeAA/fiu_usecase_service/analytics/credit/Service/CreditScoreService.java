package com.FinBridgeAA.fiu_usecase_service.analytics.credit.Service;

import com.FinBridgeAA.fiu_usecase_service.analytics.credit.DTO.CreditScoreResponse;
import com.FinBridgeAA.fiu_usecase_service.analytics.credit.Rules.CreditScoreRules;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.BankAccountDTO;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.TransactionDTO;
import com.FinBridgeAA.fiu_usecase_service.common.Enums.TransactionType;
import com.FinBridgeAA.fiu_usecase_service.integration.AaDataClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditScoreService {

    private final AaDataClient aaDataClient;
    private final CreditScoreRules rules;

    public CreditScoreResponse calculateCreditScore(String userId) {
        java.util.Map<String, String> dataRequest = new java.util.HashMap<>();
        dataRequest.put("userId", userId);
        List<BankAccountDTO> accounts = aaDataClient.fetchFinancialData(dataRequest);
        return calculateCreditScore(accounts);
    }

    public CreditScoreResponse calculateCreditScore(List<BankAccountDTO> accounts) {
        double income = 0;
        double emi = 0;
        int missedEmiCount = 0;

        for (BankAccountDTO account : accounts) {
            for (TransactionDTO txn : account.getTransactions()) {

                if (txn.getType() == TransactionType.CREDIT
                        && txn.getNarration().toLowerCase().contains("salary")) {
                    income += txn.getAmount();
                }

                if (txn.getType() == TransactionType.DEBIT
                        && txn.getNarration().toLowerCase().contains("emi")) {
                    emi += txn.getAmount();
                }

                if (txn.getNarration().toLowerCase().contains("penalty")
                        || txn.getNarration().toLowerCase().contains("bounce")) {
                    missedEmiCount++;
                }
            }
        }

        int score = rules.calculateScore(income, emi, missedEmiCount);
        String risk = rules.determineRisk(score);

        return new CreditScoreResponse(
                score,
                risk,
                "Score calculated from bank transaction behaviour");
    }
}