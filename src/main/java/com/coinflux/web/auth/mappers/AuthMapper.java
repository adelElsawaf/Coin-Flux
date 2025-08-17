package com.coinflux.web.auth.mappers;


import com.coinflux.web.auth.dtos.requests.RegisterRequest;
import com.coinflux.web.user.dtos.requests.CreateUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    default CreateUserRequest toCreateUserRequest(RegisterRequest request) {
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setFirstName(request.getFirstName());
        createRequest.setLastName(request.getLastName());
        createRequest.setUsername(request.getUsername());
        createRequest.setEmail(request.getEmail());
        createRequest.setPassword(request.getPassword());
        return createRequest;
    }
}