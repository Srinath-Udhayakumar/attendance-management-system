package com.srinath.attendance.service;

import com.srinath.attendance.dto.response.TeamSummaryResponse;
import com.srinath.attendance.dto.response.TodayStatusResponse;
import com.srinath.attendance.entity.Attendance;
import com.srinath.attendance.entity.AttendanceStatus;
import com.srinath.attendance.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceService {

    Attendance checkIn(UUID userId);

    Attendance checkOut(UUID userId);

    Optional<Attendance> getTodayAttendance(UUID userId);

    void autoMarkAbsent();

    Page<Attendance> getAttendanceHistory(UUID userId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<Attendance> getAllAttendanceByDate(LocalDate date, Pageable pageable);

    Page<Attendance> filterAttendance(UUID userId, LocalDate startDate, LocalDate endDate, AttendanceStatus status, Pageable pageable);

    Attendance approveLate(UUID attendanceId, UUID approvedBy);

    List<Attendance> getAttendanceByStatus(AttendanceStatus status, LocalDate date);

    Optional<User> findUserByEmail(String email);

    // New methods for manager endpoints
    TeamSummaryResponse getTeamSummary(LocalDate date);

    TodayStatusResponse getTodayStatus();
}
