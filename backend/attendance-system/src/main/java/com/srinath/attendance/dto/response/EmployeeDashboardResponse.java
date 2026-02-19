package com.srinath.attendance.dto.response;

import com.srinath.attendance.entity.AttendanceStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class EmployeeDashboardResponse {
    private String todayStatus;
    private int presentCount;
    private int absentCount;
    private int lateCount;
    private int halfDayCount;
    private double totalHoursThisMonth;
    private List<DailyAttendanceDTO> last7DaysAttendance;

    @Getter
    @Builder
    public static class DailyAttendanceDTO {
        private LocalDate date;
        private AttendanceStatus status;
        private double hoursWorked;
    }
}

