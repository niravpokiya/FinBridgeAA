package com.FinBridgeAA.fiu_usecase_service.budget.Rules;

import com.FinBridgeAA.fiu_usecase_service.common.DTO.TransactionDTO;
import org.springframework.stereotype.Component;

@Component
public class BudgetRules {

    public String categorize(TransactionDTO txn) {

        if (txn.getNarration() == null)
            return "Others";

        String narration = txn.getNarration().toLowerCase();

        if (containsAny(narration,
                "swiggy", "zomato", "dominos", "pizza",
                "kfc", "mcdonald", "restaurant", "cafe"))
            return "Food";

        if (containsAny(narration,
                "rent", "house rent", "flat rent"))
            return "Rent";

        if (containsAny(narration,
                "emi", "loan repayment", "home loan",
                "personal loan", "car loan"))
            return "EMI";

        if (containsAny(narration,
                "amazon", "flipkart", "myntra", "ajio",
                "meesho", "snapdeal", "shop",
                "store", "mall"))
            return "Shopping";

        if (containsAny(narration,
                "uber", "ola", "rapido", "metro",
                "irctc", "train", "flight", "bus"))
            return "Travel";

        if (containsAny(narration,
                "electricity", "water bill", "gas",
                "broadband", "wifi", "mobile recharge",
                "jio", "airtel", "vi"))
            return "Bills";

        if (containsAny(narration,
                "netflix", "prime", "hotstar",
                "spotify", "movie", "cinema"))
            return "Entertainment";

        if (containsAny(narration,
                "hospital", "pharmacy", "medical",
                "apollo", "clinic"))
            return "Medical";

        return "Others";
    }

    private boolean containsAny(String narration, String... keywords) {
        for (String keyword : keywords) {
            if (narration.contains(keyword))
                return true;
        }
        return false;
    }
}