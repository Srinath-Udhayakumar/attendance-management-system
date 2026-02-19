package com.srinath.attendance.controller;

import com.srinath.attendance.dto.response.AttendanceResponse;
import com.srinath.attendance.dto.response.EmployeeDashboardResponse;
import com.srinath.attendance.dto.response.MonthlyAttendanceSummaryDTO;
import com.srinath.attendance.entity.Attendance;
import com.srinath.attendance.security.CustomUserDetails;
import com.srinath.attendance.service.AttendanceService;
import com.srinath.attendance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final AttendanceService attendanceService;
    private final DashboardService dashboardService;

    // 🔹 Check-in
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> checkIn(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("Check-in request from user: {}", userDetails.getUserId());

        Attendance attendance = attendanceService.checkIn(userDetails.getUserId());

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
        log.info("Check-out request from user: {}", userDetails.getUserId());

        Attendance attendance = attendanceService.checkOut(userDetails.getUserId());

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
        log.info("Today's attendance request from user: {}", userDetails.getUserId());

        Optional<Attendance> attendance = attendanceService.getTodayAttendance(userDetails.getUserId());

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
        log.info("Attendance history request from user: {} for range {}-{}", userDetails.getUserId(), from, to);

        LocalDate startDate = from != null ? from : LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = to != null ? to : LocalDate.now();

        Page<Attendance> attendances = attendanceService.getAttendanceHistory(
                userDetails.getUserId(),
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
        log.info("Monthly summary request from user: {} for {}/{}", userDetails.getUserId(), month, year);

        YearMonth yearMonth = YearMonth.of(year, month);
        MonthlyAttendanceSummaryDTO summary = dashboardService.getMonthlyAttendanceSummary(
                userDetails.getUserId(),
                yearMonth
        );

        return ResponseEntity.ok(summary);
    }

    // 🔹 Employee dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<EmployeeDashboardResponse> getDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.info("Dashboard request from user: {}", userDetails.getUserId());

        EmployeeDashboardResponse dashboard = dashboardService.getEmployeeDashboard(userDetails.getUserId());

        return ResponseEntity.ok(dashboard);
    }
}
