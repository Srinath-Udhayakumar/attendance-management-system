package com.srinath.attendance.controller;

import com.srinath.attendance.dto.response.AttendanceResponse;
import com.srinath.attendance.dto.response.EmployeeDashboardResponse;
import com.srinath.attendance.dto.response.MonthlyAttendanceSummaryDTO;
import com.srinath.attendance.entity.Attendance;
import com.srinath.attendance.entity.User;
import com.srinath.attendance.security.CustomUserDetails;
import com.srinath.attendance.service.AttendanceService;
import com.srinath.attendance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final AttendanceService attendanceService;
    private final DashboardService dashboardService;

    // Helper method to get current user ID
    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        } else if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            // Handle case where principal is Spring User (from JWT auth)
            String email = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
            // Find user by email to get UUID
            return attendanceService.findUserByEmail(email)
                    .map(User::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("User not authenticated");
    }

    // 🔹 Check-in
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> checkIn(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID userId = getCurrentUserId();
        log.info("Check-in request from user: {}", userId);

        Attendance attendance = attendanceService.checkIn(userId);

        AttendanceResponse response = AttendanceResponse.builder()
                .id(attendance.getId())
                .date(attendance.getDate())
                .checkInTime(attendance.getCheckInTime())
                .status(attendance.getStatus())
                .totalHours(attendance.getTotalHours())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 🔹 Check-out
    @PostMapping("/check-out")
    public ResponseEntity<AttendanceResponse> checkOut(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID userId = getCurrentUserId();
        log.info("Check-out request from user: {}", userId);

        Attendance attendance = attendanceService.checkOut(userId);

        AttendanceResponse response = AttendanceResponse.builder()
                .id(attendance.getId())
                .date(attendance.getDate())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .status(attendance.getStatus())
                .totalHours(attendance.getTotalHours())
                .build();

        return ResponseEntity.ok(response);
    }

    // 🔹 Today's attendance status
    @GetMapping("/attendance/today")
    public ResponseEntity<AttendanceResponse> getTodayAttendance(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID userId = getCurrentUserId();
        log.info("Today's attendance request from user: {}", userId);

        Optional<Attendance> attendance = attendanceService.getTodayAttendance(userId);

        if (attendance.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Attendance att = attendance.get();
        AttendanceResponse response = AttendanceResponse.builder()
                .id(att.getId())
                .date(att.getDate())
                .checkInTime(att.getCheckInTime())
                .checkOutTime(att.getCheckOutTime())
                .status(att.getStatus())
                .totalHours(att.getTotalHours())
                .lateApproved(att.isLateApproved())
                .build();

        return ResponseEntity.ok(response);
    }

    // 🔹 Attendance history with pagination
    @GetMapping("/attendance/history")
    public ResponseEntity<Page<AttendanceResponse>> getAttendanceHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Pageable pageable
    ) {
        UUID userId = getCurrentUserId();
        log.info("Attendance history request from user: {} for range {}-{}", userId, from, to);

        LocalDate startDate = from != null ? from : LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = to != null ? to : LocalDate.now();

        Page<Attendance> attendances = attendanceService.getAttendanceHistory(
                userId,
                startDate,
                endDate,
                pageable
        );

        Page<AttendanceResponse> response = attendances.map(att -> AttendanceResponse.builder()
                .id(att.getId())
                .date(att.getDate())
                .checkInTime(att.getCheckInTime())
                .checkOutTime(att.getCheckOutTime())
                .status(att.getStatus())
                .totalHours(att.getTotalHours())
                .lateApproved(att.isLateApproved())
                .build());

        return ResponseEntity.ok(response);
    }

    // 🔹 Monthly attendance summary
    @GetMapping("/attendance/monthly/{month}/{year}")
    public ResponseEntity<MonthlyAttendanceSummaryDTO> getMonthlyAttendanceSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable int month,
            @PathVariable int year
    ) {
        UUID userId = getCurrentUserId();
        log.info("Monthly summary request from user: {} for {}/{}", userId, month, year);

        YearMonth yearMonth = YearMonth.of(year, month);
        MonthlyAttendanceSummaryDTO summary = dashboardService.getMonthlyAttendanceSummary(
                userId,
                yearMonth
        );

        return ResponseEntity.ok(summary);
    }

    // 🔹 Employee dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<EmployeeDashboardResponse> getDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID userId = getCurrentUserId();
        log.info("Dashboard request from user: {}", userId);

        EmployeeDashboardResponse dashboard = dashboardService.getEmployeeDashboard(userId);

        return ResponseEntity.ok(dashboard);
    }
}
