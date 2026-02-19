package com.srinath.attendance.service.impl;

import com.srinath.attendance.entity.Attendance;
import com.srinath.attendance.entity.AttendanceStatus;
import com.srinath.attendance.entity.User;
import com.srinath.attendance.repository.AttendanceRepository;
import com.srinath.attendance.repository.UserRepository;
import com.srinath.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Override
    public Attendance checkIn(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();

        if (attendanceRepository.existsByUserIdAndDate(userId, today)) {
            throw new RuntimeException("Already checked in today");
        }

        Attendance attendance = Attendance.builder()
                .user(user)
                .date(today)
                .checkInTime(LocalDateTime.now())
                .status(AttendanceStatus.PRESENT)
                .totalHours(0.0)
                .build();

        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance checkOut(UUID userId) {

        Attendance attendance = attendanceRepository
                .findByUserIdAndDate(userId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No check-in found"));

        attendance.setCheckOutTime(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Attendance> getTodayAttendance(UUID userId) {
        return attendanceRepository.findByUserIdAndDate(userId, LocalDate.now());
    }

    @Override
    public void autoMarkAbsent() {
        // scheduled job later
    }
}

