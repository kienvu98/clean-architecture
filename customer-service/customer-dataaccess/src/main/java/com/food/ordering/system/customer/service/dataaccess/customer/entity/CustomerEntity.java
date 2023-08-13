package com.food.ordering.system.customer.service.dataaccess.customer.entity;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@EntityScan
public class CustomerEntity {

    @Id
    private UUID id;
    private String userName;
    private String firstName;
    private String lastName;
}
