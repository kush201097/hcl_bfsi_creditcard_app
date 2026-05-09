package com.hcl.hackathon.cardservice.repository;

import com.hcl.hackathon.cardservice.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    Optional<CreditCard> findByCustomerId(Long customerId);
}
