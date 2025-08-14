package com.coinflux.web.user;

import com.coinflux.web.user.dtos.UserDTO;
import com.coinflux.web.user.dtos.requests.CreateUserRequest;
import com.coinflux.web.user.dtos.requests.UpdateUserRequest;
import com.coinflux.web.user.dtos.responses.CreateUserResponse;
import com.coinflux.web.user.dtos.responses.UpdateUserResponse;
import com.coinflux.web.user.dtos.responses.GetUserResponse;
import com.coinflux.web.user.exception.UserNotFoundException;
import com.coinflux.web.user.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder; // ✅ Add this

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        UserEntity user = mapper.fromCreateRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // ✅ Important
        userRepository.save(user);
        UserDTO dto = mapper.toDTO(user);
        return new CreateUserResponse(dto);
    }

    @Transactional(readOnly = true)
    public GetUserResponse getUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return new GetUserResponse(mapper.toDTO(user));
    }

    @Transactional
    public UpdateUserResponse updateUser(Long id, UpdateUserRequest request) {
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        existing.setUsername(request.getUsername());
        existing.setEmail(request.getEmail());
        existing.setPassword(request.getPassword());

        userRepository.save(existing);
        return new UpdateUserResponse(mapper.toDTO(existing));
    }
    public UserDTO getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return mapper.toDTO(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}