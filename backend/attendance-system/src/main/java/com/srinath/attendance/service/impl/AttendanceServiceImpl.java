package com.srinath.attendance.service.impl;

import com.srinath.attendance.entity.Attendance;
import com.srinath.attendance.entity.AttendanceStatus;
import com.srinath.attendance.entity.User;
import com.srinath.attendance.exception.InvalidAttendanceStateException;
import com.srinath.attendance.exception.UserNotFoundException;
import com.srinath.attendance.repository.AttendanceRepository;
import com.srinath.attendance.repository.UserRepository;
import com.srinath.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    // Configuration constants (should be moved to properties in production)
    private static final LocalTime OFFICE_START_TIME = LocalTime.of(9, 0);
    private static final LocalTime OFFICE_END_TIME = LocalTime.of(17, 0);
    private static final LocalTime LATE_THRESHOLD = LocalTime.of(9, 30);
    private static final double BREAK_DURATION_MINUTES = 30.0;

    @Override
    public Attendance checkIn(UUID userId) {
        log.info("Check-in attempt for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for check-in: {}", userId);
                    return new UserNotFoundException("User not found");
                });

        LocalDate today = LocalDate.now();

        // Check if already checked in today
        if (attendanceRepository.existsByUserIdAndDate(userId, today)) {
            log.warn("User already checked in today: {}", userId);
            throw new InvalidAttendanceStateException("Already checked in today");
        }

        LocalDateTime checkInTime = LocalDateTime.now();
        AttendanceStatus status = determineStatus(checkInTime.toLocalTime());

        Attendance attendance = Attendance.builder()
                .user(user)
                .date(today)
                .checkInTime(checkInTime)
                .status(status)
                .totalHours(0.0)
                .build();

        Attendance saved = attendanceRepository.save(attendance);
        log.info("User {} checked in at {} with status {}", userId, checkInTime, status);

        return saved;
    }

    @Override
    public Attendance checkOut(UUID userId) {
        log.info("Check-out attempt for user: {}", userId);

        Attendance attendance = attendanceRepository
                .findByUserIdAndDate(userId, LocalDate.now())
                .orElseThrow(() -> {
                    log.error("No check-in found for check-out: {}", userId);
                    return new InvalidAttendanceStateException("No check-in found for today");
                });

        if (attendance.getCheckOutTime() != null) {
            log.warn("User already checked out today: {}", userId);
            throw new InvalidAttendanceStateException("Already checked out today");
        }

        LocalDateTime checkOutTime = LocalDateTime.now();
        attendance.setCheckOutTime(checkOutTime);

        // Calculate total hours worked
        double totalHours = calculateTotalHours(attendance.getCheckInTime(), checkOutTime);
        attendance.setTotalHours(totalHours);

        // Update status if not already marked late/half-day
        if (attendance.getStatus() == AttendanceStatus.PRESENT) {
            attendance.setStatus(AttendanceStatus.PRESENT);
        }

        Attendance saved = attendanceRepository.save(attendance);
        log.info("User {} checked out at {} with {} hours worked", userId, checkOutTime, totalHours);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Attendance> getTodayAttendance(UUID userId) {
        return attendanceRepository.findByUserIdAndDate(userId, LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attendance> getAttendanceHistory(UUID userId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.info("Fetching attendance history for user {} from {} to {}", userId, startDate, endDate);
        return attendanceRepository.findByUserIdAndDateBetween(userId, startDate, endDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attendance> getAllAttendanceByDate(LocalDate date, Pageable pageable) {
        log.info("Fetching all attendances for date: {}", date);
        return attendanceRepository.findByDate(date, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Attendance> filterAttendance(UUID userId, LocalDate startDate, LocalDate endDate, AttendanceStatus status, Pageable pageable) {
        log.info("Filtering attendance for user {} between {} and {} with status {}", userId, startDate, endDate, status);
        return attendanceRepository.findByUserIdAndDateBetweenAndStatus(userId, startDate, endDate, status, pageable);
    }

    @Override
    public Attendance approveLate(UUID attendanceId, UUID approvedBy) {
        log.info("Approving late attendance {} by user {}", attendanceId, approvedBy);

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new InvalidAttendanceStateException("Attendance record not found"));

        User approver = userRepository.findById(approvedBy)
                .orElseThrow(() -> new UserNotFoundException("Approver user not found"));

        attendance.setLateApproved(true);
        attendance.setApprovedBy(approver);
        attendance.setApprovedAt(LocalDateTime.now());

        Attendance saved = attendanceRepository.save(attendance);
        log.info("Late approval granted for attendance {}", attendanceId);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceByStatus(AttendanceStatus status, LocalDate date) {
        log.info("Fetching attendances with status {} for date {}", status, date);
        return attendanceRepository.findByStatusAndDate(status, date);
    }

    // 📅 Scheduled job to mark absent employees
    @Scheduled(cron = "0 0 18 * * MON-FRI") // 6 PM on weekdays
    @Override
    public void autoMarkAbsent() {
        log.info("Running auto-mark absent job");

        LocalDate today = LocalDate.now();
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            boolean hasCheckIn = attendanceRepository.existsByUserIdAndDate(user.getId(), today);

            if (!hasCheckIn) {
                Attendance absentRecord = Attendance.builder()
                        .user(user)
                        .date(today)
                        .status(AttendanceStatus.ABSENT)
                        .totalHours(0.0)
                        .build();

                attendanceRepository.save(absentRecord);
                log.info("Marked user {} as absent for {}", user.getId(), today);
            }
        }

        log.info("Auto-mark absent job completed");
    }

    // Helper methods
    private AttendanceStatus determineStatus(LocalTime checkInTime) {
        if (checkInTime.isBefore(LATE_THRESHOLD)) {
            return AttendanceStatus.PRESENT;
        } else if (checkInTime.isBefore(OFFICE_END_TIME)) {
            return AttendanceStatus.LATE;
        } else {
            return AttendanceStatus.HALF_DAY;
        }
    }

    private double calculateTotalHours(LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        long minutesWorked = ChronoUnit.MINUTES.between(checkInTime, checkOutTime);
        double hoursWorked = (minutesWorked - BREAK_DURATION_MINUTES) / 60.0;
        return Math.max(hoursWorked, 0.0); // Ensure non-negative
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

