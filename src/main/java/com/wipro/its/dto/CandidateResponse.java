package com.wipro.its.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResponse {

    private String candidateId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String location;
    private String city;
    private String state;
    private String mobileNo;
    private String emailId;
    private String primarySkills;
    private String secondarySkills;
    private Float experience;
    private String qualification;
    private String designation;
    private Integer noticePeriod;
    private Boolean shareDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
