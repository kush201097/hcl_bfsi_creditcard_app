package com.hcl.hackathon.cardservice.service;

import com.hcl.hackathon.cardservice.dto.CardIssuedEvent;
import com.hcl.hackathon.cardservice.dto.CardResponse;
import com.hcl.hackathon.cardservice.dto.CreditApprovedEvent;
import com.hcl.hackathon.cardservice.entity.CreditCard;
import com.hcl.hackathon.cardservice.mapper.CardMapper;
import com.hcl.hackathon.cardservice.repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CreditCardRepository creditCardRepository;
    private final CardEventProducer cardEventProducer;
    private final CardMapper cardMapper;
    private final Random random = new Random();

    @Transactional
    public void generateCard(CreditApprovedEvent event) {
        log.info("Generating card for customer: {}", event.getCustomerId());

        // Check if card already exists (idempotency)
        if (creditCardRepository.findByCustomerId(event.getCustomerId()).isPresent()) {
            log.warn("Card already exists for customer: {}", event.getCustomerId());
            return;
        }

        String cardNumber = generateCardNumber();
        String cvv = generateCvv();
        String expiryDate = generateExpiryDate();

        CreditCard card = CreditCard.builder()
                .customerId(event.getCustomerId())
                .cardNumber(cardNumber)
                .cvv(cvv)
                .expiryDate(expiryDate)
                .creditLimit(event.getCreditLimit())
                .status("ACTIVE")
                .build();

        creditCardRepository.save(card);
        log.info("Successfully generated and saved card for customer: {}", event.getCustomerId());

        // Publish event
        CardIssuedEvent issuedEvent = CardIssuedEvent.builder()
                .customerId(card.getCustomerId())
                .cardNumber(cardMapper.maskCardNumber(card.getCardNumber()))
                .status(card.getStatus())
                .build();
        cardEventProducer.publishCardIssuedEvent(issuedEvent);
    }

    public CardResponse getCardByCustomerId(Long customerId) {
        CreditCard card = creditCardRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Card not found for customer: " + customerId));
        return cardMapper.toResponse(card);
    }

    private String generateCardNumber() {
        // Mock generation (Visa style prefix 4)
        StringBuilder sb = new StringBuilder("4");
        for (int i = 0; i < 15; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generateCvv() {
        return String.format("%03d", random.nextInt(1000));
    }

    private String generateExpiryDate() {
        LocalDate expiry = LocalDate.now().plusYears(4);
        return expiry.format(DateTimeFormatter.ofPattern("MM/yy"));
    }
}
