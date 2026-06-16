package com.example.jobmatrix.explore.dto;

import com.example.jobmatrix.job.model.JobType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class JobSearchRequest implements Serializable {

    private List<String> skills;

    private String location;

    private Integer experience;

    private Double expectedSalary;

    private JobType jobType;

    private Boolean remoteOnly;

    private String keyword;

    private Integer page = 0;

    private Integer size = 10;

    public String cacheKey() {

        return String.join(
                "_",
                keyword == null ? "NA" : keyword.toLowerCase(),
                skills == null ? "NA" : String.join(",", skills),
                location == null ? "NA" : location.toLowerCase(),
                experience == null ? "NA" : experience.toString(),
                expectedSalary == null ? "NA" : expectedSalary.toString(),
                jobType == null ? "NA" : jobType.name(),
                remoteOnly == null ? "NA" : remoteOnly.toString(),
                page.toString(),
                size.toString()
        );
    }
}