package org.vux.apigateway.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.vux.apigateway.service.JwtService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private String SECRET_KEY = "462D4A614E645267556B58703272357538782F413F4428472B4B625065536856";

    public String extractEmail(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String jwt, String comparedJwtInRedis) {
        final String email = extractEmail(jwt);
        final String role = extractRole(jwt);
        final String id = extractId(jwt);

        return email.equals(extractEmail(comparedJwtInRedis)) && role.equals(extractRole(comparedJwtInRedis)) && id.equals(extractId(comparedJwtInRedis)) && !isTokenExpired(jwt);
    }

    public String extractRole(String jwt) {
        return extractClaim(jwt, c -> c.get("role", String.class));
    }

    public String extractId(String jwt) {
        return extractClaim(jwt, c -> c.get("id", String.class));
    }

    private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }
}
