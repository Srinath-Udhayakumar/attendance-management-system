package com.srinath.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

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
}
