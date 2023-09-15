package org.vux.authservice.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vux.authservice.dto.UserAuthInfo;
import org.vux.authservice.request.LoginRequest;
import org.vux.authservice.request.RegisterRequest;
import org.vux.authservice.response.CommonResponse;
import org.vux.authservice.service.AuthService;
import org.vux.authservice.service.JwtService;
import org.vux.authservice.service.RedisService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final WebClient.Builder webClientBuilder;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
//    private final AuthenticationManager authenticationManager;

    @Override
    public CommonResponse register(RegisterRequest registerRequest) {
        // call user-service to create new user
        CommonResponse response = webClientBuilder.build()
                .post()
                .uri("http://user-service/api/users")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .block();

        if (response == null) {
            return CommonResponse.builder()
                    .message("User creation failed")
                    .status(500)
                    .build();
        }

        return response;
    }

    @Override
    public CommonResponse login(LoginRequest loginRequest) {
        // call user-service to get user by email
        CommonResponse response = webClientBuilder.build()
                .get()
                .uri("http://user-service/api/users/" + loginRequest.getEmail())
                .retrieve()
                .bodyToMono(CommonResponse.class)
                .block();

        UserAuthInfo userAuthInfo = new ObjectMapper().convertValue(response.getData(), UserAuthInfo.class);

//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    loginRequest.getEmail(), passwordEncoder.encode(loginRequest.getPassword())
//            ));
//        } catch (Exception e) {
//            return CommonResponse.builder()
//                    .status(400)
//                    .message("Invalid credentials")
//                    .build();
//        }

        if (userAuthInfo == null || !passwordEncoder.matches(loginRequest.getPassword(), userAuthInfo.getPassword())) {
            response = CommonResponse.builder()
                    .status(400)
                    .message("Invalid credentials")
                    .build();
            return response;
        }

        String token = jwtService.generateToken(userAuthInfo);

        try {
            redisService.set(userAuthInfo.getId(), token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CommonResponse.builder()
                .status(200)
                .message("Login success")
                .data(token)
                .build();
    }
}
