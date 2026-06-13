package com.example.jobmatrix.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()

                .info(
                        new Info()
                                .title(
                                        "JobMatrix API"
                                )
                                .version("1.0")
                                .description(
                                        "Job Portal API"
                                )
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Bearer Authentication")
                )

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "Bearer Authentication",

                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(
                                                        SecurityScheme.Type.HTTP
                                                )
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}