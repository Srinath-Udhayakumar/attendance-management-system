package com.srinath.attendance.dto.summary;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlySummaryDTO {
    private int presentDays;
    private int absentDays;
    private int lateDays;
    private int halfDays;
    private double totalHours;
}
