package com.example.jobmatrix.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobResponse {
    private String id;
    private String title;
    private String companyName;
    private String location;
    private Double salary;
}
