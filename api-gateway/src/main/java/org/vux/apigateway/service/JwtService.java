package org.vux.apigateway.service;

public interface JwtService {
    String extractId(String jwt);

    String extractEmail(String jwt);

    boolean validateToken(String jwt, String comparedJwtInRedis);

    String extractRole(String jwt);
}
