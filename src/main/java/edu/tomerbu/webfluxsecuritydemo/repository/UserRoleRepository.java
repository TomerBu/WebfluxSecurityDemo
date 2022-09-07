package edu.tomerbu.webfluxsecuritydemo.repository;

import edu.tomerbu.webfluxsecuritydemo.entitiy.Role;
import edu.tomerbu.webfluxsecuritydemo.entitiy.User;
import edu.tomerbu.webfluxsecuritydemo.entitiy.UserRoles;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface UserRoleRepository extends R2dbcRepository<UserRoles, Long> {
    Flux<UserRoles> findByUserId(long id);
}