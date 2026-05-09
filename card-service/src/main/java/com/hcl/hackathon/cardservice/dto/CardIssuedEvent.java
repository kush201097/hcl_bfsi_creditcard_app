package com.hcl.hackathon.cardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardIssuedEvent {
    private Long customerId;
    private String cardNumber;
    private String status;
}
