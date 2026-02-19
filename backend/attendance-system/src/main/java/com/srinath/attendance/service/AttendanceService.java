package com.srinath.attendance.service;

import com.srinath.attendance.entity.Attendance;

import java.util.Optional;
import java.util.UUID;

public interface AttendanceService {

    Attendance checkIn(UUID userId);

    Attendance checkOut(UUID userId);

    Optional<Attendance> getTodayAttendance(UUID userId);

    void autoMarkAbsent();
}
