package com.hcl.hackathon.applicationservice.repository;

import com.hcl.hackathon.applicationservice.entity.CreditCardApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardApplicationRepository extends JpaRepository<CreditCardApplication, Long> {
    Optional<CreditCardApplication> findByCustomerId(Long customerId);
}
