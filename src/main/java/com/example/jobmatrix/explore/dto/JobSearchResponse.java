package com.example.jobmatrix.explore.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSearchResponse implements Serializable {

    private long totalJobs;

    private int page;

    private int size;

    private List<ExternalJobDto> jobs;
}