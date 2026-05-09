package com.hcl.hackathon.applicationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO for Customer Document Verification in KYC process.
 * Includes validation for document types and verification status.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for Customer Document Verification (KYC)")
public class CustomerDocVerificationDto {

    @Schema(description = "Unique document verification identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be a positive number")
    @Schema(description = "Reference to customer ID", example = "1", required = true)
    private Long customerId;

    @NotBlank(message = "Application ID cannot be blank or null")
    @Size(min = 5, max = 50, message = "Application ID must be between 5 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Application ID can only contain alphanumeric characters, underscores, and hyphens")
    @Schema(description = "Unique application identifier", example = "APP123456", required = true)
    private String applicationId;

    @NotBlank(message = "Document type cannot be blank or null")
    @Pattern(regexp = "^(AADHAR|PAN|VOTER_ID|PASSPORT|DRIVING_LICENSE|GST|TAN)$",
             message = "Document type must be one of: AADHAR, PAN, VOTER_ID, PASSPORT, DRIVING_LICENSE, GST, TAN")
    @Schema(description = "Type of identity document", example = "AADHAR", required = true,
            allowableValues = {"AADHAR", "PAN", "VOTER_ID", "PASSPORT", "DRIVING_LICENSE", "GST", "TAN"})
    private String documentType;

    @NotBlank(message = "Document number cannot be blank or null")
    @Size(min = 8, max = 100, message = "Document number must be between 8 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\-/]*$", message = "Document number can only contain alphanumeric characters, hyphens, and forward slashes")
    @Schema(description = "Document number or reference identifier", example = "123456789012", required = true)
    private String documentNumber;

    @NotBlank(message = "Verification status cannot be blank or null")
    @Pattern(regexp = "^(PENDING|VERIFIED|REJECTED|EXPIRED)$",
             message = "Verification status must be one of: PENDING, VERIFIED, REJECTED, EXPIRED")
    @Schema(description = "Current verification status of the document", example = "PENDING", required = true,
            allowableValues = {"PENDING", "VERIFIED", "REJECTED", "EXPIRED"})
    private String verificationStatus;
}
