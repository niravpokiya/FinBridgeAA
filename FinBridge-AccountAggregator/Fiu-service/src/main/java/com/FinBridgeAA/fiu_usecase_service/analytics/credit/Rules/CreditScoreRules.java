package com.FinBridgeAA.fiu_usecase_service.analytics.credit.Rules;

import org.springframework.stereotype.Component;

@Component
public class CreditScoreRules {

    public int calculateScore(
            double monthlyIncome,
            double monthlyEmi,
            int missedEmis
    ) {
        int score = 100;

        // EMI burden check
        double emiRatio = monthlyEmi / monthlyIncome;

        if (emiRatio > 0.5) score -= 30;
        else if (emiRatio > 0.3) score -= 15;

        // Missed EMI penalty
        score -= missedEmis * 20;

        // Income stability bonus
        if (monthlyIncome > 40000) score += 10;

        return Math.min(Math.max(score, 0), 100);
    }

    public String determineRisk(int score) {
        if (score >= 75) return "LOW";
        if (score >= 50) return "MEDIUM";
        return "HIGH";
    }
}
