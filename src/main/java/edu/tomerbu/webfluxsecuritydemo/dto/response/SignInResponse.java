package edu.tomerbu.webfluxsecuritydemo.dto.response;

import lombok.*;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResponse {
    @Getter
    private String token;
}