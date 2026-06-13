package com.example.jobmatrix.otp;

import com.example.jobmatrix.otp.dto.SendOtpRequest;
import com.example.jobmatrix.otp.dto.VerifyOtpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(
            @RequestBody SendOtpRequest request
    ) {

        otpService.sendOtp(
                request.getEmail()
        );

        return ResponseEntity.ok(
                "OTP sent successfully"
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyOtp(
            @RequestBody VerifyOtpRequest request
    ) {

        return ResponseEntity.ok(
                otpService.verifyOtp(
                        request.getEmail(),
                        request.getOtp()
                )
        );
    }
}
