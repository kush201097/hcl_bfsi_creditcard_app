package com.hcl.hackathon.applicationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO for Customer information in credit card application.
 * Includes validation constraints for mandatory and format fields.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for Customer information")
public class CustomerDto {

    @Schema(description = "Unique customer identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "User ID cannot be blank or null")
    @Size(min = 5, max = 50, message = "User ID must be between 5 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "User ID can only contain alphanumeric characters, underscores, and hyphens")
    @Schema(description = "Unique user identifier", example = "USR123456", required = true)
    private String userId;

    @NotBlank(message = "First name cannot be blank or null")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]*$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    @Schema(description = "Customer's first name", example = "John", required = true)
    private String firstName;

    @NotBlank(message = "Last name cannot be blank or null")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]*$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    @Schema(description = "Customer's last name", example = "Doe", required = true)
    private String lastName;

    @NotBlank(message = "Email cannot be blank or null")
    @Email(message = "Email should be valid and in proper format (e.g., john.doe@example.com)")
    @Schema(description = "Customer's email address", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Phone number cannot be blank or null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @Schema(description = "Customer's 10-digit phone number", example = "9876543210", required = true)
    private String phone;
}
