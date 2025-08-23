package com.example.printinfo.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.printinfo.dao.PrintInfoService;
import com.example.printinfo.model.PrintInfo;

@RestController
@RequestMapping("/api/print-info")
@CrossOrigin(origins = "*")
public class PrintInfoController {

    @Autowired
    private PrintInfoService service;

    // 전체 목록
    @GetMapping("/list")
    public Map<String,Object> getList() {
        List<PrintInfo> list = service.getList();
        Map<String,Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }
    
    // 전체 목록
    @GetMapping("/printList1")
    public Map<String,Object> getPrintAll1() {
        List<PrintInfo> list = service.getAllPrintInfo1(null, null, null, null, null);
        Map<String,Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }
    
    // 전체 목록
    @GetMapping("/list1")
    public Map<String,Object> getAll1(
            @RequestParam(required = false) String pickingDateStart,
            @RequestParam(required = false) String pickingDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {
        List<PrintInfo> list = service.getAllPrintInfo1(pickingDateStart, pickingDateEnd,
                                                        printTeam, companyContact, itemName);
        Map<String,Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }

    // 전체 목록
    @GetMapping("/list2")
    public Map<String,Object> getAll2() {
        List<PrintInfo> list = service.getAllPrintInfo2();
        Map<String,Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }
    
    // 전체 목록
    @GetMapping("/list3")
    public Map<String,Object> getAll3() {
        List<PrintInfo> list = service.getAllPrintInfo3();
        Map<String,Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }
    
    // 단건 조회
    @GetMapping("/{id}")
    public PrintInfo getById(@PathVariable int id) {
        return service.getPrintInfoById(id);
    }
    
    // 단건 조회
    @GetMapping("/{id}/detail")
    public Map<String,Object> getByIdDetail(@PathVariable int id) throws IOException {
    	
    	Map<String,Object> response = new HashMap<>();
    	
    	response.put("info",service.getPrintInfoById(id));
    	 
        return response;
    }    
    
 // 피킹 완료 처리
    @PostMapping("/{printId}/picking")
    public ResponseEntity<String> updatePicking(@PathVariable int printId, @RequestParam String status) {
        boolean updated = service.updatePickingStatus(printId, status); 
        if (updated) {
            return ResponseEntity.ok("피킹 완료 처리 성공");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 데이터 없음");
        }
    }   

    // 피킹 취소 처리 (New)
    @PostMapping("/{printId}/cancel-picking")
    public ResponseEntity<String> cancelPicking(@PathVariable int printId) {
        boolean updated = service.cancelPickingStatus(printId);
        if (updated) {
            return ResponseEntity.ok("피킹 취소 처리 성공");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 데이터 없음");
        }
    }

    // 출고준비 완료 처리
    @PostMapping("/{printId}/out-ready")
    public ResponseEntity<String> updateOutReady(@PathVariable int printId, @RequestParam String status) {
        boolean updated = service.updateOutReadyStatus(printId, status); 
        if (updated) {
            return ResponseEntity.ok("출고준비 완료 처리 성공");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 데이터 없음");
        }
    }

    // 출고준비 취소 처리 (New)
    @PostMapping("/{printId}/cancel-out-ready")
    public ResponseEntity<String> cancelOutReady(@PathVariable int printId) {
        try {
            boolean updated = service.cancelOutReadyStatus(printId);
            if (updated) {
                return ResponseEntity.ok("출고준비 취소 처리 성공");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 데이터 없음");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}   