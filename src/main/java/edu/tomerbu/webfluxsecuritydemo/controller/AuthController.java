package edu.tomerbu.webfluxsecuritydemo.controller;

import edu.tomerbu.webfluxsecuritydemo.dto.request.SignInRequest;
import edu.tomerbu.webfluxsecuritydemo.dto.response.SignInResponse;
import edu.tomerbu.webfluxsecuritydemo.dto.response.SignUpResponse;
import edu.tomerbu.webfluxsecuritydemo.dto.request.SignUpRequest;
import edu.tomerbu.webfluxsecuritydemo.entitiy.User;
import edu.tomerbu.webfluxsecuritydemo.error.UnAuthorizedException;
import edu.tomerbu.webfluxsecuritydemo.security.JWTUtil;
import edu.tomerbu.webfluxsecuritydemo.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {
    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public AuthController(UserDetailsServiceImpl userService, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    //in rest controller we cant use The ServerResponse Type
    @PostMapping("signup")
    public Mono<ResponseEntity<SignUpResponse>> signup(@RequestBody SignUpRequest sr) {
        return
                userService.signup(sr)
                        .map(user -> new SignUpResponse(user.getUsername()))
                        .map(ResponseEntity::ok);

    }

    @PostMapping("/login")
    public Mono<ResponseEntity<SignInResponse>> login(@RequestBody SignInRequest sr) {
        return userService.findByUsername(sr.getUsername())
                .filter(
                        userDetails-> passwordEncoder.matches(sr.getPassword(), userDetails.getPassword())
                )
                .cast(User.class)
                .map(userDetails -> ResponseEntity.ok(
                        new SignInResponse(jwtUtil.generateToken(userDetails)))
                )
                .switchIfEmpty(Mono.error(new UnAuthorizedException("No JWT 4 U...")));
    }
}