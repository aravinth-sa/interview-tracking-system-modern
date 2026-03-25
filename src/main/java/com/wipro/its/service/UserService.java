package com.wipro.its.service;

import com.wipro.its.entity.User;
import com.wipro.its.enums.UserType;
import com.wipro.its.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getUsersByType(UserType userType) {
        return userRepository.findByUserType(userType);
    }
}
