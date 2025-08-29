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
import com.example.printinfo.service.PrintService;

@RestController
@RequestMapping("/api/print-info")
@CrossOrigin(origins = "*")
public class PrintInfoController {

    @Autowired
    private PrintInfoService service;
    
    @Autowired
    private PrintService service2;    

    // 전체 목록
    @GetMapping("/list")
    public Map<String,Object> getList(
            @RequestParam(required = false) String pickingDateStart,
            @RequestParam(required = false) String pickingDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {
        List<PrintInfo> list = service2.getList(pickingDateStart, pickingDateEnd,
                                               printTeam, companyContact, itemName);
        Map<String,Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }
    
    // 전체 목록
    @GetMapping("/printList1")
    public Map<String,Object> getPrintAll1(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {
        List<PrintInfo> list = service2.searchPrints(orderDateStart, orderDateEnd, printTeam, companyContact, itemName);
        Map<String,Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }
    
    // 전체 목록
    @GetMapping("/logistic-list1")
    public Map<String,Object> getAll1(
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

        List<PrintInfo> list = service2.getAllPrintInfo1(orderDateStart, orderDateEnd,
                                                        printTeam, companyContact, itemName);
        Map<String,Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }

    // 피킹완료 목록
    @GetMapping("/logistic-list2")
    public Map<String,Object> getAll2(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {
        List<PrintInfo> list = service2.getAllPrintInfo2(orderDateStart, orderDateEnd,
                                                        printTeam, companyContact, itemName);
        Map<String,Object> response = new HashMap<>();
        response.put("data", list); // DataTables 기본 expects {data: [...]}
        return response;
    }
    
    // 출고준비 목록
    @GetMapping("/logistic-list3")
    public Map<String,Object> getAll3(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {
        List<PrintInfo> list = service2.getAllPrintInfo3(orderDateStart, orderDateEnd,
                                                        printTeam, companyContact, itemName);
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
    public ResponseEntity<String> updatePicking(@PathVariable int printId) {
        boolean updated = service.updatePickingStatus(printId); 
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
    public ResponseEntity<String> updateOutReady(@PathVariable int printId) {
        boolean updated = service.updateOutReadyStatus(printId); 
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

 // 인쇄 완료 처리
    @PostMapping("/{printId}/printEnd")
    public ResponseEntity<String> printEnd(@PathVariable int printId, @RequestParam String status) {
        boolean updated = service.updatePrintEnd(printId); 
        if (updated) {
            return ResponseEntity.ok("인쇄완료 처리 성공");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 데이터 없음");
        }
    }   

    // 인쇄완료 취소 처리 (New)
    @PostMapping("/{printId}/cancel-printEnd")
    public ResponseEntity<String> cancelPrintEnd(@PathVariable int printId) {
        boolean updated = service.cancelPrintEnd(printId);
        if (updated) {
            return ResponseEntity.ok("인쇄완료 취소 처리 성공");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 데이터 없음");
        }
    }    
}   