package com.example.jobmatrix.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudinaryUploadResponse {

    private String url;

    private String publicId;
}