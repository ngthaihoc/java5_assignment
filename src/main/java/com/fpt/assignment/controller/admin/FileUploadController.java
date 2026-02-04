package com.fpt.assignment.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class FileUploadController {

    @PostMapping("admin/books/upload-image")
    @ResponseBody
    public String handleImageUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty())
            return "default-book.png";

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path targetPath = Paths.get("target/classes/static/images/books/" + fileName);
        Path srcPath = Paths.get("src/main/resources/static/images/books/" + fileName);

        Files.createDirectories(targetPath.getParent());
        Files.createDirectories(srcPath.getParent());

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(file.getInputStream(), srcPath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
