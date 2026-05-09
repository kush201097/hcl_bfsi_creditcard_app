package com.hcl.hackathon.cardservice.service;

import com.hcl.hackathon.cardservice.dto.CreditApprovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardEventConsumer {

    private final CardService cardService;

    @KafkaListener(topics = "credit-approved", groupId = "card-service-group")
    public void consumeCreditApprovedEvent(CreditApprovedEvent event) {
        log.info("Received credit-approved event for customer: {}", event.getCustomerId());
        try {
            cardService.generateCard(event);
        } catch (Exception e) {
            log.error("Error processing credit-approved event for customer: {}", event.getCustomerId(), e);
        }
    }
}
