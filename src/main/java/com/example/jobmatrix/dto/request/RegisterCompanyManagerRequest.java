package com.example.jobmatrix.dto.request;

import com.example.jobmatrix.company.model.Industry;
import com.example.jobmatrix.user.model.Department;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterCompanyManagerRequest {

    private String username;

    private String email;

    private String password;

    private String companyName;

    private String companyWebsite;

    private Industry industry;

}