package com.example.jobmatrix.debug;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheDebugRunner implements CommandLineRunner {

    private final CacheManager cacheManager;

    @Override
    public void run(String... args) {

        System.out.println(
                "Cache Manager = "
                        + cacheManager.getClass()
        );
    }
}