package com.FinBridgeAA.fiu_usecase_service.budget.DTO;

import lombok.Data;

@Data
public class BudgetRequest {
    private String userId;
    private String fromDate;
    private String toDate;
}