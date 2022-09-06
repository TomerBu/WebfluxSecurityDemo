package edu.tomerbu.webfluxsecuritydemo.repository;

import edu.tomerbu.webfluxsecuritydemo.entitiy.Role;
import edu.tomerbu.webfluxsecuritydemo.entitiy.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;


public interface RoleRepository extends R2dbcRepository<Role, Long> {
    Mono<Role> findByName(String name);
}