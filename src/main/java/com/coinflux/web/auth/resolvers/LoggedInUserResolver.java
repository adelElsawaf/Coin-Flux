package com.coinflux.web.auth.resolvers;


import com.coinflux.web.auth.annotations.LoggedInUser;
import com.coinflux.web.jwt.JwtService;
import com.coinflux.web.user.UserService;
import com.coinflux.web.user.dtos.UserDTO;
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

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractUsername(token); // subject is email
        return userService.getUserByEmail(email);
    }
}