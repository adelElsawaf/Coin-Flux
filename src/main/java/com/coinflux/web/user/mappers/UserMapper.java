package com.coinflux.web.user.mappers;

import com.coinflux.web.user.UserEntity;
import com.coinflux.web.user.dtos.UserDTO;
import com.coinflux.web.user.dtos.requests.CreateUserRequest;
import com.coinflux.web.user.dtos.requests.UpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(UserEntity user);


    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    UserEntity fromCreateRequest(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    UserEntity fromUpdateRequest(UpdateUserRequest request);
}