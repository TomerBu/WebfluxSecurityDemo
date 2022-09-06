package edu.tomerbu.webfluxsecuritydemo.entitiy;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    private long id;

    private String name;
}