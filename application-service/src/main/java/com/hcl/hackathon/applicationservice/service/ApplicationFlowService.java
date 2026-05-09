package com.hcl.hackathon.applicationservice.service;

import com.hcl.hackathon.applicationservice.dto.CustomerDocVerificationDto;
import com.hcl.hackathon.applicationservice.dto.CustomerDto;
import com.hcl.hackathon.applicationservice.dto.EmploymentDetailsDto;

public interface ApplicationFlowService {
    CustomerDto addCustomer(CustomerDto customerDto);
    CustomerDocVerificationDto addKycDocuments(CustomerDocVerificationDto kycDto);
    EmploymentDetailsDto addEmploymentDetails(EmploymentDetailsDto employmentDto);
    com.hcl.hackathon.applicationservice.dto.ApplicationResponse submitApplication(Long customerId);
}
