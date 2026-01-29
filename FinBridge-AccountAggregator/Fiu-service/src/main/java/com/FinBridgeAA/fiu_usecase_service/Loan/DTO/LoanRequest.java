package com.FinBridgeAA.fiu_usecase_service.Loan.DTO;

import lombok.Data;

@Data
public class LoanRequest {
    private String userId;
    private int requestedAmount;
    private int tenureMonths;
    private boolean forceNewConsent; // NEW Field

    public LoanRequest() {
    }

    public LoanRequest(String userId, int requestedAmount, int tenureMonths, boolean forceNewConsent) {
        this.userId = userId;
        this.requestedAmount = requestedAmount;
        this.tenureMonths = tenureMonths;
        this.forceNewConsent = forceNewConsent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(int requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public boolean isForceNewConsent() {
        return forceNewConsent;
    }

    public void setForceNewConsent(boolean forceNewConsent) {
        this.forceNewConsent = forceNewConsent;
    }
}