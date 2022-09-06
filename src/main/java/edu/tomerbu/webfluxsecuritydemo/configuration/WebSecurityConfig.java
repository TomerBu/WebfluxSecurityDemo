package edu.tomerbu.webfluxsecuritydemo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/auth/signup").permitAll()
                .pathMatchers("/auth/login").permitAll()
                //hasRole strips the ROLE_ prefix: just ADMIN/USER
                .pathMatchers("/home/admin").hasRole("ADMIN")
                .pathMatchers("/home/ok").hasAnyRole("USER", "ADMIN")
                .anyExchange()
                .authenticated()
                .and()
                .httpBasic()
                .and().csrf().disable();
        return http.build();
    }

}