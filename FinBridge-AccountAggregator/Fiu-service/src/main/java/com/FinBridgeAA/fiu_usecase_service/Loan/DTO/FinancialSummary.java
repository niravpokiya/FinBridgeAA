package com.FinBridgeAA.fiu_usecase_service.Loan.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class FinancialSummary {
    private double monthlyIncome;
    private double monthlyExpense;
    private double averageBalance;
    private double stabilityScore; // 0.0 to 1.0 (1.0 is very stable)
    private Map<String, Double> categoryExpenses;

    public FinancialSummary(double monthlyIncome, double monthlyExpense, double averageBalance, double stabilityScore,
            Map<String, Double> categoryExpenses) {
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpense = monthlyExpense;
        this.averageBalance = averageBalance;
        this.stabilityScore = stabilityScore;
        this.categoryExpenses = categoryExpenses;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public double getMonthlyExpense() {
        return monthlyExpense;
    }

    public double getAverageBalance() {
        return averageBalance;
    }

    public double getStabilityScore() {
        return stabilityScore;
    }

    public Map<String, Double> getCategoryExpenses() {
        return categoryExpenses;
    }
}
