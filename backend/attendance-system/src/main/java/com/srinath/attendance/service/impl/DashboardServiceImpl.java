package com.srinath.attendance.service.impl;

import com.srinath.attendance.dto.response.EmployeeDashboardResponse;
import com.srinath.attendance.dto.response.ManagerDashboardResponse;
import com.srinath.attendance.dto.response.MonthlyAttendanceSummaryDTO;
import com.srinath.attendance.entity.Attendance;
import com.srinath.attendance.entity.AttendanceStatus;
import com.srinath.attendance.entity.User;
import com.srinath.attendance.exception.UserNotFoundException;
import com.srinath.attendance.repository.AttendanceRepository;
import com.srinath.attendance.repository.UserRepository;
import com.srinath.attendance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Override
    public EmployeeDashboardResponse getEmployeeDashboard(UUID userId) {
        log.info("Fetching employee dashboard for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);

        // Get today's attendance
        Optional<Attendance> todayAttendance = attendanceRepository.findByUserIdAndDate(userId, today);
        String todayStatus = todayAttendance
                .map(a -> a.getStatus().name())
                .orElse("NO_CHECK_IN");

        // Get monthly stats
        List<Attendance> monthlyAttendances = attendanceRepository
                .findByUserIdAndDateBetween(userId, monthStart, today, org.springframework.data.domain.Pageable.unpaged())
                .getContent();

        int presentCount = (int) monthlyAttendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        int absentCount = (int) monthlyAttendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        int lateCount = (int) monthlyAttendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.LATE).count();
        int halfDayCount = (int) monthlyAttendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.HALF_DAY).count();

        double totalHours = monthlyAttendances.stream()
                .mapToDouble(Attendance::getTotalHours)
                .sum();

        // Get last 7 days
        LocalDate sevenDaysAgo = today.minusDays(7);
        List<EmployeeDashboardResponse.DailyAttendanceDTO> last7Days = attendanceRepository
                .findByUserIdAndDateBetween(userId, sevenDaysAgo, today, org.springframework.data.domain.Pageable.unpaged())
                .getContent()
                .stream()
                .map(a -> EmployeeDashboardResponse.DailyAttendanceDTO.builder()
                        .date(a.getDate())
                        .status(a.getStatus())
                        .hoursWorked(a.getTotalHours())
                        .build())
                .sorted(Comparator.comparing(EmployeeDashboardResponse.DailyAttendanceDTO::getDate))
                .collect(Collectors.toList());

        return EmployeeDashboardResponse.builder()
                .todayStatus(todayStatus)
                .presentCount(presentCount)
                .absentCount(absentCount)
                .lateCount(lateCount)
                .halfDayCount(halfDayCount)
                .totalHoursThisMonth(totalHours)
                .last7DaysAttendance(last7Days)
                .build();
    }

    @Override
    public ManagerDashboardResponse getManagerDashboard() {
        log.info("Fetching manager dashboard");

        LocalDate today = LocalDate.now();

        // Get all employees
        List<User> allEmployees = userRepository.findAllEmployees();
        int totalEmployees = allEmployees.size();

        // Get today's attendance
        List<Attendance> todayAttendances = attendanceRepository.findByDate(today, org.springframework.data.domain.Pageable.unpaged()).getContent();

        int presentToday = (int) todayAttendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        int absentToday = (int) todayAttendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        int lateToday = (int) todayAttendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.LATE).count();

        // Weekly trend (last 7 days)
        List<ManagerDashboardResponse.WeeklyTrendDTO> weeklyTrend = getWeeklyTrend();

        // Department stats
        List<ManagerDashboardResponse.DepartmentStatDTO> departmentStats = getDepartmentStats(todayAttendances);

        // Absent employees today
        List<ManagerDashboardResponse.AbsentEmployeeDTO> absentEmployees = getAbsentEmployeesToday(allEmployees, todayAttendances);

        return ManagerDashboardResponse.builder()
                .totalEmployees(totalEmployees)
                .presentToday(presentToday)
                .absentToday(absentToday)
                .lateToday(lateToday)
                .weeklyTrend(weeklyTrend)
                .departmentStats(departmentStats)
                .absentEmployeesToday(absentEmployees)
                .build();
    }

    @Override
    public MonthlyAttendanceSummaryDTO getMonthlyAttendanceSummary(UUID userId, YearMonth yearMonth) {
        log.info("Fetching monthly summary for user {} for month {}", userId, yearMonth);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Attendance> attendances = attendanceRepository
                .findByUserIdAndDateBetween(userId, startDate, endDate, org.springframework.data.domain.Pageable.unpaged())
                .getContent();

        int presentDays = (int) attendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count();
        int absentDays = (int) attendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count();
        int lateDays = (int) attendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.LATE).count();
        int halfDayCount = (int) attendances.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.HALF_DAY).count();

        double totalHours = attendances.stream()
                .mapToDouble(Attendance::getTotalHours)
                .sum();

        // Calculate working days (approx 22 per month)
        int workingDays = 22;
        double attendancePercentage = ((presentDays + (halfDayCount * 0.5)) / workingDays) * 100;

        return MonthlyAttendanceSummaryDTO.builder()
                .presentDays(presentDays)
                .absentDays(absentDays)
                .lateDays(lateDays)
                .halfDayCount(halfDayCount)
                .totalHoursWorked(totalHours)
                .startDate(startDate)
                .endDate(endDate)
                .workingDays(workingDays)
                .attendancePercentage(Math.min(attendancePercentage, 100.0))
                .build();
    }

    private List<ManagerDashboardResponse.WeeklyTrendDTO> getWeeklyTrend() {
        return (List) java.util.Collections.emptyList();
        // TODO: Implement weekly trend calculation
    }

    private List<ManagerDashboardResponse.DepartmentStatDTO> getDepartmentStats(List<Attendance> todayAttendances) {
        return (List) java.util.Collections.emptyList();
        // TODO: Implement department stats aggregation
    }

    private List<ManagerDashboardResponse.AbsentEmployeeDTO> getAbsentEmployeesToday(
            List<User> allEmployees, List<Attendance> todayAttendances) {
        return allEmployees.stream()
                .filter(emp -> todayAttendances.stream()
                        .noneMatch(a -> a.getUser().getId().equals(emp.getId())))
                .map(emp -> ManagerDashboardResponse.AbsentEmployeeDTO.builder()
                        .name(emp.getName())
                        .employeeId(emp.getEmployeeId())
                        .departmentName(emp.getDepartment().getName())
                        .email(emp.getEmail())
                        .build())
                .collect(Collectors.toList());
    }
}
