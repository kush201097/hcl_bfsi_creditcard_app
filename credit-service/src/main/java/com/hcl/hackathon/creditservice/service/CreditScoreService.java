package com.hcl.hackathon.creditservice.service;

import com.hcl.hackathon.common.exception.BusinessException;
import com.hcl.hackathon.creditservice.client.CardServiceClient;
import com.hcl.hackathon.creditservice.dto.CreditScoreRequest;
import com.hcl.hackathon.creditservice.dto.CreditScoreResponse;
import com.hcl.hackathon.creditservice.entity.CustomerCreditScore;
import com.hcl.hackathon.creditservice.mapper.CreditScoreMapper;
import com.hcl.hackathon.creditservice.repository.CustomerCreditScoreRepository;
import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditScoreService {

    private final CustomerCreditScoreRepository repository;
    private final CardServiceClient cardServiceClient;
    private final CreditScoreMapper mapper;

    public CreditScoreResponse calculateOrFetch(CreditScoreRequest request) {
        try {
            return repository.findByCustomerId(request.getCustomerId())
                    .map(entity -> {
                        log.info("Credit score found in DB for customerId={}", request.getCustomerId());
                        return mapper.toResponse(entity, "DB");
                    })
                    .orElseGet(() -> resolveFromCardService(request));
        } catch (FeignException e) {
            log.error("Error retrieving card data from card-service for customerId={} and applicationId={} and Error={}",
                    request.getCustomerId(), request.getApplicationId(), e.getMessage(), e);
            throw new BusinessException(
                    "Failed to retrieve credit card information for customerId: " + request.getCustomerId(),
                    "CARD_SERVICE_ERROR");
        } catch (DataAccessException e) {
            log.error("Error saving credit score for customerId={} and applicationId={} and Error={}",
                    request.getCustomerId(), request.getApplicationId(), e.getMessage(), e);
            throw new BusinessException(
                    "Failed to persist credit score for customerId: " + request.getCustomerId(),
                    "CREDIT_SCORE_PERSISTENCE_ERROR");
        }
    }

    private CreditScoreResponse resolveFromCardService(CreditScoreRequest request) {
        // Calculate credit score based on income and card count
        int creditScore = calculateScore(request.getCurrentIncome(), request.getCustomerId());

        CustomerCreditScore saved = repository.save(CustomerCreditScore.builder()
                .customerId(request.getCustomerId())
                .creditScore(creditScore)
                .build());

        log.info("Calculated credit score={} for customerId={} from card-service data", creditScore, request.getCustomerId());
        return mapper.toResponse(saved, "CALCULATED");
    }

    private int calculateScore(double income, long customerId) {
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
