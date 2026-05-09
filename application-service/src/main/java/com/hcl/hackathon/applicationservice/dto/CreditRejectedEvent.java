package com.hcl.hackathon.applicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditRejectedEvent {
    private Long customerId;
    private String applicationId;
    private String rejectionReason;
}
