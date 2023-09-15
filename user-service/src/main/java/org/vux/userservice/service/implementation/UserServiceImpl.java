package org.vux.userservice.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vux.userservice.entity.User;
import org.vux.userservice.enums.Role;
import org.vux.userservice.repository.UserRepository;
import org.vux.userservice.request.RegisterRequest;
import org.vux.userservice.response.CommonResponse;
import org.vux.userservice.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CommonResponse createUser(RegisterRequest registerRequest) {
        userRepository.save(
                User.builder()
                        .email(registerRequest.getEmail())
                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                        .role(Role.USER)
                        .build()
        );

        return CommonResponse.builder()
                .message("User created successfully")
                .status(200)
                .build();
    }

    @Override
    public CommonResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return CommonResponse.builder()
                    .message("User not found")
                    .status(404)
                    .build();
        }

        return CommonResponse.builder()
                .message("Get user successfully")
                .status(200)
                .data(user)
                .build();
    }
}
