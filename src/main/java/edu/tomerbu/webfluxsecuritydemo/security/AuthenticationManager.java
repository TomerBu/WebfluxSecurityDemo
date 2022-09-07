package edu.tomerbu.webfluxsecuritydemo.security;

import edu.tomerbu.webfluxsecuritydemo.error.UnAuthorizedException;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {

    public AuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return super.authenticate(authentication)
                .onErrorMap(e -> new UnAuthorizedException());
    }
}