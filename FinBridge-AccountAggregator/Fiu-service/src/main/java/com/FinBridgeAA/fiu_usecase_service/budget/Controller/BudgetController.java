package com.FinBridgeAA.fiu_usecase_service.budget.Controller;

import com.FinBridgeAA.fiu_usecase_service.budget.DTO.BudgetRequest;
import com.FinBridgeAA.fiu_usecase_service.budget.DTO.BudgetResponse;
import com.FinBridgeAA.fiu_usecase_service.budget.Service.BudgetAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fiu/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetAnalysisService budgetAnalysisService;

    @PostMapping("/analyze")
    public BudgetResponse analyzeBudget(@RequestBody BudgetRequest request) {
        return budgetAnalysisService.analyze(request);
    }
}