package com.wipro.its.service;

import com.wipro.its.dto.InterviewScheduleRequest;
import com.wipro.its.dto.InterviewScheduleResponse;
import com.wipro.its.dto.RatingRequest;
import com.wipro.its.entity.InterviewSchedule;
import com.wipro.its.exception.ResourceNotFoundException;
import com.wipro.its.repository.CandidateRepository;
import com.wipro.its.repository.InterviewScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewScheduleService {

    private final InterviewScheduleRepository interviewScheduleRepository;
    private final CandidateRepository candidateRepository;

    @Transactional
    public InterviewScheduleResponse schedule(InterviewScheduleRequest request) {
        if (!candidateRepository.existsById(request.getCandidateId())) {
            throw new ResourceNotFoundException("Candidate", request.getCandidateId());
        }

        String interviewId = generateId("IV", interviewScheduleRepository.findMaxSequenceByPrefix("IV"));

        InterviewSchedule schedule = InterviewSchedule.builder()
                .interviewId(interviewId)
                .candidateId(request.getCandidateId())
                .subject(request.getSubject())
                .techId(request.getTechId())
                .interviewDate(request.getInterviewDate())
                .interviewTime(request.getInterviewTime())
                .empHrId(request.getEmpHrId())
                .empHrInterviewDate(request.getEmpHrInterviewDate())
                .empHrInterviewTime(request.getEmpHrInterviewTime())
                .shareResult(false)
                .build();

        return toResponse(interviewScheduleRepository.save(schedule));
    }

    @Transactional(readOnly = true)
    public InterviewScheduleResponse findById(String id) {
        return toResponse(getScheduleOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<InterviewScheduleResponse> getByTechPanel(String techId) {
        return interviewScheduleRepository.findByTechIdAndInterviewDate(techId, null).isEmpty()
                ? interviewScheduleRepository.findAll().stream()
                        .filter(s -> techId.equals(s.getTechId()))
                        .map(this::toResponse)
                        .collect(Collectors.toList())
                : interviewScheduleRepository.findAll().stream()
                        .filter(s -> techId.equals(s.getTechId()))
                        .map(this::toResponse)
                        .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterviewScheduleResponse> getByHrPanel(String hrId) {
        return interviewScheduleRepository.findAll().stream()
                .filter(s -> hrId.equals(s.getEmpHrId()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InterviewScheduleResponse submitTechRating(String id, RatingRequest request) {
        InterviewSchedule schedule = getScheduleOrThrow(id);
        schedule.setTechRating(request.getRating());
        return toResponse(interviewScheduleRepository.save(schedule));
    }

    @Transactional
    public InterviewScheduleResponse submitHrRating(String id, RatingRequest request) {
        InterviewSchedule schedule = getScheduleOrThrow(id);
        schedule.setEmpHrRating(request.getRating());
        return toResponse(interviewScheduleRepository.save(schedule));
    }

    @Transactional
    public InterviewScheduleResponse finalizeResult(String id, String result) {
        InterviewSchedule schedule = getScheduleOrThrow(id);
        schedule.setResult(result);
        return toResponse(interviewScheduleRepository.save(schedule));
    }

    @Transactional
    public InterviewScheduleResponse shareResult(String id) {
        InterviewSchedule schedule = getScheduleOrThrow(id);
        schedule.setShareResult(true);
        return toResponse(interviewScheduleRepository.save(schedule));
    }

    private InterviewSchedule getScheduleOrThrow(String id) {
        return interviewScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InterviewSchedule", id));
    }

    private String generateId(String prefix, Integer maxSeq) {
        int next = (maxSeq == null ? 0 : maxSeq) + 1;
        return String.format("%s%03d", prefix, next);
    }

    private InterviewScheduleResponse toResponse(InterviewSchedule s) {
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
