package com.wipro.its.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "interview_schedules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class InterviewSchedule {

    @Id
    @Column(name = "interview_id", length = 50)
    private String interviewId;

    @Column(name = "candidate_id", length = 50, nullable = false)
    private String candidateId;

    @Column(name = "subject", length = 100)
    private String subject;

    @Column(name = "tech_id", length = 50)
    private String techId;

    @Column(name = "interview_date")
    private LocalDate interviewDate;

    @Column(name = "interview_time")
    private LocalTime interviewTime;

    @Column(name = "tech_rating")
    private Float techRating;

    @Column(name = "emp_hr_id", length = 50)
    private String empHrId;

    @Column(name = "emp_hr_interview_date")
    private LocalDate empHrInterviewDate;

    @Column(name = "emp_hr_interview_time")
    private LocalTime empHrInterviewTime;

    @Column(name = "emp_hr_rating")
    private Float empHrRating;

    @Column(name = "result", length = 50)
    private String result;

    @Column(name = "share_result")
    private Boolean shareResult = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
