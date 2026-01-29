package com.FinBridgeAA.fiu_usecase_service.Loan.Controller;

import com.FinBridgeAA.fiu_usecase_service.Loan.DTO.LoanRequest;
import com.FinBridgeAA.fiu_usecase_service.Loan.DTO.LoanResponse;
import com.FinBridgeAA.fiu_usecase_service.Loan.Service.LoanEligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanController {

    private final LoanEligibilityService service;

    public LoanController(LoanEligibilityService service) {
        this.service = service;
    }

    @PostMapping("/check-eligibility")
    public ResponseEntity<LoanResponse> checkEligibility(
            @RequestBody LoanRequest request,
            @RequestHeader(value = "X-USER-ID", required = false) String userId) {
        if (userId == null) {
            System.out.println("DEBUG: Header X-USER-ID is MISSING in Controller");
            // temporary fallback or let it fail later
        } else {
            System.out.println("DEBUG: Header X-USER-ID received: " + userId);
        }
        // Ensure userId from header matches request (security check) or just set it
        request.setUserId(userId);
        return ResponseEntity.ok(service.isEligible(request));
    }

    @org.springframework.web.bind.annotation.GetMapping("/history")
    public ResponseEntity<java.util.List<com.FinBridgeAA.fiu_usecase_service.Loan.Entity.LoanApplication>> getHistory(
            @RequestHeader(value = "X-USER-ID", required = false) String userId) {
        if (userId == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.getHistory(userId));
    }
}