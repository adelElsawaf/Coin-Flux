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

    UserEntity fromCreateRequest(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    UserEntity fromUpdateRequest(UpdateUserRequest request);
}