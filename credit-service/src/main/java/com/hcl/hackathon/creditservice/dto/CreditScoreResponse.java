package com.hcl.hackathon.creditservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditScoreResponse {

    private Long customerId;
    private Long applicationId;
    private Integer creditScore;
    private String source;
}
