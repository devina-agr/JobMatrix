package com.example.jobmatrix.otp;

import com.example.jobmatrix.notification.service.EmailService;
import com.example.jobmatrix.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisService redisService;

    private final EmailService emailService;

    public void sendOtp(
            String email
    ) {

        String otp =
                generateOtp(email);

        emailService.sendEmail(
                email,
                "JobMatrix OTP",
                """
                <html>
                <body>

                <h2>JobMatrix OTP Verification</h2>

                <p>Your OTP is:</p>

                <h1>%s</h1>

                <p>
                This OTP expires in 5 minutes.
                </p>

                </body>
                </html>
                """
                        .formatted(otp)
        );
    }

    public String generateOtp(
            String email
    ) {

        String otp =
                String.valueOf(
                        100000 +
                                new Random()
                                        .nextInt(900000)
                );

        redisService.save(
                "otp:" + email,
                otp,
                5
        );

        return otp;
    }

    public boolean verifyOtp(
            String email,
            String otp
    ) {

        String savedOtp =
                (String) redisService.get(
                        "otp:" + email
                );

        if(savedOtp == null){
            return false;
        }

        return savedOtp.equals(otp);
    }
}