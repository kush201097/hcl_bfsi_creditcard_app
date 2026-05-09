package com.hcl.hackathon.applicationservice.repository;

import com.hcl.hackathon.applicationservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
