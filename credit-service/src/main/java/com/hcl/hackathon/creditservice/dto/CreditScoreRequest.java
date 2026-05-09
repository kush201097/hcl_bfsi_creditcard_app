package com.hcl.hackathon.creditservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditScoreRequest {

    private Long customerId;
    private Long applicationId;
    private Double currentIncome;
}
