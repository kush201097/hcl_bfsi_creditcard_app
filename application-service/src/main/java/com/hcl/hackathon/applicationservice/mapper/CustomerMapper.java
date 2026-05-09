package com.hcl.hackathon.applicationservice.mapper;

import com.hcl.hackathon.applicationservice.dto.CustomerDto;
import com.hcl.hackathon.applicationservice.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto toDto(Customer customer);
    Customer toEntity(CustomerDto customerDto);
}
