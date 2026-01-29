package com.FinBridgeAA.Account_aggregator_service.Orchestration;

import com.FinBridgeAA.Account_aggregator_service.Audit.AuditLogger;
import com.FinBridgeAA.Account_aggregator_service.Client.FipClient;
import com.FinBridgeAA.Account_aggregator_service.Client.FIUClient;
import com.FinBridgeAA.Account_aggregator_service.Crypto.EncryptionService;
import com.FinBridgeAA.Account_aggregator_service.DTO.ConsentArtifact;
import com.FinBridgeAA.Account_aggregator_service.DTO.DataRequestDTO;
import com.FinBridgeAA.Account_aggregator_service.DTO.EncryptedPayload;
import com.FinBridgeAA.Account_aggregator_service.Policy.AAPolicyEnforcer;
import com.FinBridgeAA.Account_aggregator_service.Streaming.InMemoryDataPipe;
import com.FinBridgeAA.Account_aggregator_service.Validation.ConsentValidator;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

@Component
public class DataFlowOrchestrator {

    private final ConsentValidator consentValidator;
    private final AAPolicyEnforcer policyEnforcer;
    private final FipClient fipClient;
    private final EncryptionService encryptionService;
    private final FIUClient fiuClient;
    private final AuditLogger auditLogger;
    private final ObjectMapper objectMapper;

    public DataFlowOrchestrator(
            ConsentValidator consentValidator,
            AAPolicyEnforcer policyEnforcer,
            FipClient fipClient,
            EncryptionService encryptionService,
            FIUClient fiuClient,
            AuditLogger auditLogger,
            ObjectMapper objectMapper) {

        this.consentValidator = consentValidator;
        this.policyEnforcer = policyEnforcer;
        this.fipClient = fipClient;
        this.encryptionService = encryptionService;
        this.fiuClient = fiuClient;
        this.auditLogger = auditLogger;
        this.objectMapper = objectMapper;
    }

    public void execute(DataRequestDTO request) {

        ConsentArtifact consent = consentValidator.validate(request.getConsentId());

        policyEnforcer.enforce(consent, request);

        InMemoryDataPipe pipe = new InMemoryDataPipe();
        // 3. Fetch Data from FIP
        // In real AA, this involves asking FIP to prepare data, then fetching it.
        // Simplified: Direct fetch using userId from consent
        List<Map<String, Object>> financialData = fipClient.fetchFinancialData(consent.getCustomerId());

        try {
            byte[] serializedData = objectMapper.writeValueAsBytes(financialData);
            pipe.write(serializedData);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing financial data", e);
        }

        EncryptedPayload encryptedPayload = encryptionService.encrypt(pipe.read(), request.getFiuId());

        pipe.destroy();

        fiuClient.deliver(encryptedPayload);

        auditLogger.logSuccess(request, consent);
    }
}