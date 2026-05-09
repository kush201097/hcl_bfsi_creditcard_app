package com.hcl.hackathon.applicationservice.controller;

import com.hcl.hackathon.applicationservice.dto.CustomerDocVerificationDto;
import com.hcl.hackathon.applicationservice.dto.CustomerDto;
import com.hcl.hackathon.applicationservice.dto.EmploymentDetailsDto;
import com.hcl.hackathon.applicationservice.service.ApplicationFlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing credit card applications.
 * Handles customer registration, document verification, and employment details.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
@Tag(name = "Application Management", description = "APIs for managing credit card applications including customer registration, KYC verification, and employment details")
public class ApplicationController {

    private final ApplicationFlowService applicationFlowService;

    /**
     * Register a new customer for credit card application.
     * 
     * @param customerDto the customer information with mandatory validations
     * @return ResponseEntity with HTTP 201 Created and saved customer details
     */
    @PostMapping("/customers")
    @Operation(summary = "Register a new customer", 
               description = "Creates a new customer record with basic information. This is the first step in credit card application process.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
                    description = "Customer created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDto.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Invalid input - validation failed. Check error response for field-level validation errors"),
        @ApiResponse(responseCode = "500", 
                    description = "Internal server error occurred while processing the request")
    })
    public ResponseEntity<CustomerDto> addCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("Received request to add customer with userId: {}", customerDto.getUserId());
        CustomerDto savedCustomer = applicationFlowService.addCustomer(customerDto);
        log.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    /**
     * Add KYC document verification details for a customer.
     * 
     * @param kycDto the document verification information with mandatory validations
     * @return ResponseEntity with HTTP 201 Created and saved verification details
     */
    @PostMapping("/kyc-documents")
    @Operation(summary = "Add customer document verification (KYC)", 
               description = "Adds KYC documents and verification details for a customer. Supports various document types including AADHAR, PAN, Passport, etc.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
                    description = "Document verification created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDocVerificationDto.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Invalid input - validation failed. Check error response for field-level validation errors. " +
                                 "Ensure customer ID exists and document type is valid."),
        @ApiResponse(responseCode = "500", 
                    description = "Internal server error occurred while processing the request")
    })
    public ResponseEntity<CustomerDocVerificationDto> addKycDocuments(
            @Valid @RequestBody CustomerDocVerificationDto kycDto) {
        log.info("Received request to add KYC documents for customerId: {}, documentType: {}", 
                 kycDto.getCustomerId(), kycDto.getDocumentType());
        CustomerDocVerificationDto savedKyc = applicationFlowService.addKycDocuments(kycDto);
        log.info("KYC document verification created successfully with ID: {}", savedKyc.getId());
        return new ResponseEntity<>(savedKyc, HttpStatus.CREATED);
    }

    /**
     * Add employment and income details for a customer.
     * 
     * @param employmentDto the employment information with mandatory validations
     * @return ResponseEntity with HTTP 201 Created and saved employment details
     */
    @PostMapping("/employment-details")
    @Operation(summary = "Add employment and income details", 
               description = "Adds employment and income details for a customer. This information is used for credit scoring and verification.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
                    description = "Employment details created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmploymentDetailsDto.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Invalid input - validation failed. Check error response for field-level validation errors. " +
                                 "Ensure customer ID exists, employment type is valid, and salary is positive."),
        @ApiResponse(responseCode = "500", 
                    description = "Internal server error occurred while processing the request")
    })
    public ResponseEntity<EmploymentDetailsDto> addEmploymentDetails(
            @Valid @RequestBody EmploymentDetailsDto employmentDto) {
        log.info("Received request to add employment details for customerId: {}, employmentType: {}", 
                 employmentDto.getCustomerId(), employmentDto.getEmploymentType());
        EmploymentDetailsDto savedEmployment = applicationFlowService.addEmploymentDetails(employmentDto);
        log.info("Employment details created successfully with ID: {}", savedEmployment.getId());
        return new ResponseEntity<>(savedEmployment, HttpStatus.CREATED);
    }

    /**
     * Submit the complete credit card application.
     * 
     * @param customerId the ID of the customer whose application is being submitted
     * @return ResponseEntity with HTTP 200 OK and application response details
     */
    @PostMapping("/submit/{customerId}")
    @Operation(summary = "Submit credit card application", 
               description = "Submits the application for processing. This will trigger event publishing to Kafka.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Application submitted successfully"),
        @ApiResponse(responseCode = "400", 
                    description = "Invalid customer ID or missing required steps"),
        @ApiResponse(responseCode = "500", 
                    description = "Internal server error")
    })
    public ResponseEntity<com.hcl.hackathon.applicationservice.dto.ApplicationResponse> submitApplication(
            @PathVariable Long customerId) {
        log.info("Received request to submit application for customerId: {}", customerId);
        com.hcl.hackathon.applicationservice.dto.ApplicationResponse response = applicationFlowService.submitApplication(customerId);
        log.info("Application submitted successfully with application ID: {}", response.getApplicationId());
        return ResponseEntity.ok(response);
    }
}
