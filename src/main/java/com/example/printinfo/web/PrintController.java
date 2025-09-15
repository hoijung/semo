package com.example.printinfo.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.printinfo.dao.PrintDto;
import com.example.printinfo.model.PrintInfo;
import com.example.printinfo.service.FileStorageService;
import com.example.printinfo.service.PrintService;
import com.popbill.api.IssueResponse;
import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.Taxinvoice;

@RestController
@RequestMapping("/api/prints")
public class PrintController {

    @Autowired
    private PrintService service;

    @Autowired
    private TaxinvoiceService taxinvoiceService;

    @Autowired(required = false)
    private FileStorageService fileStorageService;

    @Value("${popbill.link-id}")
    private String linkId;

    @Value("${popbill.secret-key}")
    private String secretKey;

    @GetMapping
    public List<PrintDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/search")
    public Map<String, Object> searchPrints(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {

        List<PrintInfo> list = service.search(orderDateStart, orderDateEnd, printTeam, companyContact, itemName);

        Map<String, Object> response = new HashMap<>();
        response.put("data", list);
        return response;
    }

    @GetMapping("/printList1")
    public Map<String, Object> getPrintAll1(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {

        List<PrintInfo> list = service.searchPrints(orderDateStart, orderDateEnd, printTeam, companyContact, itemName);
        Map<String, Object> response = new HashMap<>();
        response.put("data", list);
        return response;
    }

    @GetMapping("/{id}")
    public PrintDto getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public PrintDto create(@RequestPart("dto") PrintDto dto,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) throws IOException {
        if (fileStorageService != null && logoFile != null && !logoFile.isEmpty()) {
            String fileName = fileStorageService.storeFile(logoFile);
            dto.set인쇄로고예시(fileName);
        }
        service.insert(dto);
        return dto;
    }

    @PutMapping("/{id}")
    public PrintDto update(@PathVariable Integer id,
            @RequestPart("dto") PrintDto dto,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) throws IOException {
        if (fileStorageService != null && logoFile != null && !logoFile.isEmpty()) {
            PrintDto existingDto = service.findById(id);
            if (existingDto != null && existingDto.get인쇄로고예시() != null) {
                fileStorageService.deleteFile(existingDto.get인쇄로고예시());
            }
            String fileName = fileStorageService.storeFile(logoFile);
            dto.set인쇄로고예시(fileName);
        }

        dto.set인쇄ID(id);
        service.update(dto);
        return dto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
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

    @PostMapping("/issue-tax-invoice")
    public ResponseEntity<?> issueTaxInvoice(@RequestBody Taxinvoice taxinvoice) {
        try {
            String mgtKey = taxinvoice.getInvoicerCorpNum() + "-" + taxinvoice.getWriteDate() + "-" + System.currentTimeMillis();
            IssueResponse response = taxinvoiceService.registIssue(taxinvoice.getInvoicerCorpNum(), taxinvoice);
            return ResponseEntity.ok(response);
        } catch (PopbillException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/billing-list")
    public Map<String, Object> getBillingList(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd) {

        List<PrintInfo> list = service.search(orderDateStart, orderDateEnd, null, null, null);

        Map<String, Object> response = new HashMap<>();
        response.put("data", list);
        return response;
    }
}