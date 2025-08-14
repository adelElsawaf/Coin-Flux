package com.coinflux.web.auth;

import com.coinflux.web.auth.dtos.AuthDTO;
import com.coinflux.web.auth.dtos.requests.LoginRequest;
import com.coinflux.web.auth.dtos.requests.RegisterRequest;
import com.coinflux.web.auth.dtos.responses.LoginResponse;
import com.coinflux.web.auth.dtos.responses.RegisterResponse;
import com.coinflux.web.auth.exceptions.InvalidCredentialsException;
import com.coinflux.web.auth.exceptions.UserAlreadyExistsException;
import com.coinflux.web.auth.exceptions.TokenGenerationException;
import com.coinflux.web.auth.mappers.AuthMapper;
import com.coinflux.web.jwt.JwtService;
import com.coinflux.web.user.UserService;
import com.coinflux.web.user.dtos.UserDTO;
import com.coinflux.web.user.dtos.requests.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userService.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        // Create user
        CreateUserRequest createUserRequest = authMapper.toCreateUserRequest(request);
        UserDTO createdUser = userService.createUser(createUserRequest).getUser();

        // Generate token
        String token;
        try {
            token = jwtService.generateToken(createdUser.getEmail());
        } catch (Exception e) {
            throw new TokenGenerationException(e.getMessage());
        }

        return new RegisterResponse(createdUser, new AuthDTO(token));
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }

        UserDTO user = userService.getUserByEmail(request.getEmail());

        String token;
        try {
            token = jwtService.generateToken(user.getEmail());
        } catch (Exception e) {
            throw new TokenGenerationException(e.getMessage());
        }

        return new LoginResponse(user, new AuthDTO(token));
    }
}
