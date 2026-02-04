package com.wipro.its.repository;

import com.wipro.its.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, String> {

    List<InterviewSchedule> findByTechIdAndInterviewDate(String techId, LocalDate interviewDate);

    List<InterviewSchedule> findByEmpHrIdAndEmpHrInterviewDate(String empHrId, LocalDate empHrInterviewDate);

    List<InterviewSchedule> findByCandidateId(String candidateId);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(i.interviewId, 3) AS int)), 0) FROM InterviewSchedule i " +
           "WHERE i.interviewId LIKE :prefix%")
    Integer findMaxSequenceByPrefix(@Param("prefix") String prefix);

    @Query("SELECT i FROM InterviewSchedule i WHERE i.shareResult = true")
    List<InterviewSchedule> findSharedResults();
}
