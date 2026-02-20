package com.srinath.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodayStatusResponse {
    private List<EmployeeInfo> presentEmployees;
    private List<EmployeeInfo> absentEmployees;
    private List<EmployeeInfo> lateEmployees;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeInfo {
        private UUID id;
        private String name;
        private String employeeId;
        private String department;
        private String checkInTime;
    }
}
