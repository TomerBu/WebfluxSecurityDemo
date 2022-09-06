package edu.tomerbu.webfluxsecuritydemo.entitiy;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Builder
public class Order {
    @Id
    @Column("id")
    private long id;
    @Column("amount")
    private int amount;
    @Column("orderDate")
    private LocalDateTime orderDate;
    @Column("updateDate")
    private LocalDateTime updateDate;
}