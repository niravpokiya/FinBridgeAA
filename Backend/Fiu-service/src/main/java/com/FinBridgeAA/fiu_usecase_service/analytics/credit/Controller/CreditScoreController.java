package com.FinBridgeAA.fiu_usecase_service.analytics.credit.Controller;

import com.FinBridgeAA.fiu_usecase_service.analytics.credit.DTO.CreditScoreRequest;
import com.FinBridgeAA.fiu_usecase_service.analytics.credit.DTO.CreditScoreResponse;
import com.FinBridgeAA.fiu_usecase_service.analytics.credit.Service.CreditScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fiu/credit")
@RequiredArgsConstructor
public class CreditScoreController {

    private final CreditScoreService creditScoreService;

    @PostMapping("/score")
    public CreditScoreResponse calculateScore(
            @RequestBody CreditScoreRequest request) {

        return creditScoreService
                .calculateCreditScore(request.getUserId());
    }
}