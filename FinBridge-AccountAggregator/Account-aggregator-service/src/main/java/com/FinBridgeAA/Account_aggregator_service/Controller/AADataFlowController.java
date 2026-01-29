package com.FinBridgeAA.Account_aggregator_service.Controller;

import com.FinBridgeAA.Account_aggregator_service.DTO.DataRequestDTO;
import com.FinBridgeAA.Account_aggregator_service.Orchestration.DataFlowOrchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aa")
public class AADataFlowController {

    private final DataFlowOrchestrator orchestrator;

    public AADataFlowController(DataFlowOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/data/flow")
    public ResponseEntity<String> startDataFlow(@RequestBody DataRequestDTO request) {
        orchestrator.execute(request);
        return ResponseEntity.ok("DATA_SECURELY_DELIVERED_TO_FIU");
    }
}
