package com.example.jobmatrix.upload;

public enum UploadFolder {

    RESUMES("jobmatrix/resumes"),

    COMPANY_LOGOS("jobmatrix/company-logos"),

    PROFILE_PICTURES("jobmatrix/profile-pictures");

    private final String folder;

    UploadFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}