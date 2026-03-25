package com.wipro.its.security;

import com.wipro.its.entity.User;
import com.wipro.its.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String role = "ROLE_" + user.getUserType().name();

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(role)))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!Boolean.TRUE.equals(user.getEnabled()))
                .build();
    }
}
