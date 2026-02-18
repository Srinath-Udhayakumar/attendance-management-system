package com.srinath.attendance.entity;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "roles",uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;  // EMPLOYEE, MANAGER
}
