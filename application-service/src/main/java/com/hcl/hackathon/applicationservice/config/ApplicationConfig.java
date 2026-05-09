package com.hcl.hackathon.applicationservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI/Swagger documentation.
 * Provides custom OpenAPI bean with API metadata and contact information.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Customized OpenAPI configuration for Credit Card Application API.
     *
     * @return OpenAPI object with custom API info
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Credit Card Application API")
                        .version("1.0.0")
                        .description("REST APIs for managing credit card applications, customer information, document verification, and employment details. " +
                                "This API handles the complete workflow of credit card application including KYC verification and income verification.")
                        .contact(new Contact()
                                .name("HCL BFSI Credit Card Team")
                                .email("support@hcl.com")
                                .url("https://www.hcl.com")));
    }
}

