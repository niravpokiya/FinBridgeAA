package com.FinBridgeAA.fiu_usecase_service.analytics.credit.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreditScoreResponse {
    private int creditScore;
    private String riskLevel;
    private String remark;

    public int getCreditScore() {
        return creditScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getRemark() {
        return remark;
    }
}