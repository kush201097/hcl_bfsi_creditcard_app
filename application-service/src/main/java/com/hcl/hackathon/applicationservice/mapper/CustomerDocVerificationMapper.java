package com.hcl.hackathon.applicationservice.mapper;

import com.hcl.hackathon.applicationservice.dto.CustomerDocVerificationDto;
import com.hcl.hackathon.applicationservice.entity.CustomerDocVerification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerDocVerificationMapper {
    CustomerDocVerificationDto toDto(CustomerDocVerification entity);
    CustomerDocVerification toEntity(CustomerDocVerificationDto dto);
}
