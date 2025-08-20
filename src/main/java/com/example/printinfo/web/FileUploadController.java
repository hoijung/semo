package com.example.printinfo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {

    // 파일을 저장할 디렉토리 (프로젝트 내 또는 외부 경로 지정 가능)
    // 실제 운영 환경에서는 안전한 외부 경로를 사용하는 것이 좋습니다.
    private final String UPLOAD_DIR = "C:\\Project\\access-printinfo-grid\\backend\\src\\main\\resources\\static\\File\\";

    @PostMapping("/uploadfile") // 요청 경로를 /uploadfile로 지정 (PHP의 uploadfile.php와 유사)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("업로드할 파일이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            // 업로드 디렉토리가 없으면 생성
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            // 파일명 중복 방지를 위해 UUID 등을 사용하는 것이 좋습니다.
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.copy(file.getInputStream(), filePath);

            // 파일 업로드 성공 메시지
            return new ResponseEntity<>("파일 '" + fileName + "'이 성공적으로 업로드되었습니다.", HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("파일 업로드 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
