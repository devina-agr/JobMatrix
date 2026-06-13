package com.example.jobmatrix.dto.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {

    private Long id;

    private String title;

    private String companyName;

    private String location;

    private Double salary;

    private Integer experienceRequired;

    private Set<String> skills;
}