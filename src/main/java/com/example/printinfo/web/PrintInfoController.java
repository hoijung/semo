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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.printinfo.dao.ColorDataDto;
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
    public void updatePicking(@PathVariable int printId) {
        service.updatePickingEnd(printId); 
    }   

    // 피킹 취소 처리 (New)
    @PostMapping("/{printId}/cancel-picking")
    public void cancelPicking(@PathVariable int printId) {
        service.cancelPickingEnd(printId);
    }

    // 출고준비 완료 처리
    @PostMapping("/{printId}/out-ready")
    public void updateOutReady(@PathVariable int printId) {
        service.updateOutReadyEnd(printId);
    }

    // 출고준비 취소 처리 (New)
    @PostMapping("/{printId}/cancel-out-ready")
    public void cancelOutReady(@PathVariable int printId) {
        
            service.cancelOutReadyEnd(printId);
    }

 // 인쇄 완료 처리
    @PostMapping("/{printId}/printEnd")
    public void printEnd(@PathVariable int printId) {
         service.updatePrintEnd(printId); 
    }   

    // 인쇄완료 취소 처리 (New)
    @PostMapping("/{printId}/cancel-printEnd")
    public void cancelPrintEnd(@PathVariable int printId) {
         service.cancelPrintEnd(printId);
    }    

    @PostMapping("/update-color-data")
    public ResponseEntity<Void> updateColorData(@RequestBody ColorDataDto colorData) {
        service.updateColorData(colorData);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}