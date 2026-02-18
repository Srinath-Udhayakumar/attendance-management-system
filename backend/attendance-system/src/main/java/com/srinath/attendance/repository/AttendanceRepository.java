package com.srinath.attendance.repository;

import com.srinath.attendance.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Optional<Attendance> findByUserIdAndDate(UUID userId, LocalDate date);
    List<Attendance> findByUserId(UUID userId);
    Page<Attendance> findByDate(LocalDate date, Pageable pageable);
    Page<Attendance> findByUserIdAndDateBetween(UUID userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
