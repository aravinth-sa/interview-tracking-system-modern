package com.wipro.its.controller;

import com.wipro.its.dto.ApiResponse;
import com.wipro.its.dto.InterviewScheduleResponse;
import com.wipro.its.dto.RatingRequest;
import com.wipro.its.service.InterviewScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
public class HRPanelController {

    private final InterviewScheduleService interviewScheduleService;

    @GetMapping("/interviews")
    public ResponseEntity<ApiResponse<List<InterviewScheduleResponse>>> getMyInterviews(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<InterviewScheduleResponse> interviews =
                interviewScheduleService.getByHrPanel(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(interviews));
    }

    @GetMapping("/interviews/{id}")
    public ResponseEntity<ApiResponse<InterviewScheduleResponse>> getInterview(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(interviewScheduleService.findById(id)));
    }

    @PostMapping("/interviews/{id}/rating")
    public ResponseEntity<ApiResponse<InterviewScheduleResponse>> submitRating(
            @PathVariable String id,
            @Valid @RequestBody RatingRequest request) {
        return ResponseEntity.ok(ApiResponse.success("HR rating submitted",
                interviewScheduleService.submitHrRating(id, request)));
    }
}
