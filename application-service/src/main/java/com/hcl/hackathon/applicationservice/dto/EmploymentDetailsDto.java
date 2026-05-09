package com.hcl.hackathon.applicationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * DTO for Employment and Income Details of customer.
 * Includes validation for employment type, salary, and tenure details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for Employment and Income Details")
public class EmploymentDetailsDto {

    @Schema(description = "Unique employment details identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be a positive number")
    @Schema(description = "Reference to customer ID", example = "1", required = true)
    private Long customerId;

    @NotBlank(message = "Company name cannot be blank or null")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-&.,'()]*$", message = "Company name can only contain letters, numbers, spaces, and special characters (-, &, ., comma, apostrophe, parentheses)")
    @Schema(description = "Current employer's company name", example = "Acme Corporation", required = true)
    private String companyName;

    @NotBlank(message = "Employment type cannot be blank or null")
    @Pattern(regexp = "^(EMPLOYED|SELF_EMPLOYED|BUSINESS_OWNER|RETIRED|STUDENT|HOMEMAKER|UNEMPLOYED)$",
             message = "Employment type must be one of: EMPLOYED, SELF_EMPLOYED, BUSINESS_OWNER, RETIRED, STUDENT, HOMEMAKER, UNEMPLOYED")
    @Schema(description = "Type of employment", example = "EMPLOYED", required = true,
            allowableValues = {"EMPLOYED", "SELF_EMPLOYED", "BUSINESS_OWNER", "RETIRED", "STUDENT", "HOMEMAKER", "UNEMPLOYED"})
    private String employmentType;

    @NotNull(message = "Annual salary cannot be null")
    @Positive(message = "Annual salary must be greater than 0")
    @DecimalMax(value = "99999999.99", message = "Annual salary cannot exceed 99,999,999.99")
    @Digits(integer = 8, fraction = 2, message = "Annual salary must have at most 8 digits for integer part and 2 digits for decimal part")
    @Schema(description = "Annual salary in currency units (e.g., INR)", example = "500000.00", required = true)
    private BigDecimal annualSalary;

    @NotNull(message = "Years employed cannot be null")
    @Min(value = 0, message = "Years employed cannot be negative")
    @Max(value = 70, message = "Years employed cannot exceed 70")
    @Schema(description = "Number of years employed at current company", example = "5", required = true)
    private Integer yearsEmployed;
}
