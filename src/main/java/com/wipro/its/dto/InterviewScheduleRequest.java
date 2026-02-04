package com.wipro.its.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScheduleRequest {

    @NotBlank(message = "Candidate ID is required")
    private String candidateId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Tech panel ID is required")
    private String techId;

    @NotNull(message = "Interview date is required")
    private LocalDate interviewDate;

    @NotNull(message = "Interview time is required")
    private LocalTime interviewTime;

    @NotBlank(message = "HR panel ID is required")
    private String empHrId;

    @NotNull(message = "HR interview date is required")
    private LocalDate empHrInterviewDate;

    @NotNull(message = "HR interview time is required")
    private LocalTime empHrInterviewTime;
}
