package com.wipro.its.mapper;

import com.wipro.its.dto.CandidateResponse;
import com.wipro.its.entity.Candidate;
import com.wipro.its.entity.Profile;
import org.springframework.stereotype.Component;

/**
 * Manual mapper — converts Candidate + Profile entities to CandidateResponse DTO.
 * Previously used MapStruct (@Mapper), replaced with manual implementation
 * because MapStruct's annotation processor does not support Java 21+.
 * The mapping logic is identical — just written by hand instead of generated.
 */
@Component
public class CandidateMapper {

    public CandidateResponse toResponse(Candidate candidate, Profile profile) {
        CandidateResponse.CandidateResponseBuilder builder = CandidateResponse.builder()
                .candidateId(candidate.getCandidateId())
                .primarySkills(candidate.getPrimarySkills())
                .secondarySkills(candidate.getSecondarySkills())
                .experience(candidate.getExperience())
                .qualification(candidate.getQualification())
                .designation(candidate.getDesignation())
                .noticePeriod(candidate.getNoticePeriod())
                .location(candidate.getLocation())
                .shareDetails(candidate.getShareDetails())
                .createdAt(candidate.getCreatedAt())
                .updatedAt(candidate.getUpdatedAt());

        if (profile != null) {
            builder.firstName(profile.getFirstName())
                    .lastName(profile.getLastName())
                    .dateOfBirth(profile.getDateOfBirth())
                    .gender(profile.getGender())
                    .city(profile.getCity())
                    .state(profile.getState())
                    .mobileNo(profile.getMobileNo())
                    .emailId(profile.getEmailId());
        }

        return builder.build();
    }
}
