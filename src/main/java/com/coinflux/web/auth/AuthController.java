package com.coinflux.web.auth;

import com.coinflux.web.auth.dtos.requests.LoginRequest;
import com.coinflux.web.auth.dtos.requests.RegisterRequest;
import com.coinflux.web.auth.dtos.responses.LoginResponse;
import com.coinflux.web.auth.dtos.responses.RegisterResponse;
import com.coinflux.web.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
