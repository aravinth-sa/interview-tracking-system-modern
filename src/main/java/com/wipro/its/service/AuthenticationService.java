package com.wipro.its.service;

import com.wipro.its.dto.LoginRequest;
import com.wipro.its.dto.LoginResponse;
import com.wipro.its.entity.User;
import com.wipro.its.exception.ResourceNotFoundException;
import com.wipro.its.repository.UserRepository;
import com.wipro.its.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUserId(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUsername()));

        user.setLoginStatus(true);
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpiration())
                .userId(user.getUserId())
                .userType(user.getUserType().name())
                .build();
    }

    @Transactional
    public void logout(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        user.setLoginStatus(false);
        userRepository.save(user);
    }
}
