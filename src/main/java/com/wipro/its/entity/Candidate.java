package com.wipro.its.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Candidate {

    @Id
    @Column(name = "candidate_id", length = 50)
    private String candidateId;

    @Column(name = "primary_skills", length = 200)
    private String primarySkills;

    @Column(name = "secondary_skills", length = 200)
    private String secondarySkills;

    @Column(name = "experience")
    private Float experience;

    @Column(name = "qualification", length = 100)
    private String qualification;

    @Column(name = "designation", length = 100)
    private String designation;

    @Column(name = "notice_period")
    private Integer noticePeriod;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "share_details")
    private Boolean shareDetails = false;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
