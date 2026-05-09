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
public class CreditApprovedEvent {
    private Long customerId;
    private String applicationId;
    private BigDecimal creditLimit;
}
