package com.wipro.its.service;

import com.wipro.its.dto.LoginRequest;
import com.wipro.its.dto.LoginResponse;
import com.wipro.its.entity.User;
import com.wipro.its.enums.UserType;
import com.wipro.its.exception.ResourceNotFoundException;
import com.wipro.its.repository.UserRepository;
import com.wipro.its.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User adminUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .userId("ADMIN001")
                .password("$2a$10$encoded")
                .userType(UserType.ADMIN)
                .loginStatus(false)
                .enabled(true)
                .build();

        loginRequest = LoginRequest.builder()
                .username("ADMIN001")
                .password("admin123")
                .build();
    }

    @Test
    @DisplayName("login: should return token and set loginStatus to true on success")
    void login_success() {
        // GIVEN
        // authenticationManager.authenticate() → does nothing (void) by default in Mockito
        given(userRepository.findByUserId("ADMIN001")).willReturn(Optional.of(adminUser));
        given(jwtService.generateToken(adminUser)).willReturn("mock-jwt-token");
        given(jwtService.getExpiration()).willReturn(86400000L);

        // WHEN
        LoginResponse response = authenticationService.login(loginRequest);

        // THEN
        assertThat(response.getAccessToken()).isEqualTo("mock-jwt-token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUserId()).isEqualTo("ADMIN001");
        assertThat(response.getUserType()).isEqualTo("ADMIN");

        // loginStatus must be saved as true
        assertThat(adminUser.getLoginStatus()).isTrue();
        verify(userRepository).save(adminUser);
    }

    @Test
    @DisplayName("login: should throw BadCredentialsException when password is wrong")
    void login_wrongPassword_throwsException() {
        // GIVEN — authenticationManager throws when credentials are bad
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("Bad credentials"));

        // WHEN + THEN
        assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("login: should throw ResourceNotFoundException when user not in DB")
    void login_userNotFound_throwsException() {
        // authenticationManager passes (valid Spring Security user) but DB lookup fails
        given(userRepository.findByUserId("ADMIN001")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.login(loginRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("ADMIN001");
    }

    @Test
    @DisplayName("logout: should set loginStatus to false")
    void logout_success() {
        adminUser.setLoginStatus(true);
        given(userRepository.findByUserId("ADMIN001")).willReturn(Optional.of(adminUser));

        authenticationService.logout("ADMIN001");

        assertThat(adminUser.getLoginStatus()).isFalse();
        verify(userRepository).save(adminUser);
    }

    @Test
    @DisplayName("logout: should throw ResourceNotFoundException when user not found")
    void logout_notFound_throwsException() {
        given(userRepository.findByUserId("GHOST")).willReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.logout("GHOST"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("GHOST");
    }
}
