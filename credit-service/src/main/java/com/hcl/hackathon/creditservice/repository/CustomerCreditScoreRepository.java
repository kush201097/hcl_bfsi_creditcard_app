package com.hcl.hackathon.creditservice.repository;

import com.hcl.hackathon.creditservice.entity.CustomerCreditScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerCreditScoreRepository extends JpaRepository<CustomerCreditScore, Long> {

    Optional<CustomerCreditScore> findByCustomerId(Long customerId);
}
