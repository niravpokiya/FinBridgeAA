package com.FinBridgeAA.Fip_mock_bank_service.Util;

import com.FinBridgeAA.Fip_mock_bank_service.Entity.BankAccountEntity;
import com.FinBridgeAA.Fip_mock_bank_service.Entity.TransactionEntity;
import com.FinBridgeAA.Fip_mock_bank_service.Entity.UserEntity;
import com.FinBridgeAA.Fip_mock_bank_service.Repository.BankAccountRepo;
import com.FinBridgeAA.Fip_mock_bank_service.Repository.TransactionRepo;
import com.FinBridgeAA.Fip_mock_bank_service.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
// now it will not run until we add implements CommandLineRunner and @Component tag...
// and @Override tag over method...
@RequiredArgsConstructor
public class BankDataSeeder implements CommandLineRunner {

    private final BankAccountRepo accountRepo;
    private final TransactionRepo txnRepo;
    private final UserRepo userRepo;

    // ---------------- helper methods ----------------

    private String randomNarration() {
        String[] narrations = {
                "Swiggy Order",
                "Zomato Food",
                "Amazon Purchase",
                "Flipkart Shopping",
                "Uber Ride",
                "Electricity Bill",
                "Mobile Recharge",
                "Rent Transfer",
                "Netflix Subscription",
                "Salary Credit"
        };
        return narrations[new Random().nextInt(narrations.length)];
    }

    private double randomAmount() {
        Random r = new Random();
        return r.nextInt(10) == 0
                ? r.nextInt(40000) + 30000   // CREDIT
                : -(r.nextInt(3000) + 100);  // DEBIT
    }

    private String randomType() {
        return new Random().nextInt(10) == 0 ? "CREDIT" : "DEBIT";
    }

    private LocalDate randomDate() {
        LocalDate start = LocalDate.now().minusMonths(12);
        long days = ChronoUnit.DAYS.between(start, LocalDate.now());
        return start.plusDays(new Random().nextInt((int) days));
    }

    private String randomPhone(int index) {
        return "9" + (800000000 + index); // safe synthetic phones
    }

    // ---------------- seeding logic ----------------

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) {
            System.out.println("✅ Mock bank data already exists. Skipping seeding.");
            return;
        }

        for (int u = 0; u < 5; u++) {

            // 1️⃣ Create User
            UserEntity user = new UserEntity();
            user.setUserId(UUID.randomUUID().toString());
            user.setPhone(randomPhone(u));
            user.setName("Demo User " + (u + 1));
            user.setPan("ABCDE12" + u + "F");

            userRepo.save(user);

            // 2️⃣ Create 2 accounts per user
            for (int acc = 0; acc < 2; acc++) {

                BankAccountEntity account = new BankAccountEntity();
                account.setAccountId(UUID.randomUUID().toString());
                account.setBankName(acc % 2 == 0 ? "HDFC Bank" : "ICICI Bank");
                account.setAccountType(acc == 0 ? "SAVINGS" : "SALARY");
                account.setUser(user); // 🔥 IMPORTANT

                accountRepo.save(account);

                // 3️⃣ Create transactions
                List<TransactionEntity> txns = new ArrayList<>();

                for (int i = 0; i < 400; i++) {
                    TransactionEntity txn = new TransactionEntity();
                    txn.setTransactionId(UUID.randomUUID().toString());
                    txn.setDate(randomDate());
                    txn.setAmount(randomAmount());
                    txn.setType(randomType());
                    txn.setNarration(randomNarration());
                    txn.setAccount(account);

                    txns.add(txn);
                }

                txnRepo.saveAll(txns);
            }
        }
    }
}
