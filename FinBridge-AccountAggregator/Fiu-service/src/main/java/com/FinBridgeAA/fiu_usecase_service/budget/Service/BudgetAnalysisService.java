package com.FinBridgeAA.fiu_usecase_service.budget.Service;

import com.FinBridgeAA.fiu_usecase_service.budget.DTO.BudgetRequest;
import com.FinBridgeAA.fiu_usecase_service.budget.DTO.BudgetResponse;
import com.FinBridgeAA.fiu_usecase_service.budget.Rules.BudgetRules;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.BankAccountDTO;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.TransactionDTO;
import com.FinBridgeAA.fiu_usecase_service.common.Enums.TransactionType;
import com.FinBridgeAA.fiu_usecase_service.integration.AaDataClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BudgetAnalysisService {

    private final AaDataClient aaDataClient;
    private final BudgetRules budgetRules;

    public BudgetResponse analyze(BudgetRequest request) {
        Map<String, String> dataRequest = new HashMap<>();
        dataRequest.put("userId", request.getUserId());
        List<BankAccountDTO> accounts = aaDataClient.fetchFinancialData(dataRequest);

        return analyze(accounts, LocalDate.parse(request.getFromDate()), LocalDate.parse(request.getToDate()));
    }

    public BudgetResponse analyze(List<BankAccountDTO> accounts, LocalDate from, LocalDate to) {
        Map<String, Double> categoryMap = new HashMap<>();
        double totalExpense = 0;

        for (BankAccountDTO account : accounts) {
            for (TransactionDTO txn : account.getTransactions()) {

                LocalDate txnDate = LocalDate.parse(txn.getDate());

                if (txn.getType() == TransactionType.DEBIT &&
                        !txnDate.isBefore(from) &&
                        !txnDate.isAfter(to)) {

                    String category = budgetRules.categorize(txn);

                    categoryMap.put(
                            category,
                            categoryMap.getOrDefault(category, 0.0) + txn.getAmount());

                    totalExpense += txn.getAmount();
                }
            }
        }

        String topCategory = categoryMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");

        return new BudgetResponse(totalExpense, categoryMap, topCategory);
    }
}