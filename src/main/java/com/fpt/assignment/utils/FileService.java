package com.fpt.assignment.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    private final String baseUploadDir = "C:/uploads/";


    public String saveFile(MultipartFile file, String subfolder) throws IOException {
        String uploadDir = baseUploadDir + subfolder + "/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        file.transferTo(new File(uploadDir + fileName));
        return fileName;
    }
}
