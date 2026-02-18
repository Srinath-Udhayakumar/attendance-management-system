package com.srinath.attendance.service;

import com.srinath.attendance.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface AttendanceService {
    Attendance checkIn(UUID userId);
    Attendance checkOut(UUID userId);
    Attendance getTodayAttendance(UUID userId);
    Page<Attendance> getAttendanceByIdAndDateBetween(UUID userId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    void autoMarkAbsent();
    void autoCheckOut();
}
