package com.example.jobmatrix.auth;


import com.example.jobmatrix.dto.TokenPayload;
import com.example.jobmatrix.dto.request.LoginRequest;
import com.example.jobmatrix.dto.request.RegisterRequest;
import com.example.jobmatrix.dto.response.AuthResponse;
import com.example.jobmatrix.security.JwtService;
import com.example.jobmatrix.user.model.Role;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())){

            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())
                )
                .role(
                        request.getRole() == null? Role.ROLE_CANDIDATE: request.getRole()
                )
                .enabled(true)
                .tokenVersion(0)
                .build();

        userRepository.save(user);

        TokenPayload payload = new TokenPayload(user.getEmail(), user.getTokenVersion());

        String token = jwtService.generateToken(payload);

        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        TokenPayload payload = new TokenPayload(user.getEmail(), user.getTokenVersion());

        String token = jwtService.generateToken(payload);

        return new AuthResponse(token, user.getEmail(),user.getRole().name());
    }
}
