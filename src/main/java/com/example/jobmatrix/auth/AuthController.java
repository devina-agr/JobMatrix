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

    private final UserService userService;
    private final JwtService jwtServices;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtService jwtServices, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtServices = jwtServices;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest authRequest){
        User user=userService.register(authRequest.getEmail(),authRequest.getPassword());
        TokenPayload payload=new TokenPayload(user.getEmail(), user.getTokenVersion());
        String token=jwtServices.generateToken(payload);
        AuthResponse response=new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest authRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        User user=userService.getByEmail(authRequest.getEmail());
        TokenPayload payload=new TokenPayload(user.getEmail(), user.getTokenVersion());
        String token=jwtServices.generateToken(payload);
        AuthResponse response=new AuthResponse(token,user.getEmail(),user.getRole().name());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request){
        userService.resetPassword(request.getToken(),request.getNewPassword());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Password reset successfully! Please login again.");
    }
}
