package com.hcl.hackathon.cardservice.service;

import com.hcl.hackathon.cardservice.dto.CardIssuedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishCardIssuedEvent(CardIssuedEvent event) {
        log.info("Publishing card-issued event for customer ID: {}", event.getCustomerId());
        kafkaTemplate.send("card-issued", event);
    }
}
