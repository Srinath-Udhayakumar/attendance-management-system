package com.srinath.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
        indexes = {@Index(name = "idx_user_email", columnList = "email")}
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    // Store BCrypt hash only
    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private boolean enabled = true;

    // 🔐 Spring Security readiness
//    @Column(nullable = false)
//    private boolean accountNonLocked = true;
//
//    @Column(nullable = false)
//    private boolean accountNonExpired = true;
//
//    @Column(nullable = false)
//    private boolean credentialsNonExpired = true;
}
