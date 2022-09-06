package edu.tomerbu.webfluxsecuritydemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("home")
public class HomeController {

    @GetMapping("ok")
    public Mono<String> ok() {
        return Mono.just("Hello, You are logged in...");
    }

    @GetMapping("admin")
    public Mono<String> admin() {
        return Mono.just("Hello, You are logged in as Admin");
    }
}