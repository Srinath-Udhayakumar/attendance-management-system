package com.srinath.attendance.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MonthlyAttendanceSummaryDTO {
    private int presentDays;
    private int absentDays;
    private int lateDays;
    private int halfDayCount;
    private double totalHoursWorked;
    private LocalDate startDate;
    private LocalDate endDate;
    private int workingDays;
    private double attendancePercentage;
}

