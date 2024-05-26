package com.odyssey.services.cloudinary;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(CloudinaryConfigProperties cloudinaryConfigProperties) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryConfigProperties.cloudName(),
                "api_key", cloudinaryConfigProperties.apiKey(),
                "api_secret", cloudinaryConfigProperties.apiSecret()
        ));
    }

    public String uploadImage(File file, String destination) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                "public_id", "Odyssey-DB/" + destination + "/" + UUID.randomUUID().toString().substring(0, 12)
        ));
        return uploadResult.get("url").toString();
    }

    public boolean deleteImageByUrl(String url) {
        try {
            String publicId = url.substring(url.indexOf("Odyssey-DB"));
            publicId = publicId.substring(0, publicId.indexOf("."));
            cloudinary.uploader().destroy(publicId, Map.of());
            return true;
        }
        catch (IOException io) {
            return false;
        }

    }
}
