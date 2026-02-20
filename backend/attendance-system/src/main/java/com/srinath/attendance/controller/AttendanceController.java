package com.srinath.attendance.controller;

import com.srinath.attendance.dto.response.TeamSummaryResponse;
import com.srinath.attendance.dto.response.TodayStatusResponse;
import com.srinath.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Slf4j
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 🔹 Mark attendance (dummy for now)
    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance() {
        return ResponseEntity.ok("Attendance marked successfully");
    }

    // 🔹 Get attendance (dummy)
    @GetMapping("/my")
    public ResponseEntity<String> getMyAttendance() {
        return ResponseEntity.ok("Attendance list fetched");
    }

    // 👨‍💼 TEAM SUMMARY - Manager only
    @GetMapping("/summary")
    public ResponseEntity<TeamSummaryResponse> getTeamSummary(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        log.info("Manager requesting team summary for date: {}", targetDate);

        TeamSummaryResponse summary = attendanceService.getTeamSummary(targetDate);
        return ResponseEntity.ok(summary);
    }

    // 👨‍💼 TODAY STATUS - Manager only
    @GetMapping("/today-status")
    public ResponseEntity<TodayStatusResponse> getTodayStatus() {
        log.info("Manager requesting today's attendance status");

        TodayStatusResponse status = attendanceService.getTodayStatus();
        return ResponseEntity.ok(status);
    }
}
