package edu.tomerbu.webfluxsecuritydemo.controller;

import edu.tomerbu.webfluxsecuritydemo.dto.response.SignUpResponse;
import edu.tomerbu.webfluxsecuritydemo.dto.request.SignUpRequest;
import edu.tomerbu.webfluxsecuritydemo.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {
    private final UserDetailsServiceImpl userService;

    public AuthController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    //in rest controller we cant use The ServerResponse Type
    @PostMapping("signup")
    public Mono<ResponseEntity<SignUpResponse>> signup(@RequestBody SignUpRequest ar) {
        return
                userService.signup(ar)
                        .map(user->new SignUpResponse(user.getUsername()))
                        .map(ResponseEntity::ok);

    }
}