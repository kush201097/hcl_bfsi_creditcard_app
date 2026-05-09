package com.hcl.hackathon.cardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {
    private Long customerId;
    private String maskedCardNumber;
    private String expiryDate;
    private BigDecimal creditLimit;
    private String status;
}
