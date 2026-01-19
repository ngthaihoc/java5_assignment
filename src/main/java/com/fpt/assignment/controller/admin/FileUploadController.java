package com.fpt.assignment.controller.admin;

import java.io.File;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    @PostMapping("/admin/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File rỗng");
        }

        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destination = new File(uploadDir, fileName);
            file.transferTo(destination);

            // Trả về URL ảnh để gán vào input hidden
            return ResponseEntity.ok("/uploads/" + fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload thất bại");
        }
    }
}
