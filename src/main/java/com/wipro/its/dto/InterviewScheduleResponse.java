package com.wipro.its.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScheduleResponse {

    private String interviewId;
    private String candidateId;
    private String subject;
    private String techId;
    private LocalDate interviewDate;
    private LocalTime interviewTime;
    private Float techRating;
    private String empHrId;
    private LocalDate empHrInterviewDate;
    private LocalTime empHrInterviewTime;
    private Float empHrRating;
    private String result;
    private Boolean shareResult;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
