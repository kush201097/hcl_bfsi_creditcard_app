package com.hcl.hackathon.applicationservice.mapper;

import com.hcl.hackathon.applicationservice.dto.EmploymentDetailsDto;
import com.hcl.hackathon.applicationservice.entity.EmploymentDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmploymentDetailsMapper {
    EmploymentDetailsDto toDto(EmploymentDetails entity);
    EmploymentDetails toEntity(EmploymentDetailsDto dto);
}
