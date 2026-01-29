package com.FinBridgeAA.fiu_usecase_service.Loan.Service;

import com.FinBridgeAA.fiu_usecase_service.Loan.DTO.FinancialSummary;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.BankAccountDTO;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.TransactionDTO;
import com.FinBridgeAA.fiu_usecase_service.common.Enums.TransactionType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataProcessor {

    public FinancialSummary process(List<BankAccountDTO> accounts) {
        double totalIncome = 0;
        double totalExpense = 0;
        double netBalance = 0;
        Map<String, Double> categories = new HashMap<>();

        // Assuming data spans 6 months for mock calculation
        int dataMonths = 6;

        for (BankAccountDTO account : accounts) {
            for (TransactionDTO txn : account.getTransactions()) {
                if (txn.getType() == TransactionType.CREDIT) {
                    totalIncome += txn.getAmount();
                } else {
                    totalExpense += txn.getAmount();

                    // Categorize based on narration
                    String category = categorize(txn.getNarration());
                    categories.put(category, categories.getOrDefault(category, 0.0) + txn.getAmount());
                }
            }
        }

        // Calculate monthly averages
        double monthlyIncome = dataMonths > 0 ? totalIncome / dataMonths : 0;
        double monthlyExpense = dataMonths > 0 ? totalExpense / dataMonths : 0;
        double avgBalance = (monthlyIncome - monthlyExpense); // Simplified avg balance

        // Stability Score: Simple heuristic (Income > Expense => Stable)
        double stabilityScore = monthlyIncome > monthlyExpense * 1.2 ? 0.9 : 0.5;

        return new FinancialSummary(
                monthlyIncome,
                monthlyExpense,
                avgBalance,
                stabilityScore,
                categories);
    }

    private String categorize(String narration) {
        String lower = narration.toLowerCase();
        if (lower.contains("salary"))
            return "SALARY";
        if (lower.contains("rent"))
            return "HOUSING";
        if (lower.contains("swiggy") || lower.contains("zomato"))
            return "FOOD";
        if (lower.contains("emi"))
            return "LOAN_REPAYMENT";
        return "OTHERS";
    }
}
