package com.example.jobmatrix.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/resume")
    public ResponseEntity<String> uploadResume(
            @RequestParam("file")
            MultipartFile file
    ) throws Exception {

        String url =
                cloudinaryService.uploadFile(
                        file,
                        UploadFolder.RESUMES
                );

        return ResponseEntity.ok(url);
    }

    @PostMapping("/company-logo")
    public ResponseEntity<String> uploadCompanyLogo(
            @RequestParam("file")
            MultipartFile file
    ) throws Exception {

        String url =
                cloudinaryService.uploadFile(
                        file,
                        UploadFolder.COMPANY_LOGOS
                );

        return ResponseEntity.ok(url);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam("file")
            MultipartFile file
    ) throws Exception {

        String url =
                cloudinaryService.uploadFile(
                        file,
                        UploadFolder.PROFILE_PICTURES
                );

        return ResponseEntity.ok(url);
    }
}