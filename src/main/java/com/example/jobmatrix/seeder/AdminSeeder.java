package com.example.jobmatrix.seeder;


import com.example.jobmatrix.user.model.Role;
import com.example.jobmatrix.user.model.User;
import com.example.jobmatrix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if(userRepository.existsByRole(
                Role.ROLE_ADMIN
        )) {
            return;
        }

        User admin = User.builder()
                .username("admin")
                .email("admin@jobmatrix.com")
                .password(
                        passwordEncoder.encode(
                                "Admin@123"
                        )
                )
                .role(Role.ROLE_ADMIN)
                .enabled(true)
                .tokenVersion(0)
                .build();

        userRepository.save(admin);
    }
}
