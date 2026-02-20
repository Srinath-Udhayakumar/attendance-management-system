package com.srinath.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamSummaryResponse {
    private Long totalEmployees;
    private Long presentToday;
    private Long absentToday;
    private Long lateToday;
    private Long halfDayToday;
}
