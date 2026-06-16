package com.example.jobmatrix.upload;

import lombok.Getter;

@Getter
public enum UploadFolder {

    RESUMES("jobmatrix/resumes"),

    COMPANY_LOGOS("jobmatrix/company-logos");

    private final String folder;

    UploadFolder(String folder) {
        this.folder = folder;
    }

}