package com.wipro.its.mapper;

import com.wipro.its.dto.InterviewScheduleResponse;
import com.wipro.its.entity.InterviewSchedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InterviewScheduleMapper {

    InterviewScheduleResponse toResponse(InterviewSchedule interviewSchedule);
}
