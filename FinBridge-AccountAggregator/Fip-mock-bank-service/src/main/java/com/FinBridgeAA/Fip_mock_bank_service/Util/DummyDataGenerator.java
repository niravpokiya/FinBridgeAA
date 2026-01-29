package com.FinBridgeAA.Fip_mock_bank_service.Util;

import com.FinBridgeAA.Fip_mock_bank_service.DTO.BankAccountDTO;
import com.FinBridgeAA.Fip_mock_bank_service.DTO.TransactionDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DummyDataGenerator {

    public static List<BankAccountDTO> generateMockData(String userId) {
        return generateMockData(userId, 3);
    }

    public static List<BankAccountDTO> generateMockData(String userId, int months) {
        List<BankAccountDTO> accounts = new ArrayList<>();

        // Use a consistent seed for reproducibility based on userId
        Random random = new Random(userId.hashCode());

        // Account 1: Savings
        List<TransactionDTO> txns1 = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // Generate N months of data
        for (int i = 0; i < months; i++) {
            LocalDate baseDate = today.minusMonths(i);

            // Salary
            txns1.add(new TransactionDTO(UUID.randomUUID().toString(), baseDate.withDayOfMonth(1).toString(),
                    50000 + random.nextInt(5000), "CREDIT", "Salary - " + baseDate.getMonth()));

            // Rent
            txns1.add(new TransactionDTO(UUID.randomUUID().toString(), baseDate.withDayOfMonth(5).toString(), 15000,
                    "DEBIT", "Rent Payment"));

            // Expenses
            txns1.add(new TransactionDTO(UUID.randomUUID().toString(), baseDate.withDayOfMonth(10).toString(),
                    2000 + random.nextInt(1000), "DEBIT", "Groceries"));
            txns1.add(new TransactionDTO(UUID.randomUUID().toString(), baseDate.withDayOfMonth(20).toString(), 5000,
                    "DEBIT", "EMI - Personal Loan"));
        }

        accounts.add(new BankAccountDTO("HDFC Bank", "ACC-" + Math.abs(userId.hashCode()), "SAVINGS", txns1));

        // Account 2: Current (Business)
        List<TransactionDTO> txns2 = new ArrayList<>();
        for (int i = 0; i < months; i++) {
            LocalDate baseDate = today.minusMonths(i);
            txns2.add(new TransactionDTO(UUID.randomUUID().toString(), baseDate.withDayOfMonth(2).toString(),
                    20000 + random.nextInt(10000), "CREDIT", "Freelance Income"));
            txns2.add(new TransactionDTO(UUID.randomUUID().toString(), baseDate.withDayOfMonth(15).toString(),
                    8000 + random.nextInt(2000), "DEBIT", "Office Expenses"));
        }

        accounts.add(new BankAccountDTO("ICICI Bank", "ACC-" + Math.abs(userId.hashCode()) + "9", "CURRENT", txns2));

        return accounts;
    }
}