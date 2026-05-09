package com.hcl.hackathon.authservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.hackathon.authservice.entity.AuditEvent;
import com.hcl.hackathon.authservice.repository.AuditEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditEventRepository auditEventRepository;
    private final ObjectMapper objectMapper;

    @Async
    public void logEvent(String aggregateId, String aggregateType, String eventType, Object payload) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            
            AuditEvent event = AuditEvent.builder()
                    .aggregateId(aggregateId)
                    .aggregateType(aggregateType)
                    .eventType(eventType)
                    .payload(jsonPayload)
                    .build();
                    
            auditEventRepository.save(event);
            log.info("Audit logged: {} for {}", eventType, aggregateId);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize audit payload for event: {}", eventType, e);
        }
    }
}
