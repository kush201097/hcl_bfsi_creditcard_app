package com.hcl.hackathon.creditservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "card-service")
public interface CardServiceClient {
    @GetMapping("/api/v1/cards/customer/{customerId}/count")
    Integer getCustomerCardCount(@PathVariable("customerId") Long customerId);
}

