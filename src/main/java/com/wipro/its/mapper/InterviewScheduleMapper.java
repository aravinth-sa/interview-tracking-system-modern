package com.wipro.its.mapper;

import com.wipro.its.dto.InterviewScheduleResponse;
import com.wipro.its.entity.InterviewSchedule;
import org.springframework.stereotype.Component;

@Component
public class InterviewScheduleMapper {

    public InterviewScheduleResponse toResponse(InterviewSchedule s) {
        return InterviewScheduleResponse.builder()
                .interviewId(s.getInterviewId())
                .candidateId(s.getCandidateId())
                .subject(s.getSubject())
                .techId(s.getTechId())
                .interviewDate(s.getInterviewDate())
                .interviewTime(s.getInterviewTime())
                .techRating(s.getTechRating())
                .empHrId(s.getEmpHrId())
                .empHrInterviewDate(s.getEmpHrInterviewDate())
                .empHrInterviewTime(s.getEmpHrInterviewTime())
                .empHrRating(s.getEmpHrRating())
                .result(s.getResult())
                .shareResult(s.getShareResult())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }
}
