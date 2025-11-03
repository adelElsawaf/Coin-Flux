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
import com.coinflux.web.jwt.TokenType;
import com.coinflux.web.mail.MailService;
import com.coinflux.web.mail.dtos.requests.MailSendRequest;
import com.coinflux.web.mail.enums.MailType;
import com.coinflux.web.mail.exceptions.MailFailedException;
import com.coinflux.web.shared.constants.JWTConstants;
import com.coinflux.web.user.UserService;
import com.coinflux.web.user.dtos.UserDTO;
import com.coinflux.web.user.dtos.requests.CreateUserRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final MailService mailService;
    @Value("${frontend.base_url}")
    private String frontendUrl;


    private final AuthenticationManager authenticationManager;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userService.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        // Create user
        CreateUserRequest createUserRequest = authMapper.toCreateUserRequest(request);
        UserDTO createdUser = userService.createUser(createUserRequest).getUser();
        String activationToken = this.jwtService.generateToken(request.getEmail(),JWTConstants.ACTIVATION_TOKEN_EXPIRY_IN_MILLIS, TokenType.ACTIVATION);
        Map<String, Object> variables = Map.of(
                "username", createdUser.getFirstName(),
                "activationLink", frontendUrl+"/auth/activate?token=" + activationToken
        );

        mailService.createAndSendTemplateEmail(
                createdUser.getEmail(),
                "Activate your Coinflux Account",
                "activation-email", // name of the Thymeleaf template (without .html)
                variables,
                MailType.ACCOUNT_ACTIVATION
        );


        return new RegisterResponse("Registered Successfully , Please Activate to continue");
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

        String access_token;
        String refresh_token;
        try {
            access_token = jwtService.generateToken(user.getEmail(), JWTConstants.ACCESS_TOKEN_EXPIRY_IN_MILLIS, TokenType.ACCESS);
            refresh_token = jwtService.generateToken(user.getEmail(),JWTConstants.REFRESH_TOKEN_EXPIRY_IN_MILLIS,TokenType.REFRESH);
        } catch (Exception e) {
            throw new TokenGenerationException(e.getMessage());
        }

        return new LoginResponse(user,new AuthDTO(access_token,refresh_token));
    }

    public String refreshAccessToken (String refreshToken){
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null || !jwtService.isTokenValid(refreshToken, userEmail,TokenType.REFRESH)) {
            throw new InvalidCredentialsException();
        }
        return jwtService.generateToken(userEmail, JWTConstants.ACCESS_TOKEN_EXPIRY_IN_MILLIS,TokenType.REFRESH);
    }

    public boolean activateUser(String token) {
        String userEmail = jwtService.extractUsername(token);
        if (!jwtService.isTokenValid(token, userEmail,TokenType.ACTIVATION)) {
            return false;
        }
        // Check if already activated
        if (userService.isAlreadyActivated(userEmail)) {
            return false; // reject token reuse
        }

        userService.activateUser(userEmail);
        return true;
    }
}
