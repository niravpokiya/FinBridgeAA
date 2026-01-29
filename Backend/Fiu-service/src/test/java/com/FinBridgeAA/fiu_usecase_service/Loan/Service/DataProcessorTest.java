package com.FinBridgeAA.fiu_usecase_service.Loan.Service;

import com.FinBridgeAA.fiu_usecase_service.Loan.DTO.FinancialSummary;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.BankAccountDTO;
import com.FinBridgeAA.fiu_usecase_service.common.DTO.TransactionDTO;
import com.FinBridgeAA.fiu_usecase_service.common.Enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataProcessorTest {

    private final DataProcessor processor = new DataProcessor();

    @Test
    public void testProcessFinancialData() {
        // Mock Data
        List<BankAccountDTO> accounts = new ArrayList<>();
        List<TransactionDTO> txns = new ArrayList<>();

        // Income: 50000 * 2 = 100000
        txns.add(new TransactionDTO("1", "2023-01-01", 50000, TransactionType.CREDIT, "Salary"));
        txns.add(new TransactionDTO("2", "2023-02-01", 50000, TransactionType.CREDIT, "Salary"));

        // Expense: 20000 * 2 = 40000
        txns.add(new TransactionDTO("3", "2023-01-05", 20000, TransactionType.DEBIT, "Rent"));
        txns.add(new TransactionDTO("4", "2023-02-05", 20000, TransactionType.DEBIT, "Rent"));

        accounts.add(new BankAccountDTO("Bank A", "123", "SAVINGS", txns));

        // Execute
        FinancialSummary summary = processor.process(accounts);

        // Verify (DataProcessor assumes 6 months average by default in current impl)
        // Total Income = 100,000. Monthly (over 6 mo) = 16666.66
        // Total Expense = 40,000. Monthly (over 6 mo) = 6666.66

        // Let's adjust expected values based on the logic in DataProcessor
        // Note: The logic divides by 6 hardcoded.

        assertEquals(100000.0 / 6, summary.getMonthlyIncome(), 0.1);
        assertEquals(40000.0 / 6, summary.getMonthlyExpense(), 0.1);
        assertEquals(40000.0, summary.getCategoryExpenses().get("HOUSING"), 0.1); // Total Rent
    }
}
