//package com.srinath.attendance.service.impl;
//
//import com.srinath.attendance.entity.Attendance;
//import com.srinath.attendance.entity.User;
//import com.srinath.attendance.repository.AttendanceRepository;
//import com.srinath.attendance.repository.UserRepository;
//import com.srinath.attendance.service.AttendanceService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class AttendanceServiceImpl implements AttendanceService {
//
//    private final AttendanceRepository attendanceRepository;
//    private final UserRepository userRepository;
//
//    @Override
//    public Attendance checkIn(UUID userId) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // check already checked in
//
//
//
//
//        return null;
//    }
//}
