package com.example.jobmatrix.explore.service;

import com.example.jobmatrix.explore.dto.ExternalJobDto;
import com.example.jobmatrix.explore.dto.JobSearchRequest;
import com.example.jobmatrix.explore.provider.JobProvider;
import com.example.jobmatrix.job.model.JobType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalJobProvider implements JobProvider {

    private final RestClient restClient;

    @Value("${rapidapi.key}")
    private String apiKey;

    @Value("${rapidapi.host}")
    private String host;

    @Override
    public List<ExternalJobDto> searchJobs(
            JobSearchRequest request
    ) {

        return fetchJobsFromApi(request)
                .stream()
                .map(job -> {

                    int score =
                            calculateScore(
                                    job,
                                    request
                            );

                    job.setMatchScore(
                            score
                    );

                    return job;
                })
                .filter(job ->
                        job.getMatchScore() > 0
                )
                .sorted(
                        (a, b) ->
                                Integer.compare(
                                        b.getMatchScore(),
                                        a.getMatchScore()
                                )
                )
                .toList();
    }

    private int calculateScore(
            ExternalJobDto job,
            JobSearchRequest request
    ) {

        int score = 10;

        if(Boolean.TRUE.equals(
                request.getRemoteOnly()
        )) {

            if(job.getJobType()
                    == JobType.REMOTE) {

                score += 30;

            } else {

                return 0;
            }
        }

        if (request.getSkills() != null
                && !request.getSkills().isEmpty()) {

            String searchableText =
                    (
                            (job.getTitle() == null
                                    ? ""
                                    : job.getTitle())
                                    + " "
                                    + (job.getDescription() == null
                                    ? ""
                                    : job.getDescription())
                    ).toLowerCase();

            long skillMatches =
                    request.getSkills()
                            .stream()
                            .filter(skill ->
                                    searchableText.contains(
                                            skill.toLowerCase()
                                    )
                            )
                            .count();

            if (skillMatches > 0) {

                score += (int) skillMatches * 25;

                double ratio =
                        (double) skillMatches
                                / request.getSkills().size();

                score += (int) (ratio * 25);
            }
        }

        if(request.getLocation() != null
                && !request.getLocation().isBlank()
                && job.getLocation() != null) {

            String userLocation =
                    request.getLocation()
                            .toLowerCase();

            String jobLocation =
                    job.getLocation()
                            .toLowerCase();

            if(jobLocation.contains(
                    userLocation
            )) {

                score += 20;
            }
        }

        if(request.getJobType() != null
                && request.getJobType()
                == job.getJobType()) {

            score += 15;
        }

        return score;
    }

    @SuppressWarnings("unchecked")
    private List<ExternalJobDto> fetchJobsFromApi(
            JobSearchRequest request
    ) {

        try {

            StringBuilder query =
                    new StringBuilder();

            if(request.getKeyword() != null
                    && !request.getKeyword().isBlank()) {

                query.append(
                        request.getKeyword()
                );
            }

            if(request.getSkills() != null
                    && !request.getSkills().isEmpty()) {

                query.append(" ")
                        .append(
                                String.join(
                                        " ",
                                        request.getSkills()
                                )
                        );
            }

            if(request.getLocation() != null
                    && !request.getLocation().isBlank()) {

                query.append(" ")
                        .append(
                                request.getLocation()
                        );
            }

            String encodedQuery =
                    URLEncoder.encode(
                            query.toString(),
                            StandardCharsets.UTF_8
                    );

            String url =
                    "https://jsearch.p.rapidapi.com/search"
                            + "?query=" + encodedQuery
                            + "&page=" + (request.getPage() + 1)
                            + "&num_pages=1";

            Map<String, Object> response =
                    restClient.get()
                            .uri(url)
                            .header(
                                    "X-RapidAPI-Key",
                                    apiKey
                            )
                            .header(
                                    "X-RapidAPI-Host",
                                    host
                            )
                            .retrieve()
                            .body(Map.class);

            if (response == null
                    || response.get("data") == null) {

                return List.of();
            }

            List<Map<String, Object>> jobs =
                    (List<Map<String, Object>>)
                            response.get("data");

            return jobs.stream()
                    .map(job -> {

                        String title =
                                (String) job.getOrDefault(
                                        "job_title",
                                        ""
                                );

                        String description =
                                (String) job.getOrDefault(
                                        "job_description",
                                        ""
                                );

                        return ExternalJobDto.builder()
                                .id(
                                        String.valueOf(
                                                job.get("job_id")
                                        )
                                )
                                .title(title)
                                .company(
                                        (String) job.getOrDefault(
                                                "employer_name",
                                                "Unknown Company"
                                        )
                                )
                                .location(
                                        resolveLocation(job)
                                )
                                .description(description)
                                .applyUrl(
                                        (String) job.getOrDefault(
                                                "job_apply_link",
                                                ""
                                        )
                                )
                                .skills(
                                        List.of()
                                )
                                .salary(null)
                                .experience(null)
                                .jobType(
                                        determineJobType(job)
                                )
                                .build();
                    })
                    .toList();

        } catch (Exception e) {

            log.error(
                    "Failed to fetch jobs",
                    e
            );

            return List.of();
        }
    }

    private JobType mapJobType(
            String type
    ) {

        if (type == null) {

            return JobType.FULL_TIME;
        }

        String value =
                type.toLowerCase();

        if (value.contains("intern")) {

            return JobType.INTERNSHIP;
        }

        if (value.contains("contract")) {

            return JobType.CONTRACT;
        }

        if (value.contains("part")) {

            return JobType.PART_TIME;
        }

        if (value.contains("remote")) {

            return JobType.REMOTE;
        }

        return JobType.FULL_TIME;
    }

    private String resolveLocation(
            Map<String, Object> job
    ) {

        String city =
                (String) job.get(
                        "job_city"
                );

        String state =
                (String) job.get(
                        "job_state"
                );

        String country =
                (String) job.get(
                        "job_country"
                );

        if(city != null
                && !city.isBlank()) {

            if(state != null
                    && !state.isBlank()) {

                return city + ", " + state;
            }

            return city;
        }

        if(country != null
                && !country.isBlank()) {

            return country;
        }

        return "Remote";
    }

    private JobType determineJobType(
            Map<String, Object> job
    ) {

        Object remoteFlag =
                job.get("job_is_remote");

        if (remoteFlag != null
                && Boolean.parseBoolean(
                remoteFlag.toString()
        )) {

            return JobType.REMOTE;
        }

        return mapJobType(
                (String) job.get(
                        "job_employment_type"
                )
        );
    }
}