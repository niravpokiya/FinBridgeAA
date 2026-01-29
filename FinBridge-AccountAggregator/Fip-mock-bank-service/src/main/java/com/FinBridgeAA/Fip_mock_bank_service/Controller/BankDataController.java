package com.FinBridgeAA.Fip_mock_bank_service.Controller;

import com.FinBridgeAA.Fip_mock_bank_service.DTO.BankAccountDTO;
import com.FinBridgeAA.Fip_mock_bank_service.Service.BankDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank")
public class BankDataController {

    private final BankDataService bankDataService;

    public BankDataController(BankDataService bankDataService) {
        this.bankDataService = bankDataService;
    }

    @GetMapping("/{userId}/financial-data")
    public ResponseEntity<List<BankAccountDTO>> getFinancialData(@PathVariable String userId) {
        return ResponseEntity.ok(bankDataService.getFinancialData(userId));
    }

    @GetMapping("/data")
    public ResponseEntity<List<BankAccountDTO>> fetchBankData(
            @RequestParam String userId,
            @RequestParam int months) {

        // ✅ API-level validation
        if (months < 1) {
            throw new IllegalArgumentException(
                    "Months must be greater than or equal to 1");
        }

        return ResponseEntity.ok(bankDataService.getBankData(userId, months));
    }
}