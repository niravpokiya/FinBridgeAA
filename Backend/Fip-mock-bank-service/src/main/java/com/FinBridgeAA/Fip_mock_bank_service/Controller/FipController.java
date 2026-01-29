package com.FinBridgeAA.Fip_mock_bank_service.Controller;

import com.FinBridgeAA.Fip_mock_bank_service.DTO.BankAccountDTO;
import com.FinBridgeAA.Fip_mock_bank_service.Service.FipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fip")
@RequiredArgsConstructor
public class FipController {

    private final FipService fipService;

    @GetMapping("/financial-data/{userId}")
    public List<BankAccountDTO> getFinancialData(@PathVariable UUID userId) {
        return fipService.getFinancialData(userId);
    }
}
