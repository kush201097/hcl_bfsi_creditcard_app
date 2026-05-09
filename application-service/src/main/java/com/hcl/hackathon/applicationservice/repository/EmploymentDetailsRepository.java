package com.hcl.hackathon.applicationservice.repository;

import com.hcl.hackathon.applicationservice.entity.EmploymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmploymentDetailsRepository extends JpaRepository<EmploymentDetails, Long> {
    Optional<EmploymentDetails> findByCustomerId(Long customerId);
}
