package com.srinath.attendance.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ManagerDashboardResponse {
    private int totalEmployees;
    private int presentToday;
    private int absentToday;
    private int lateToday;
    private List<WeeklyTrendDTO> weeklyTrend;
    private List<DepartmentStatDTO> departmentStats;
    private List<AbsentEmployeeDTO> absentEmployeesToday;

    @Getter
    @Builder
    public static class WeeklyTrendDTO {
        private String date;
        private int present;
        private int absent;
        private int late;
    }

    @Getter
    @Builder
    public static class DepartmentStatDTO {
        private String departmentName;
        private int totalEmployees;
        private int presentToday;
        private int absentToday;
        private int lateToday;
        private double averageHoursWorked;
    }

    @Getter
    @Builder
    public static class AbsentEmployeeDTO {
        private String name;
        private String employeeId;
        private String departmentName;
        private String email;
    }
}

