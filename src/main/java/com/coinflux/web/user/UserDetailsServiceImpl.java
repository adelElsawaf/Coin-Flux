package com.coinflux.web.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username) // or findByUsername
                .map(user -> User.builder()
                        .username(user.getEmail()) // or getUsername()
                        .password(user.getPassword())
                        .roles("USER") // change if you have roles in DB
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
