package edu.tomerbu.webfluxsecuritydemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SecurityController {

    @GetMapping("/")
    public ResponseEntity<String> index(Model model, Principal principal) {
        String username = principal.getName();
        log.info("Authenticated user is: {}", username);
        model.addAttribute("username", username);
        return ResponseEntity.ok("Hello, world");
    }


}