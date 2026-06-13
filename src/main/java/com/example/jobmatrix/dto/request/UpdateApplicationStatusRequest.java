package com.example.jobmatrix.dto.request;

import com.example.jobmatrix.application.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateApplicationStatusRequest {

    @NotNull
    private ApplicationStatus status;
}