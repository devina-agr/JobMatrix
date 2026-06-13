package com.example.jobmatrix.upload;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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

    public void deleteFile(
            String publicId
    ) throws IOException {

        cloudinary.uploader()
                .destroy(
                        publicId,
                        ObjectUtils.emptyMap()
                );
    }
}