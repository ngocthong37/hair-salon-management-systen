package com.hairsalon.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class StorageService {
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dqqhlhhr9",
                "api_key", "788139511972494",
                "api_secret", "TVQm_6LL5d3zNouU0ouIFrQRDfE"));
        return cloudinary;
    }

    // Check file type: image png, jpg, jpeg, bmp
    public boolean isImage(MultipartFile file) {
        return Arrays.asList(new String[] {"image/png","image/jpg","image/jpeg","image/bmp"})
                .contains(Objects.requireNonNull(file.getContentType()).trim().toLowerCase());
    }

    // Upload image to cloudinary
    public String uploadImages(MultipartFile file, String namePath) {
        String imageUrl = "";
        try {
                String randomString = UUID.randomUUID().toString();
                String updatedNamePath = namePath + "/" + randomString;
                Map uploadResult = this.cloudinary().uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto", "public_id", updatedNamePath)
                );
                imageUrl = (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageUrl;
    }
}