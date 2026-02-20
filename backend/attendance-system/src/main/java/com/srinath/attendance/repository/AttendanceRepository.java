package com.srinath.attendance.repository;

import com.srinath.attendance.entity.Attendance;
import com.srinath.attendance.entity.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Optional<Attendance> findByUserIdAndDate(UUID userId, LocalDate date);
    List<Attendance> findByUserId(UUID userId);
    Page<Attendance> findByDate(LocalDate date, Pageable pageable);
    Page<Attendance> findByUserIdAndDateBetween(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
    long countByUserIdAndDateBetween(UUID userId, LocalDate start, LocalDate end);
    boolean existsByUserIdAndDate(UUID userId, LocalDate date);

    // Additional methods for filtering and reporting
    @Query("SELECT a FROM Attendance a WHERE a.status = :status AND a.date = :date")
    List<Attendance> findByStatusAndDate(@Param("status") AttendanceStatus status, @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.user.id = :userId AND a.status = :status")
    List<Attendance> findByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") AttendanceStatus status);

    @Query("SELECT a FROM Attendance a WHERE a.user.id = :userId AND a.date BETWEEN :startDate AND :endDate AND a.status = :status")
    Page<Attendance> findByUserIdAndDateBetweenAndStatus(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") AttendanceStatus status,
            Pageable pageable
    );

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.user.id = :userId AND a.date BETWEEN :startDate AND :endDate AND a.status = :status")
    long countByUserIdAndDateBetweenAndStatus(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") AttendanceStatus status
    );

    @Query("SELECT a FROM Attendance a WHERE a.date = :date AND a.status = :status")
    Page<Attendance> findByDateAndStatus(@Param("date") LocalDate date, @Param("status") AttendanceStatus status, Pageable pageable);

    @Query("SELECT a FROM Attendance a WHERE a.user.department.id = :departmentId AND a.date BETWEEN :startDate AND :endDate")
    List<Attendance> findByDepartmentAndDateBetween(
            @Param("departmentId") UUID departmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Count methods for summary statistics
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.status = :status AND a.date = :date")
    long countByStatusAndDate(@Param("status") AttendanceStatus status, @Param("date") LocalDate date);
}
