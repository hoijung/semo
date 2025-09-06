package com.example.printinfo.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // Added import
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.printinfo.dao.PrintDto;
import com.example.printinfo.model.PrintInfo;
import com.example.printinfo.service.FileStorageService;
import com.example.printinfo.service.PrintService;

@RestController
@RequestMapping("/api/prints")
public class PrintController {

    @Autowired
    private PrintService service;

    // 파일 저장을 처리할 서비스 (아래에 추가 설명)
    @Autowired(required = false)
    private FileStorageService fileStorageService;

    @GetMapping
    public List<PrintDto> getAll() {
        return service.findAll();
    }

    // New search endpoint for list.html
    @GetMapping("/search")
    public Map<String, Object> searchPrints(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {

        System.out.println("pickingDateStartss: " + orderDateStart);
        System.out.println("pickingDateEnd: " + orderDateEnd);
        System.out.println("printTeam: " + printTeam);
        System.out.println("companyContact: " + companyContact);
        System.out.println("itemName: " + itemName);

        List<PrintInfo> list = service.searchPrints(orderDateStart, orderDateEnd, printTeam, companyContact, itemName);

        Map<String, Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }

    // 전체 목록
    @GetMapping("/printList1")
    public Map<String, Object> getPrintAll1(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {

        System.out.println("pickingDateStartss: " + orderDateStart);
        System.out.println("pickingDateEnd: " + orderDateEnd);
        System.out.println("printTeam: " + printTeam);
        System.out.println("companyContact: " + companyContact);
        System.out.println("itemName: " + itemName);

        List<PrintInfo> list = service.searchPrints(orderDateStart, orderDateEnd, printTeam, companyContact, itemName);
        Map<String, Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }

    @GetMapping("/{id}")
    public PrintDto getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public PrintDto create(@RequestPart("dto") PrintDto dto,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) throws IOException {
        // 파일이 존재하고, 파일 저장 서비스가 구현되어 있을 경우
        if (fileStorageService != null && logoFile != null && !logoFile.isEmpty()) {
            String fileName = fileStorageService.storeFile(logoFile);
            dto.set인쇄로고예시(fileName); // DTO에 저장된 파일명 설정
        }
        service.insert(dto);
        return dto;
    }

    @PutMapping("/{id}")
    public PrintDto update(@PathVariable Integer id,
            @RequestPart("dto") PrintDto dto,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) throws IOException {
        // 새 파일이 업로드된 경우
        if (fileStorageService != null && logoFile != null && !logoFile.isEmpty()) {
            // 기존 파일 삭제 로직 (선택 사항)
            PrintDto existingDto = service.findById(id);
            if (existingDto != null && existingDto.get인쇄로고예시() != null) {
                fileStorageService.deleteFile(existingDto.get인쇄로고예시());
            }
            String fileName = fileStorageService.storeFile(logoFile);
            dto.set인쇄로고예시(fileName); // DTO에 새로운 파일명 설정
        }

        dto.set인쇄ID(id);
        service.update(dto);
        return dto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>  delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(id);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/distribute")
    public PrintDto distributePrint(@PathVariable Integer id) {
        return service.distributePrint(id);
    }

    @PostMapping("/{id}/cancelDistribute")
    public ResponseEntity<?> cancelDistributePrint(@PathVariable Integer id) {
        try {
            PrintDto updatedDto = service.cancelDistributePrint(id);
            return ResponseEntity.ok(updatedDto);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
