package com.wipro.its.repository;

import com.wipro.its.entity.User;
import com.wipro.its.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);

    List<User> findByUserType(UserType userType);

    boolean existsByUserId(String userId);
}
