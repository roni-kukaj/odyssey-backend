package com.odyssey.services.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileService {
    public static File convertFile(MultipartFile file) {
        File convFile = new File("src/main/resources/images/" + UUID.randomUUID().toString().substring(0, 5) + file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            convFile = null;
        }

        return convFile;
    }
}
