package com.srinath.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "roles",
        uniqueConstraints = @UniqueConstraint(name = "uk_role_name", columnNames = "name")
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoleType name;  // EMPLOYEE, MANAGER
}
