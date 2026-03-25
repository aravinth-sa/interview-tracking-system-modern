package com.wipro.its.service;

import com.wipro.its.dto.CandidateRequest;
import com.wipro.its.dto.CandidateResponse;
import com.wipro.its.entity.Candidate;
import com.wipro.its.entity.Profile;
import com.wipro.its.exception.DuplicateResourceException;
import com.wipro.its.exception.ResourceNotFoundException;
import com.wipro.its.repository.CandidateRepository;
import com.wipro.its.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CandidateResponse create(CandidateRequest request) {
        if (profileRepository.existsByEmailId(request.getEmailId())) {
            throw new DuplicateResourceException("Candidate with email already exists: " + request.getEmailId());
        }

        String candidateId = generateId("CA", candidateRepository.findMaxSequenceByPrefix("CA"));

        Candidate candidate = Candidate.builder()
                .candidateId(candidateId)
                .primarySkills(request.getPrimarySkills())
                .secondarySkills(request.getSecondarySkills())
                .experience(request.getExperience())
                .qualification(request.getQualification())
                .designation(request.getDesignation())
                .noticePeriod(request.getNoticePeriod())
                .location(request.getLocation())
                .shareDetails(false)
                .build();

        candidateRepository.save(candidate);

        Profile profile = Profile.builder()
                .candidateId(candidateId)
                .candidate(candidate)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .street(request.getStreet())
                .location(request.getLocation())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .mobileNo(request.getMobileNo())
                .emailId(request.getEmailId())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        profileRepository.save(profile);
        candidate.setProfile(profile);

        return toResponse(candidate, profile);
    }

    @Transactional(readOnly = true)
    public CandidateResponse findById(String id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", id));
        Profile profile = profileRepository.findById(id).orElse(null);
        return toResponse(candidate, profile);
    }

    @Transactional(readOnly = true)
    public List<CandidateResponse> search(String primarySkills, String qualification, String location) {
        return candidateRepository.searchCandidates(primarySkills, qualification, location)
                .stream()
                .map(c -> toResponse(c, c.getProfile()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CandidateResponse update(String id, CandidateRequest request) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", id));
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", id));

        candidate.setPrimarySkills(request.getPrimarySkills());
        candidate.setSecondarySkills(request.getSecondarySkills());
        candidate.setExperience(request.getExperience());
        candidate.setQualification(request.getQualification());
        candidate.setDesignation(request.getDesignation());
        candidate.setNoticePeriod(request.getNoticePeriod());
        candidate.setLocation(request.getLocation());

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setStreet(request.getStreet());
        profile.setLocation(request.getLocation());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setPincode(request.getPincode());
        profile.setMobileNo(request.getMobileNo());
        profile.setEmailId(request.getEmailId());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            profile.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        candidateRepository.save(candidate);
        profileRepository.save(profile);

        return toResponse(candidate, profile);
    }

    @Transactional
    public void delete(String id) {
        if (!candidateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate", id);
        }
        candidateRepository.deleteById(id);
    }

    private String generateId(String prefix, Integer maxSeq) {
        int next = (maxSeq == null ? 0 : maxSeq) + 1;
        return String.format("%s%03d", prefix, next);
    }

    private CandidateResponse toResponse(Candidate c, Profile p) {
        CandidateResponse.CandidateResponseBuilder builder = CandidateResponse.builder()
                .candidateId(c.getCandidateId())
                .primarySkills(c.getPrimarySkills())
                .secondarySkills(c.getSecondarySkills())
                .experience(c.getExperience())
                .qualification(c.getQualification())
                .designation(c.getDesignation())
                .noticePeriod(c.getNoticePeriod())
                .location(c.getLocation())
                .shareDetails(c.getShareDetails())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt());

        if (p != null) {
            builder.firstName(p.getFirstName())
                    .lastName(p.getLastName())
                    .dateOfBirth(p.getDateOfBirth())
                    .gender(p.getGender())
                    .city(p.getCity())
                    .state(p.getState())
                    .mobileNo(p.getMobileNo())
                    .emailId(p.getEmailId());
        }

        return builder.build();
    }
}
