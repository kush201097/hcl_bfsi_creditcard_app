package com.hcl.hackathon.authservice.service;

import com.hcl.hackathon.authservice.dto.AuthResponse;
import com.hcl.hackathon.authservice.dto.LoginRequest;
import com.hcl.hackathon.authservice.dto.SignupRequest;
import com.hcl.hackathon.authservice.entity.User;
import com.hcl.hackathon.authservice.mapper.UserMapper;
import com.hcl.hackathon.authservice.repository.UserRepository;
import com.hcl.hackathon.common.exception.BusinessException;
import com.hcl.hackathon.common.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuditService auditService;

    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already registered", "USER_ALREADY_EXISTS");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Phone number is already registered", "USER_ALREADY_EXISTS");
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getId());
        
        auditService.logEvent(savedUser.getId().toString(), "User", "USER_SIGNUP", savedUser.getEmail());
        
        return AuthResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .token(token)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password", "UNAUTHORIZED"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("Invalid email or password", "UNAUTHORIZED");
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException("User account is not active", "ACCOUNT_DISABLED");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        
        auditService.logEvent(user.getId().toString(), "User", "USER_LOGIN", user.getEmail());

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .token(token)
                .build();
    }
}
