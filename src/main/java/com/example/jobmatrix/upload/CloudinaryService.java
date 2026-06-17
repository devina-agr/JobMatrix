package com.example.jobmatrix.upload;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.jobmatrix.dto.response.CloudinaryUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadFile(
            MultipartFile file,
            UploadFolder folder
    ) throws IOException {

        Map<?, ?> result =
                cloudinary.uploader()
                        .upload(
                                file.getBytes(),
                                ObjectUtils.asMap(
                                        "folder",
                                        folder.getFolder()
                                )
                        );

        return result.get("secure_url")
                .toString();
    }

    public CloudinaryUploadResponse uploadResume(
            MultipartFile file
    ) {

        try {

            String filename = file.getOriginalFilename();

            if (filename == null) {
                throw new RuntimeException("Invalid file");
            }

            String lowerFilename = filename.toLowerCase();

            if (!(lowerFilename.endsWith(".pdf")
                    || lowerFilename.endsWith(".doc")
                    || lowerFilename.endsWith(".docx"))) {

                throw new RuntimeException(
                        "Only PDF, DOC and DOCX files are allowed"
                );
            }

            Map<?, ?> result =
                    cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "resource_type", "raw",
                                    "folder", "jobmatrix/resumes",
                                    "public_id",
                                    java.util.UUID.randomUUID()
                                            + "_"
                                            + file.getOriginalFilename()
                            )
                    );

            return CloudinaryUploadResponse.builder()
                    .url(result.get("secure_url").toString())
                    .publicId(result.get("public_id").toString())
                    .build();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload resume",
                    e
            );
        }
    }

    public CloudinaryUploadResponse uploadCompanyLogo(
            MultipartFile file
    ) {

        try {

            Map<?, ?> result =
                    cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "resource_type",
                                    "image",
                                    "folder",
                                    "jobmatrix/company-logos"
                            )
                    );

            return CloudinaryUploadResponse.builder()
                    .url(
                            result.get("secure_url")
                                    .toString()
                    )
                    .publicId(
                            result.get("public_id")
                                    .toString()
                    )
                    .build();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload company logo",
                    e
            );
        }
    }

    public void deleteFile(
            String publicId
    ) {

        try {

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete file",
                    e
            );
        }
    }
}