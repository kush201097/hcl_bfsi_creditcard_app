package com.hcl.hackathon.authservice.mapper;

import com.hcl.hackathon.authservice.dto.SignupRequest;
import com.hcl.hackathon.authservice.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-09T16:10:17+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(SignupRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( request.getEmail() );
        user.phone( request.getPhone() );

        user.status( "ACTIVE" );

        return user.build();
    }
}
