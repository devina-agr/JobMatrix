package com.example.jobmatrix.dto.request;
import com.example.jobmatrix.company.model.Industry;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCompanyRequest {

    private String name;

    private Industry industry;

    private String websiteUrl;

    private String description;

    private String headquarters;

    private Integer employeeCount;
}
