package edu.icet.book.book_network.services.impl;

import edu.icet.book.book_network.entity.Book;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl {
    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @NonNull MultipartFile sourceFile,
            @NonNull Integer userId) {
        final String fileUploadSubPath = "users" + File.separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(@NonNull MultipartFile sourceFile,@NonNull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File file = new File(finalUploadPath);
        if (!file.exists()) {
            boolean isCreated = file.mkdirs();
            if (!isCreated) {
                log.warn("Unable to create directory");
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = fileUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path path = Paths.get(targetFilePath);
        try {
            Files.write(path, sourceFile.getBytes());
            log.info("File uploaded successfully");
            return targetFilePath;
        }
        catch (IOException e) {
            log.error("Unable to write file", e);
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return null;
        }
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex == -1) {
            return null;
        }
        return originalFilename.substring(dotIndex + 1).toLowerCase();
    }
}
