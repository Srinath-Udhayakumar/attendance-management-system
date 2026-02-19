package com.srinath.attendance.dto.response;

import com.srinath.attendance.entity.AttendanceStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AttendanceResponse {

    private UUID id;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private AttendanceStatus status;
    private double totalHours;
    private boolean lateApproved;
}
