package com.hcl.hackathon.cardservice.mapper;

import com.hcl.hackathon.cardservice.dto.CardResponse;
import com.hcl.hackathon.cardservice.entity.CreditCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "maskedCardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    CardResponse toResponse(CreditCard creditCard);

    @Named("maskCardNumber")
    default String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
