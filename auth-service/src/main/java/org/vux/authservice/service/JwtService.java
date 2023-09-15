package org.vux.authservice.service;

import org.vux.authservice.dto.UserAuthInfo;

public interface JwtService {

    String generateToken(UserAuthInfo userAuthInfo);
}
