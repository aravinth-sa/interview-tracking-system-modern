package com.wipro.its.controller;

import com.wipro.its.dto.ApiResponse;
import com.wipro.its.dto.CandidateRequest;
import com.wipro.its.dto.CandidateResponse;
import com.wipro.its.dto.InterviewScheduleRequest;
import com.wipro.its.dto.InterviewScheduleResponse;
import com.wipro.its.service.CandidateService;
import com.wipro.its.service.InterviewScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CandidateService candidateService;
    private final InterviewScheduleService interviewScheduleService;

    // ── Candidate Endpoints ──────────────────────────────────────────────────

    @PostMapping("/candidates")
    public ResponseEntity<ApiResponse<CandidateResponse>> createCandidate(
            @Valid @RequestBody CandidateRequest request) {
        CandidateResponse response = candidateService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Candidate created", response));
    }

    @GetMapping("/candidates")
    public ResponseEntity<ApiResponse<List<CandidateResponse>>> searchCandidates(
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String qualification,
            @RequestParam(required = false) String location) {
        List<CandidateResponse> candidates = candidateService.search(skills, qualification, location);
        return ResponseEntity.ok(ApiResponse.success(candidates));
    }

    @GetMapping("/candidates/{id}")
    public ResponseEntity<ApiResponse<CandidateResponse>> getCandidate(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(candidateService.findById(id)));
    }

    @PutMapping("/candidates/{id}")
    public ResponseEntity<ApiResponse<CandidateResponse>> updateCandidate(
            @PathVariable String id,
            @Valid @RequestBody CandidateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Candidate updated", candidateService.update(id, request)));
    }

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCandidate(@PathVariable String id) {
        candidateService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Candidate deleted", null));
    }

    // ── Interview Schedule Endpoints ─────────────────────────────────────────

    @PostMapping("/interviews")
    public ResponseEntity<ApiResponse<InterviewScheduleResponse>> scheduleInterview(
            @Valid @RequestBody InterviewScheduleRequest request) {
        InterviewScheduleResponse response = interviewScheduleService.schedule(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Interview scheduled", response));
    }

    @PutMapping("/interviews/{id}/finalize")
    public ResponseEntity<ApiResponse<InterviewScheduleResponse>> finalizeResult(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String result = body.get("result");
        return ResponseEntity.ok(ApiResponse.success("Result finalized",
                interviewScheduleService.finalizeResult(id, result)));
    }

    @PutMapping("/interviews/{id}/share")
    public ResponseEntity<ApiResponse<InterviewScheduleResponse>> shareResult(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Result shared",
                interviewScheduleService.shareResult(id)));
    }
}
