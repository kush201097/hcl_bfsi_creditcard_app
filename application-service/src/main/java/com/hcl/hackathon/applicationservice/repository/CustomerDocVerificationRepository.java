package com.hcl.hackathon.applicationservice.repository;

import com.hcl.hackathon.applicationservice.entity.CustomerDocVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDocVerificationRepository extends JpaRepository<CustomerDocVerification, Long> {
    Optional<CustomerDocVerification> findByCustomerId(Long customerId);
}
