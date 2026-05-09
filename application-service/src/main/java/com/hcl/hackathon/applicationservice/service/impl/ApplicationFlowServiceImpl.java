package com.hcl.hackathon.applicationservice.service.impl;

import com.hcl.hackathon.applicationservice.dto.CustomerDocVerificationDto;
import com.hcl.hackathon.applicationservice.dto.CustomerDto;
import com.hcl.hackathon.applicationservice.dto.EmploymentDetailsDto;
import com.hcl.hackathon.applicationservice.entity.Customer;
import com.hcl.hackathon.applicationservice.entity.CustomerDocVerification;
import com.hcl.hackathon.applicationservice.entity.EmploymentDetails;
import com.hcl.hackathon.applicationservice.mapper.CustomerDocVerificationMapper;
import com.hcl.hackathon.applicationservice.mapper.CustomerMapper;
import com.hcl.hackathon.applicationservice.mapper.EmploymentDetailsMapper;
import com.hcl.hackathon.applicationservice.repository.CustomerDocVerificationRepository;
import com.hcl.hackathon.applicationservice.repository.CustomerRepository;
import com.hcl.hackathon.applicationservice.repository.EmploymentDetailsRepository;
import com.hcl.hackathon.applicationservice.service.ApplicationFlowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationFlowServiceImpl implements ApplicationFlowService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    private final CustomerDocVerificationRepository docVerificationRepository;
    private final CustomerDocVerificationMapper docVerificationMapper;

    private final EmploymentDetailsRepository employmentDetailsRepository;
    private final EmploymentDetailsMapper employmentDetailsMapper;

    private final com.hcl.hackathon.applicationservice.repository.CreditCardApplicationRepository creditCardApplicationRepository;
    private final com.hcl.hackathon.applicationservice.service.ApplicationEventProducer applicationEventProducer;

    @Override
    @Transactional
    public CustomerDto addCustomer(CustomerDto customerDto) {
        log.info("Adding customer for user ID: {}", customerDto.getUserId());
        Customer customer = customerMapper.toEntity(customerDto);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    @Override
    @Transactional
    public CustomerDocVerificationDto addKycDocuments(CustomerDocVerificationDto kycDto) {
        log.info("Adding KYC documents for customer ID: {}", kycDto.getCustomerId());
        CustomerDocVerification docVerification = docVerificationMapper.toEntity(kycDto);
        docVerification = docVerificationRepository.save(docVerification);
        return docVerificationMapper.toDto(docVerification);
    }

    @Override
    @Transactional
    public EmploymentDetailsDto addEmploymentDetails(EmploymentDetailsDto employmentDto) {
        log.info("Adding employment details for customer ID: {}", employmentDto.getCustomerId());
        EmploymentDetails employmentDetails = employmentDetailsMapper.toEntity(employmentDto);
        employmentDetails = employmentDetailsRepository.save(employmentDetails);
        return employmentDetailsMapper.toDto(employmentDetails);
    }

    @Override
    @Transactional
    public com.hcl.hackathon.applicationservice.dto.ApplicationResponse submitApplication(Long customerId) {
        log.info("Submitting credit card application for customer ID: {}", customerId);

        // Verify existence of all required records
        customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        docVerificationRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("KYC documents not found"));

        EmploymentDetails employmentDetails = employmentDetailsRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Employment details not found"));

        // Check if application already exists
        if (creditCardApplicationRepository.findByCustomerId(customerId).isPresent()) {
            throw new RuntimeException("Application already exists for this customer");
        }

        String applicationId = "APP-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        com.hcl.hackathon.applicationservice.entity.CreditCardApplication application = com.hcl.hackathon.applicationservice.entity.CreditCardApplication.builder()
                .customerId(customerId)
                .applicationId(applicationId)
                .status("PENDING")
                .build();

        creditCardApplicationRepository.save(application);

        // Publish Event
        com.hcl.hackathon.applicationservice.dto.ApplicationCreatedEvent event = com.hcl.hackathon.applicationservice.dto.ApplicationCreatedEvent.builder()
                .applicationId(applicationId)
                .customerId(customerId)
                .annualIncome(employmentDetails.getAnnualSalary())
                .yearsOfExperience(employmentDetails.getYearsEmployed())
                .build();

        applicationEventProducer.publishApplicationCreatedEvent(event);

        return com.hcl.hackathon.applicationservice.dto.ApplicationResponse.builder()
                .applicationId(applicationId)
                .customerId(customerId)
                .status("PENDING")
                .message("Application submitted successfully and is pending review.")
                .build();
    }
}
