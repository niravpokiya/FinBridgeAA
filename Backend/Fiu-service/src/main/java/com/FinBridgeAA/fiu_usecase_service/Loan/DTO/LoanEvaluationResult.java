package com.FinBridgeAA.fiu_usecase_service.Loan.DTO;

public class LoanEvaluationResult {

    private String riskCategory;     // LOW / MEDIUM / HIGH
    private int maxLoanAmount;
    private double interestRate;
    private String remarks;

    public LoanEvaluationResult(
            String riskCategory,
            int maxLoanAmount,
            double interestRate,
            String remarks) {
        this.riskCategory = riskCategory;
        this.maxLoanAmount = maxLoanAmount;
        this.interestRate = interestRate;
        this.remarks = remarks;
    }

    public String getRiskCategory() {
        return riskCategory;
    }

    public int getMaxLoanAmount() {
        return maxLoanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public String getRemarks() {
        return remarks;
    }
}