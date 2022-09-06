package edu.tomerbu.webfluxsecuritydemo.entitiy;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table
public class UserRoles {
    //R2DBC has No support for composite key
    private long userId;
    private long roleId;
    @Id
    private long id;
}
