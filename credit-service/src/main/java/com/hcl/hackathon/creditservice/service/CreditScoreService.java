package com.hcl.hackathon.creditservice.service;

import com.hcl.hackathon.common.exception.BusinessException;
import com.hcl.hackathon.creditservice.client.CardServiceClient;
import com.hcl.hackathon.creditservice.dto.CreditScoreRequest;
import com.hcl.hackathon.creditservice.dto.CreditScoreResponse;
import com.hcl.hackathon.creditservice.entity.CustomerCreditScore;
import com.hcl.hackathon.creditservice.mapper.CreditScoreMapper;
import com.hcl.hackathon.creditservice.repository.CustomerCreditScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditScoreService {

    private final CustomerCreditScoreRepository repository;
    private final CardServiceClient cardServiceClient;
    private final CreditScoreMapper mapper;

    @Transactional
    public CreditScoreResponse calculateOrFetch(CreditScoreRequest request) {
        return repository.findByCustomerId(request.getCustomerId())
                .map(entity -> {
                    log.info("Credit score found in DB for customerId={}", request.getCustomerId());
                    return mapper.toResponse(entity, "DB");
                })
                .orElseGet(() -> resolveFromCardService(request));
    }

    private CreditScoreResponse resolveFromCardService(CreditScoreRequest request) {
        try {


            // Calculate credit score based on income and card count
            int creditScore = calculateScore(request.getCurrentIncome(), request.getCustomerId());

            CustomerCreditScore saved = repository.save(CustomerCreditScore.builder()
                    .customerId(request.getCustomerId())
                    .applicationId(request.getApplicationId())
                    .creditScore(creditScore)
                    .build());

            log.info("Calculated credit score={} for customerId={} from card-service data", creditScore, request.getCustomerId());
            return mapper.toResponse(saved, "CALCULATED");
        } catch (Exception e) {
            log.error("Error retrieving card data from card-service for customerId={}", request.getCustomerId(), e);
            throw new BusinessException(
                    "Failed to retrieve credit card information for customerId: " + request.getCustomerId(),
                    "CARD_SERVICE_ERROR");
        }
    }

    private int calculateScore(Double income, long customerId) {
        if (income > 200_000) {
            return 500;
        }

        // Get credit card count from card-service API
        Integer cardCount = cardServiceClient.getCustomerCardCount(customerId);
        log.info("Retrieved card count={} for customerId={} from card-service", cardCount, customerId);

        if (cardCount != null && cardCount >= 2) {
            return 300;
        } else if (income > 50_000) {
            return 150;
        }
        return 50;
    }
}
