package com.wipro.its.repository;

import com.wipro.its.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

    Optional<Profile> findByEmailId(String emailId);

    boolean existsByEmailId(String emailId);
}
