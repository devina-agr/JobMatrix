package com.example.jobmatrix.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recruiter_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterProfile {

    @Id
    private String id;
    @Indexed
    private String userId;
    @Indexed
    private String companyName;
    private String companyWebsiteUrl;
    private String companyLogoUrl;
    private String companyDescription;
    @Indexed
    private String industry;
    @Indexed
    private boolean verified;
}
