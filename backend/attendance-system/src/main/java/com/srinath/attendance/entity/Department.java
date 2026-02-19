package com.srinath.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "departments",
        uniqueConstraints = @UniqueConstraint(name = "uk_department_name", columnNames = "name"),
        indexes = {@Index(name = "idx_department_name", columnList = "name")}
)

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 100)
    private String name;
}
