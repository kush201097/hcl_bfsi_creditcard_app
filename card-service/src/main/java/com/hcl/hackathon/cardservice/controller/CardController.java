package com.hcl.hackathon.cardservice.controller;

import com.hcl.hackathon.cardservice.dto.CardResponse;
import com.hcl.hackathon.cardservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CardResponse> getCardByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(cardService.getCardByCustomerId(customerId));
    }
}
