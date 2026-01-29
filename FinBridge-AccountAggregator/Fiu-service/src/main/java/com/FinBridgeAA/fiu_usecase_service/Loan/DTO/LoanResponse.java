package com.FinBridgeAA.fiu_usecase_service.Loan.DTO;

import lombok.Data;

@Data
public class LoanResponse {
    private boolean eligible;
    private String decision;
    private String reason;

    public LoanResponse(boolean eligible, String decision, String reason) {
        this.eligible = eligible;
        this.decision = decision;
        this.reason = reason;
    }

    public boolean isEligible() {
        return eligible;
    }

    public String getDecision() {
        return decision;
    }

    public String getReason() {
        return reason;
    }

}