package com.example.printinfo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    // application.properties 파일에 파일 저장 경로 설정 필요 (예: file.upload-dir=./uploads)
    public FileStorageService(@Value("${file.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("파일을 업로드할 디렉토리를 생성할 수 없습니다.", ex);
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String storedFileName = UUID.randomUUID().toString() + "." + extension;

        Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return storedFileName;
    }

    public void deleteFile(String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) return;
        Path targetPath = this.fileStorageLocation.resolve(fileName).normalize();
        Files.deleteIfExists(targetPath);
    }
}
