package org.vux.userservice.service;

import org.vux.userservice.request.RegisterRequest;
import org.vux.userservice.response.CommonResponse;

public interface UserService {
    CommonResponse createUser(RegisterRequest registerRequest);

    CommonResponse getUserByEmail(String email);
}
