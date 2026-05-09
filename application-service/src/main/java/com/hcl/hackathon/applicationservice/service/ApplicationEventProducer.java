package com.hcl.hackathon.applicationservice.service;

import com.hcl.hackathon.applicationservice.dto.ApplicationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishApplicationCreatedEvent(ApplicationCreatedEvent event) {
        log.info("Publishing application-created event for app ID: {}", event.getApplicationId());
        kafkaTemplate.send("application-created", event);
    }
}
