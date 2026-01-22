package com.fpt.assignment.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class UploadService {


    public File save(MultipartFile file, String folderPath) {
        if (file.isEmpty()) {
            return null;
        }

        File dir = new File(folderPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = file.getOriginalFilename();
        File savedFile = new File(dir, filename);

        try {

            file.transferTo(savedFile);
            System.out.println("Đã lưu file tại: " + savedFile.getAbsolutePath());
            return savedFile;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi lưu file: " + e.getMessage());
        }
    }
}