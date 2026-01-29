package com.FinBridgeAA.Account_aggregator_service.Controller;

import com.FinBridgeAA.Account_aggregator_service.Orchestration.AaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/aa")
@RequiredArgsConstructor
public class AaController {

    private final AaService aaService;

    @PostMapping("/fetch-data")
    public ResponseEntity<List<Map<String, Object>>> fetchData(
            @RequestBody Map<String, String> request) {
        UUID userId = UUID.fromString(request.get("userId"));
        return ResponseEntity.ok(aaService.fetchData(userId));
    }
}
