package com.coinflux.web.auth.resolvers;

import com.coinflux.web.auth.annotations.LoggedInUser;
import com.coinflux.web.jwt.JwtService;
import com.coinflux.web.jwt.TokenType;
import com.coinflux.web.user.UserService;
import com.coinflux.web.user.dtos.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoggedInUserResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;
    private final UserService userService;
    private final HttpServletRequest request;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(LoggedInUser.class) != null
                && parameter.getParameterType().equals(UserDTO.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {

        String token = extractTokenFromCookies(request);
        if (token == null) {
            return null; // or throw new UnauthorizedException();
        }

        // Extract username (email) from token
        String email = jwtService.extractUsername(token);

        // Validate token with username (your existing method)
        if (!jwtService.isTokenValid(token, email, TokenType.ACCESS)) {
            return null; // or throw new TokenExpiredException();
        }

        // Fetch user by email
        return userService.getUserByEmail(email);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
