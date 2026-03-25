package com.wipro.its.mapper;

import com.wipro.its.dto.CandidateResponse;
import com.wipro.its.entity.Candidate;
import com.wipro.its.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    @Mapping(source = "candidate.candidateId", target = "candidateId")
    @Mapping(source = "candidate.primarySkills", target = "primarySkills")
    @Mapping(source = "candidate.secondarySkills", target = "secondarySkills")
    @Mapping(source = "candidate.experience", target = "experience")
    @Mapping(source = "candidate.qualification", target = "qualification")
    @Mapping(source = "candidate.designation", target = "designation")
    @Mapping(source = "candidate.noticePeriod", target = "noticePeriod")
    @Mapping(source = "candidate.location", target = "location")
    @Mapping(source = "candidate.shareDetails", target = "shareDetails")
    @Mapping(source = "candidate.createdAt", target = "createdAt")
    @Mapping(source = "candidate.updatedAt", target = "updatedAt")
    @Mapping(source = "profile.firstName", target = "firstName")
    @Mapping(source = "profile.lastName", target = "lastName")
    @Mapping(source = "profile.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "profile.gender", target = "gender")
    @Mapping(source = "profile.city", target = "city")
    @Mapping(source = "profile.state", target = "state")
    @Mapping(source = "profile.mobileNo", target = "mobileNo")
    @Mapping(source = "profile.emailId", target = "emailId")
    CandidateResponse toResponse(Candidate candidate, Profile profile);
}
