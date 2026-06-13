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

        emailService.sendEmail(
                email,
                "JobMatrix OTP",
                "Your OTP is: " + otp
        );
    }

    public boolean verifyOtp(
            String email,
            String otp
    ) {

        String savedOtp =
                (String) redisService.get(
                        "otp:" + email
                );

        return otp.equals(savedOtp);
    }

}
