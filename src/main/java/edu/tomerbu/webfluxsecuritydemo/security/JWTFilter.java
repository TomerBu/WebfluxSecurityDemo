package edu.tomerbu.webfluxsecuritydemo.security;

import edu.tomerbu.webfluxsecuritydemo.service.UserDetailsServiceImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import reactor.core.publisher.Mono;


public class JWTFilter {
    private final JWTUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JWTFilter(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public AuthenticationWebFilter tokenAuthenticationFilter() {

        AuthenticationWebFilter filter =
                new AuthenticationWebFilter(tokenAuthenticationManager());

        filter.setServerAuthenticationConverter(tokenAuthenticationConverter());

        filter.setAuthenticationFailureHandler(
                (exchange, exception) -> Mono.error(exception)
        );

        return filter;
    }

    public ServerAuthenticationConverter tokenAuthenticationConverter() {
        return serverWebExchange -> {

            String authorization = serverWebExchange.getRequest()
                    .getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authorization == null || !authorization.startsWith("Bearer "))
                return Mono.empty();

            return Mono.just(new JwtAuthenticationToken(authorization.substring(7)));
        };
    }

    private ReactiveAuthenticationManager tokenAuthenticationManager() {

        return authentication -> {

            String token = (String) authentication.getCredentials();
            String username = jwtUtil.getUsernameFromToken(token);

            return userDetailsService.findByUsername(username)
                    .map(user -> new JwtAuthenticationToken(user, token, user.getAuthorities()));
        };
    }
}