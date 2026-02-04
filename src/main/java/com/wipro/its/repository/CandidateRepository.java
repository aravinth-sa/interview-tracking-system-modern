package com.wipro.its.repository;

import com.wipro.its.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {

    List<Candidate> findByPrimarySkillsAndQualification(String primarySkills, String qualification);

    List<Candidate> findByShareDetailsTrue();

    @Query("SELECT c FROM Candidate c WHERE " +
           "(:primarySkills IS NULL OR c.primarySkills = :primarySkills) AND " +
           "(:qualification IS NULL OR c.qualification = :qualification) AND " +
           "(:location IS NULL OR c.location = :location)")
    List<Candidate> searchCandidates(
        @Param("primarySkills") String primarySkills,
        @Param("qualification") String qualification,
        @Param("location") String location
    );

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(c.candidateId, 3) AS int)), 0) FROM Candidate c " +
           "WHERE c.candidateId LIKE :prefix%")
    Integer findMaxSequenceByPrefix(@Param("prefix") String prefix);
}
