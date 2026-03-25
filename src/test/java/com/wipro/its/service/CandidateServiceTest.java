package com.wipro.its.service;

import com.wipro.its.dto.CandidateRequest;
import com.wipro.its.dto.CandidateResponse;
import com.wipro.its.entity.Candidate;
import com.wipro.its.entity.Profile;
import com.wipro.its.exception.DuplicateResourceException;
import com.wipro.its.exception.ResourceNotFoundException;
import com.wipro.its.repository.CandidateRepository;
import com.wipro.its.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

// @ExtendWith(MockitoExtension.class) → no Spring context started, just Mockito
// This makes tests run in milliseconds instead of seconds
@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    // @Mock creates a fake version of these — no real DB calls happen
    @Mock private CandidateRepository candidateRepository;
    @Mock private ProfileRepository profileRepository;
    @Mock private PasswordEncoder passwordEncoder;

    // @InjectMocks creates a real CandidateService and injects the @Mocks above into it
    @InjectMocks
    private CandidateService candidateService;

    private CandidateRequest validRequest;
    private Candidate savedCandidate;
    private Profile savedProfile;

    // @BeforeEach runs before every single test method — sets up reusable test data
    @BeforeEach
    void setUp() {
        validRequest = CandidateRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .emailId("john.doe@example.com")
                .password("password123")
                .primarySkills("Java, Spring Boot")
                .experience(5.0)
                .qualification("B.Tech")
                .designation("Software Engineer")
                .noticePeriod(30)
                .location("Chennai")
                .build();

        savedCandidate = Candidate.builder()
                .candidateId("CA001")
                .primarySkills("Java, Spring Boot")
                .experience(5.0)
                .qualification("B.Tech")
                .designation("Software Engineer")
                .noticePeriod(30)
                .location("Chennai")
                .shareDetails(false)
                .build();

        savedProfile = Profile.builder()
                .candidateId("CA001")
                .firstName("John")
                .lastName("Doe")
                .emailId("john.doe@example.com")
                .build();

        savedCandidate.setProfile(savedProfile);
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("create: should create candidate and profile when email is unique")
    void create_success() {
        // GIVEN — set up what the fakes return when called
        // "given profileRepository.existsByEmailId() is called with any string, return false"
        given(profileRepository.existsByEmailId(anyString())).willReturn(false);
        given(candidateRepository.findMaxSequenceByPrefix("CA")).willReturn(null);
        given(passwordEncoder.encode(anyString())).willReturn("encoded-password");
        given(candidateRepository.save(any(Candidate.class))).willReturn(savedCandidate);
        given(profileRepository.save(any(Profile.class))).willReturn(savedProfile);

        // WHEN — call the real method
        CandidateResponse response = candidateService.create(validRequest);

        // THEN — assert the result
        assertThat(response).isNotNull();
        assertThat(response.getCandidateId()).isEqualTo("CA001");
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getEmailId()).isEqualTo("john.doe@example.com");

        // Verify that save was actually called on both repositories
        verify(candidateRepository).save(any(Candidate.class));
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("create: should throw DuplicateResourceException when email already exists")
    void create_duplicateEmail_throwsException() {
        // GIVEN — fake returns true meaning email already taken
        given(profileRepository.existsByEmailId("john.doe@example.com")).willReturn(true);

        // WHEN + THEN — assert that the right exception is thrown
        assertThatThrownBy(() -> candidateService.create(validRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("john.doe@example.com");

        // Verify that save was NEVER called — no partial data saved
        verify(candidateRepository, never()).save(any());
        verify(profileRepository, never()).save(any());
    }

    @Test
    @DisplayName("create: candidate ID should be CA001 when no candidates exist yet")
    void create_generatesCorrectId_whenNoCandidatesExist() {
        given(profileRepository.existsByEmailId(anyString())).willReturn(false);
        given(candidateRepository.findMaxSequenceByPrefix("CA")).willReturn(null); // no candidates yet
        given(passwordEncoder.encode(anyString())).willReturn("encoded");
        given(candidateRepository.save(any(Candidate.class))).willReturn(savedCandidate);
        given(profileRepository.save(any(Profile.class))).willReturn(savedProfile);

        CandidateResponse response = candidateService.create(validRequest);

        // null maxSeq → next = 1 → "CA001"
        assertThat(response.getCandidateId()).isEqualTo("CA001");
    }

    // ── FIND BY ID ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findById: should return candidate when found")
    void findById_success() {
        given(candidateRepository.findById("CA001")).willReturn(Optional.of(savedCandidate));
        given(profileRepository.findById("CA001")).willReturn(Optional.of(savedProfile));

        CandidateResponse response = candidateService.findById("CA001");

        assertThat(response.getCandidateId()).isEqualTo("CA001");
        assertThat(response.getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("findById: should throw ResourceNotFoundException when candidate not found")
    void findById_notFound_throwsException() {
        given(candidateRepository.findById("CA999")).willReturn(Optional.empty());

        assertThatThrownBy(() -> candidateService.findById("CA999"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("CA999");
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("delete: should delete candidate when found")
    void delete_success() {
        given(candidateRepository.existsById("CA001")).willReturn(true);

        // No exception = success
        candidateService.delete("CA001");

        verify(candidateRepository).deleteById("CA001");
    }

    @Test
    @DisplayName("delete: should throw ResourceNotFoundException when candidate not found")
    void delete_notFound_throwsException() {
        given(candidateRepository.existsById("CA999")).willReturn(false);

        assertThatThrownBy(() -> candidateService.delete("CA999"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("CA999");

        verify(candidateRepository, never()).deleteById(anyString());
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("search: should return list of matching candidates")
    void search_returnsList() {
        given(candidateRepository.searchCandidates("Java", null, null))
                .willReturn(List.of(savedCandidate));

        List<CandidateResponse> results = candidateService.search("Java", null, null);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPrimarySkills()).isEqualTo("Java, Spring Boot");
    }

    @Test
    @DisplayName("search: should return empty list when no matches")
    void search_returnsEmptyList() {
        given(candidateRepository.searchCandidates("COBOL", null, null))
                .willReturn(List.of());

        List<CandidateResponse> results = candidateService.search("COBOL", null, null);

        assertThat(results).isEmpty();
    }
}
