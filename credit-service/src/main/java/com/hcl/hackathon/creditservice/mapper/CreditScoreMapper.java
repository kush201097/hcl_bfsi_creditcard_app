package com.hcl.hackathon.creditservice.mapper;

import com.hcl.hackathon.creditservice.dto.CreditScoreResponse;
import com.hcl.hackathon.creditservice.entity.CustomerCreditScore;
import org.springframework.stereotype.Component;

@Component
public class CreditScoreMapper {

    public CreditScoreResponse toResponse(CustomerCreditScore entity, String source) {
        return CreditScoreResponse.builder()
                .customerId(entity.getCustomerId())
                .applicationId(entity.getApplicationId())
                .creditScore(entity.getCreditScore())
                .source(source)
                .build();
    }
}
