package com.coinflux.web.user.dtos.responses;

import com.coinflux.web.user.dtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {
    private UserDTO user;
}
