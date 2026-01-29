package com.FinBridgeAA.fiu_usecase_service.budget.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class BudgetResponse {
    private double totalExpense;
    private Map<String, Double> categoryBreakdown;
    private String topSpendingCategory;
}