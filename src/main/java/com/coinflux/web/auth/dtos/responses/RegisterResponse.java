package com.coinflux.web.auth.dtos.responses;


import com.coinflux.web.auth.dtos.AuthDTO;
import com.coinflux.web.user.dtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private String message;
}