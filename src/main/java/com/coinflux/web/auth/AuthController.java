package com.coinflux.web.auth;

import com.coinflux.web.auth.dtos.requests.LoginRequest;
import com.coinflux.web.auth.dtos.requests.RegisterRequest;
import com.coinflux.web.auth.dtos.responses.LoginResponse;
import com.coinflux.web.auth.dtos.responses.RegisterResponse;
import com.coinflux.web.auth.AuthService;
import com.coinflux.web.auth.exceptions.InvalidCredentialsException;
import com.coinflux.web.shared.constants.JWTConstants;
import com.coinflux.web.user.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", loginResponse.getAuthDto().getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JWTConstants.ACCESS_TOKEN_EXPIRY_IN_MILLIS / 1000)
                .sameSite("Strict")
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginResponse.getAuthDto().getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JWTConstants.REFRESH_TOKEN_EXPIRY_IN_MILLIS / 1000)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(loginResponse.getUser());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new InvalidCredentialsException();
        }

        String newAccessToken = authService.refreshAccessToken(refreshToken);

        ResponseCookie newAccessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JWTConstants.ACCESS_TOKEN_EXPIRY_IN_MILLIS / 1000)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString())
                .build();
    }
    @PostMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        boolean activated = authService.activateUser(token);
        if (activated) {
            return ResponseEntity.ok("Account activated successfully!");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired activation token.");
        }
    }

}
