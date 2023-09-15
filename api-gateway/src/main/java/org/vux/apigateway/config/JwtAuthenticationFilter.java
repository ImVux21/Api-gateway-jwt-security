package org.vux.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.vux.apigateway.service.JwtService;
import org.vux.apigateway.service.RedisService;
import reactor.core.publisher.Mono;
import java.util.List;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtService jwtService;
    private final RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String jwt = extractTokenFromRequest(exchange);

        log.info("JWT: " + jwt);
        String path = exchange.getRequest().getPath().toString();
        log.info("Path: " + path);
        if (jwt != null) {
            String comparedJwtInRedis = redisService.get(jwtService.extractId(jwt));

            if (comparedJwtInRedis != null) {
                if (jwtService.validateToken(jwt, comparedJwtInRedis)) {
                    String email = jwtService.extractEmail(jwt);
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + jwtService.extractRole(jwt)));
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            email, null, authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(usernamePasswordAuthenticationToken));
                }
            }
        }

        return chain.filter(exchange);
    }

    private String extractTokenFromRequest(ServerWebExchange exchange) {
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
        log.info("Authorization header: " + authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
