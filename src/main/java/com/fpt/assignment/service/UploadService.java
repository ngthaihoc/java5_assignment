package com.fpt.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class UploadService {
    @Autowired
    jakarta.servlet.ServletContext app;

    public String save(MultipartFile file, String folder) {
        String name = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            File dir = new File(app.getRealPath("/" + folder));
            if (!dir.exists()) dir.mkdirs();
            File savedFile = new File(dir, name);
            file.transferTo(savedFile);
            return name;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
