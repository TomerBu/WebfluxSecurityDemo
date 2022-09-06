package edu.tomerbu.webfluxsecuritydemo.dto.response;

import lombok.*;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponse {
    @Getter
    private String username;
}