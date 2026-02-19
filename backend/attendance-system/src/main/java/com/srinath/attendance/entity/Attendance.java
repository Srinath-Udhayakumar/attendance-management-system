package com.srinath.attendance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(
        name = "attendances",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_date", columnNames = {"user_id","date"}),
        indexes = {
                @Index(name = "idx_user_date", columnList = "user_id,date"),
                @Index(name = "idx_date", columnList = "date")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @Column(nullable = false)
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;
    @Column(nullable = false)
    private Double totalHours;
    private boolean lateApproved;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by",nullable = true)
    private User approvedBy;
    private LocalDateTime approvedAt;
}