package com.srinath.attendance.controller;

import com.srinath.attendance.dto.response.AttendanceResponse;
import com.srinath.attendance.dto.response.ManagerDashboardResponse;
import com.srinath.attendance.entity.Attendance;
import com.srinath.attendance.entity.AttendanceStatus;
import com.srinath.attendance.entity.User;
import com.srinath.attendance.security.CustomUserDetails;
import com.srinath.attendance.service.AttendanceService;
import com.srinath.attendance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
@Slf4j
public class ManagerController {

    private final AttendanceService attendanceService;
    private final DashboardService dashboardService;

    // 🔹 Get all attendances for a specific date
    @GetMapping("/attendance")
    public ResponseEntity<Page<AttendanceResponse>> getAllAttendances(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) AttendanceStatus status,
            Pageable pageable
    ) {
        log.info("Manager requesting all attendances for date: {} with status: {}", date, status);

        LocalDate queryDate = date != null ? date : LocalDate.now();

        Page<Attendance> attendances = attendanceService.getAllAttendanceByDate(queryDate, pageable);

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

    // 🔹 Get attendance history for a specific employee
    @GetMapping("/attendance/{userId}")
    public ResponseEntity<Page<AttendanceResponse>> getEmployeeAttendance(
            @PathVariable UUID userId,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Pageable pageable
    ) {
        log.info("Manager requesting attendance for employee: {} from {} to {}", userId, from, to);

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

    // 🔹 Approve late arrival
    @PostMapping("/attendance/{attendanceId}/approve-late")
    public ResponseEntity<AttendanceResponse> approveLate(
            @PathVariable UUID attendanceId,
            @AuthenticationPrincipal CustomUserDetails managerDetails
    ) {
        log.info("Manager {} approving late for attendance: {}", managerDetails.getUserId(), attendanceId);

        Attendance attendance = attendanceService.approveLate(attendanceId, managerDetails.getUserId());

        AttendanceResponse response = AttendanceResponse.builder()
                .id(attendance.getId())
                .date(attendance.getDate())
                .checkInTime(attendance.getCheckInTime())
                .checkOutTime(attendance.getCheckOutTime())
                .status(attendance.getStatus())
                .totalHours(attendance.getTotalHours())
                .lateApproved(attendance.isLateApproved())
                .build();

        return ResponseEntity.ok(response);
    }

    // 🔹 Manager dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<ManagerDashboardResponse> getDashboard() {
        log.info("Manager requesting dashboard");

        ManagerDashboardResponse dashboard = dashboardService.getManagerDashboard();

        return ResponseEntity.ok(dashboard);
    }

    // 🔹 CSV export with streaming
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportAttendanceCSV(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        log.info("Exporting attendance CSV from {} to {}", from, to);

        LocalDate startDate = from != null ? from : LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = to != null ? to : LocalDate.now();

        String csvContent = generateCSVContent(startDate, endDate);
        byte[] csvBytes = csvContent.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "attendance_" + startDate + "_to_" + endDate + ".csv");
        headers.setContentLength(csvBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }

    // Helper method to generate CSV content
    private String generateCSVContent(LocalDate startDate, LocalDate endDate) {
        StringBuilder csv = new StringBuilder();
        csv.append("Employee ID,Employee Name,Department,Date,Status,Check In,Check Out,Total Hours,Late Approved\n");

        Page<Attendance> attendances = attendanceService.getAttendanceHistory(
                null, // All users
                startDate,
                endDate,
                org.springframework.data.domain.Pageable.unpaged()
        );

        for (Attendance attendance : attendances.getContent()) {
            User user = attendance.getUser();
            String employeeId = user.getEmployeeId() != null ? user.getEmployeeId() : "";
            String employeeName = user.getName() != null ? user.getName() : "";
            String department = user.getDepartment() != null ? user.getDepartment().getName() : "";
            String date = attendance.getDate() != null ? attendance.getDate().toString() : "";
            String status = attendance.getStatus() != null ? attendance.getStatus().toString() : "";
            String checkIn = attendance.getCheckInTime() != null ? attendance.getCheckInTime().toString() : "";
            String checkOut = attendance.getCheckOutTime() != null ? attendance.getCheckOutTime().toString() : "";
            String totalHours = attendance.getTotalHours() != null ? attendance.getTotalHours().toString() : "0.0";
            String lateApproved = attendance.isLateApproved() ? "Yes" : "No";

            csv.append(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    employeeId, employeeName, department, date, status, checkIn, checkOut, totalHours, lateApproved));
        }

        return csv.toString();
    }
}

