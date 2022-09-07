package edu.tomerbu.webfluxsecuritydemo.configuration;

import edu.tomerbu.webfluxsecuritydemo.security.JWTFilter;
import edu.tomerbu.webfluxsecuritydemo.security.JWTUtil;
import edu.tomerbu.webfluxsecuritydemo.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurityConfig {
    private final JWTUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private static final String[] AUTH_WHITELIST = {
            "/login/**",
            "/logout/**",
            "/auth/**",
            "/favicon.ico",
    };

    public WebSecurityConfig(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .pathMatchers("/auth/signup").permitAll()
                .pathMatchers("/auth/login").permitAll()
                //hasRole strips the ROLE_ prefix: just ADMIN/USER
                .pathMatchers("/home/admin").hasRole("ADMIN")
                .pathMatchers("/home/ok").hasAnyRole("USER", "ADMIN")
                .anyExchange()
                .authenticated()
                .and()
                .httpBasic()

                .and().csrf().disable()
                .addFilterAt(
                        new JWTFilter(jwtUtil, userDetailsService)
                                .tokenAuthenticationFilter(),
                        SecurityWebFiltersOrder.AUTHENTICATION
                );
        return http.build();
    }
}