package com.srinath.attendance.service;

import com.srinath.attendance.dto.response.EmployeeDashboardResponse;
import com.srinath.attendance.dto.response.ManagerDashboardResponse;
import com.srinath.attendance.dto.response.MonthlyAttendanceSummaryDTO;

import java.time.YearMonth;
import java.util.UUID;

public interface DashboardService {
    EmployeeDashboardResponse getEmployeeDashboard(UUID userId);
    ManagerDashboardResponse getManagerDashboard();
    MonthlyAttendanceSummaryDTO getMonthlyAttendanceSummary(UUID userId, YearMonth yearMonth);
}
