package com.hcl.hackathon.applicationservice.service;

import com.hcl.hackathon.applicationservice.dto.CardIssuedEvent;
import com.hcl.hackathon.applicationservice.dto.CreditRejectedEvent;
import com.hcl.hackathon.applicationservice.entity.CreditCardApplication;
import com.hcl.hackathon.applicationservice.repository.CreditCardApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationEventConsumer {

    private final CreditCardApplicationRepository applicationRepository;

    @KafkaListener(topics = "card-issued", groupId = "application-service-group")
    public void consumeCardIssuedEvent(CardIssuedEvent event) {
        log.info("Received card-issued event for customer ID: {}", event.getCustomerId());
        
        Optional<CreditCardApplication> applicationOpt = applicationRepository.findByCustomerId(event.getCustomerId());
        if (applicationOpt.isPresent()) {
            CreditCardApplication application = applicationOpt.get();
            application.setStatus("CARD_ISSUED");
            applicationRepository.save(application);
            log.info("Updated application status to CARD_ISSUED for customer ID: {}", event.getCustomerId());
        } else {
            log.warn("Application not found for customer ID: {}", event.getCustomerId());
        }
    }

    @KafkaListener(topics = "credit-rejected", groupId = "application-service-group")
    public void consumeCreditRejectedEvent(CreditRejectedEvent event) {
        log.info("Received credit-rejected event for customer ID: {}", event.getCustomerId());

        Optional<CreditCardApplication> applicationOpt = applicationRepository.findByCustomerId(event.getCustomerId());
        if (applicationOpt.isPresent()) {
            CreditCardApplication application = applicationOpt.get();
            application.setStatus("REJECTED");
            applicationRepository.save(application);
            log.info("Updated application status to REJECTED for customer ID: {}", event.getCustomerId());
        } else {
            log.warn("Application not found for customer ID: {}", event.getCustomerId());
        }
    }
}
