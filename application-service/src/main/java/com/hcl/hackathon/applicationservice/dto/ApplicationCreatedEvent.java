package com.hcl.hackathon.applicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCreatedEvent {
    private String applicationId;
    private Long customerId;
    private BigDecimal annualIncome;
    private int yearsOfExperience;
}
