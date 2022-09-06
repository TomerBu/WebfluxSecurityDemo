package edu.tomerbu.webfluxsecuritydemo.service;

import edu.tomerbu.webfluxsecuritydemo.dto.request.SignUpRequest;
import edu.tomerbu.webfluxsecuritydemo.entitiy.User;
import edu.tomerbu.webfluxsecuritydemo.entitiy.UserRoles;
import edu.tomerbu.webfluxsecuritydemo.error.DuplicateUserNameException;
import edu.tomerbu.webfluxsecuritydemo.repository.RoleRepository;
import edu.tomerbu.webfluxsecuritydemo.repository.UserRepository;
import edu.tomerbu.webfluxsecuritydemo.repository.UserRoleRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepo;
    private final UserRoleRepository userRoleRepo;
    private final RoleRepository roleRepo;

    public UserDetailsServiceImpl(PasswordEncoder encoder,
                                  UserRepository userRepo, UserRoleRepository userRoleRepo,
                                  RoleRepository roleRepo) {
        this.encoder = encoder;
        this.userRepo = userRepo;
        this.userRoleRepo = userRoleRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepo.findByUserName(username)
                .switchIfEmpty(Mono.error(
                        new UsernameNotFoundException(username))
                )
                .flatMap(user -> Mono.zip(
                                Mono.just(user),
                                userRoleRepo.findByUserId(user.getId())
                        )
                )
                .flatMap(userAndUserRole -> Mono.zip(
                                Mono.just(userAndUserRole.getT1()),
                                roleRepo.findById(
                                        userAndUserRole.getT2().getRoleId()
                                )
                        )
                )
                .map(userAndRole -> userAndRole
                        .getT1().withRole(userAndRole.getT2()));
    }

    public Mono<User> signup(@RequestBody SignUpRequest ar) {
        return roleRepo.findByName("ROLE_USER")
                .flatMap(role -> Mono.zip(
                        Mono.just(User.builder().userName(ar.getUsername())
                                .displayName(ar.getDisplayName())
                                .password(encoder.encode(ar.getPassword()))
                                .role(role)
                                .build()),
                        Mono.just(role))
                )
                .flatMap(userAndRole -> Mono.zip(/*UserAndRole Pair*/
                                userRepo.save(userAndRole.getT1())
                                        .doOnError(System.err::println)
                                        .onErrorMap(e -> new DuplicateUserNameException()),
                                Mono.just(userAndRole.getT2())
                        )
                )
                .flatMap(userAndRole ->
                        Mono.zip(
                                Mono.just(userAndRole.getT1().withRole(userAndRole.getT2())),
                                userRoleRepo.save(UserRoles.builder()
                                        .roleId(userAndRole.getT1().getId())
                                        .userId(userAndRole.getT2().getId())
                                        .build())
                        )
                )
                .flatMap(userAndUserRole ->
                        Mono.just(userAndUserRole.getT1())
                );
    }

}