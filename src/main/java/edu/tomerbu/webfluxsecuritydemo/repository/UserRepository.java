package edu.tomerbu.webfluxsecuritydemo.repository;

import edu.tomerbu.webfluxsecuritydemo.entitiy.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;


public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findByUserName(String username);
}