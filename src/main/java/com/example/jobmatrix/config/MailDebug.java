package com.example.jobmatrix.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailDebug {

    private final Environment env;

    @PostConstruct
    public void debug() {

        System.out.println(
                "MAIL HOST = " +
                        env.getProperty(
                                "spring.mail.host"
                        )
        );

        System.out.println(
                "MAIL USER = " +
                        env.getProperty(
                                "spring.mail.username"
                        )
        );
    }
}