package com.example.jobmatrix.explore.service;

import com.example.jobmatrix.explore.dto.ExternalJobDto;
import com.example.jobmatrix.explore.dto.JobSearchRequest;
import com.example.jobmatrix.explore.provider.JobProvider;
import com.example.jobmatrix.job.model.JobType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
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
                .map(job -> Map.entry(
                        job,
                        calculateScore(
                                job,
                                request
                        )
                ))
                .filter(
                        entry -> entry.getValue() > 0
                )
                .sorted(
                        Map.Entry
                                .<ExternalJobDto,Integer>
                                        comparingByValue()
                                .reversed()
                )
                .map(Map.Entry::getKey)
                .toList();
    }

    private int calculateScore(
            ExternalJobDto job,
            JobSearchRequest request
    ) {

        int score = 0;

        if (Boolean.TRUE.equals(
                request.getRemoteOnly()
        )) {

            if (job.getJobType()
                    != JobType.REMOTE) {

                return 0;
            }

            score += 25;
        }

        if (request.getSkills() != null
                && !request.getSkills().isEmpty()
                && job.getSkills() != null
                && !job.getSkills().isEmpty())  {

            long skillMatches =
                    request.getSkills()
                            .stream()
                            .filter(skill ->
                                    job.getSkills()
                                            .stream()
                                            .anyMatch(jobSkill ->
                                                    jobSkill.equalsIgnoreCase(skill)
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

        if (job.getJobType() == JobType.REMOTE) {

            score += 10;

        } else if (request.getLocation() != null
                && !request.getLocation().isBlank()
                && job.getLocation() != null
                && !job.getLocation().isBlank()) {

            if (job.getLocation()
                    .toLowerCase()
                    .contains(
                            request.getLocation()
                                    .toLowerCase()
                    )) {

                score += 20;
            }
        }

//        if (request.getExperience() != null
//                && job.getExperience() != null) {
//
//            int diff =
//                    Math.abs(
//                            request.getExperience()
//                                    - job.getExperience()
//                    );
//
//            if (diff == 0) {
//
//                score += 15;
//
//            } else if (diff <= 1) {
//
//                score += 10;
//
//            } else if (diff <= 2) {
//
//                score += 5;
//            }
//        }

        if (request.getJobType() != null
                && request.getJobType()
                == job.getJobType()) {

            score += 10;
        }

//        if (request.getExpectedSalary() != null
//                && job.getSalary() != null
//                && job.getSalary() >= request.getExpectedSalary()) {
//
//            score += 10;
//        }

        return score;
    }

    @SuppressWarnings("unchecked")
    private List<ExternalJobDto> fetchJobsFromApi(
            JobSearchRequest request
    ) {

        try {

            String query =
                    request.getSkills() == null
                            || request.getSkills().isEmpty()
                            ? "Software Developer"
                            : String.join(
                            " ",
                            request.getSkills()
                    );

            if (request.getLocation() != null
                    && !request.getLocation().isBlank()) {

                query += " " + request.getLocation();
            }

            String encodedQuery =
                    URLEncoder.encode(
                            query,
                            StandardCharsets.UTF_8
                    );

            String url =
                    "https://jsearch.p.rapidapi.com/search"
                            + "?query=" + encodedQuery
                            + "&page=1"
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
                                        (String) job.get(
                                                "job_apply_link"
                                        )
                                )
                                .skills(
                                        extractSkills(
                                                title,
                                                description
                                        )
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

            e.printStackTrace();

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
                (String) job.get("job_city");

        if (city != null
                && !city.isBlank()) {

            return city;
        }

        String country =
                (String) job.get("job_country");

        if (country != null
                && !country.isBlank()) {

            return country;
        }

        return "Remote";
    }

    private List<String> extractSkills(
            String title,
            String description
    ) {

        String text =
                (title + " " + description)
                        .toLowerCase();

        List<String> knownSkills =
                List.of(
                        "java",
                        "spring",
                        "spring boot",
                        "react",
                        "angular",
                        "node",
                        "node.js",
                        "mongodb",
                        "mysql",
                        "PostgreSQL",
                        "redis",
                        "docker",
                        "kubernetes",
                        "aws",
                        "javascript",
                        "typescript",
                        "python",
                        "django",
                        "flask"
                );

        return knownSkills.stream()
                .filter(text::contains)
                .toList();
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