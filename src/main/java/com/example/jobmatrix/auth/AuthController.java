package com.example.jobmatrix.auth;

import com.example.jobmatrix.dto.TokenPayload;
import com.example.jobmatrix.dto.request.LoginRequest;
import com.example.jobmatrix.dto.request.RegisterRequest;
import com.example.jobmatrix.dto.request.ResetPasswordRequest;
import com.example.jobmatrix.dto.response.AuthResponse;
import com.example.jobmatrix.security.JwtService;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtServices;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, JwtService jwtServices, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtServices = jwtServices;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

}
