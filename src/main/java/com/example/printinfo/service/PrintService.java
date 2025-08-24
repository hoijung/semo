package com.example.printinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.printinfo.dao.PrintDto;
import com.example.printinfo.dao.PrintRepository;
import com.example.printinfo.model.PrintInfo;

@Service
public class PrintService {

    @Autowired
    private PrintRepository repository;

    public List<PrintDto> findAll() { return repository.findAll(); }

    // New search method for list.html
    public List<PrintDto> searchPrints(String pickingDateStart, String pickingDateEnd,
                                       String printTeam, String companyContact, String itemName) {
        return repository.findPrintsByCriteria(pickingDateStart, pickingDateEnd, printTeam, companyContact, itemName);
    }

    public PrintDto findById(Integer id) { return repository.findById(id); }
    public int insert(PrintDto dto) { return repository.insert(dto); }
    public int update(PrintDto dto) { return repository.update(dto); }
    public int delete(Integer id) { return repository.delete(id); }

    public PrintDto distributePrint(Integer id) {
        PrintDto dto = repository.findById(id);
        if (dto != null) {
            dto.set배분여부(true);
            repository.update(dto);
        }
        return dto;
    }

    public PrintDto cancelDistributePrint(Integer id) {
        PrintDto dto = repository.findById(id);
        if (dto == null) {
            // Handle not found case, e.g., throw an exception
            throw new IllegalArgumentException("Print record not found with ID: " + id);
        }

        if (dto.get피킹완료() != null && dto.get피킹완료()) {
            throw new IllegalStateException("피킹이 완료된 레코드는 배분을 취소할 수 없습니다.");
        }

        dto.set배분여부(false);
        repository.update(dto);
        return dto;
    }
    
    
// 전체 조회
 public List<PrintInfo> getAllPrintInfo1(String pickingDateStart, String pickingDateEnd,
                                         String printTeam, String companyContact, String itemName) {
     return repository.findAll1(pickingDateStart, pickingDateEnd, printTeam, companyContact, itemName);
 }
 
 // 전체 조회
 public List<PrintInfo> getList(String pickingDateStart, String pickingDateEnd,
                                String printTeam, String companyContact, String itemName) {
     return repository.getList(pickingDateStart, pickingDateEnd, printTeam, companyContact, itemName);
 }    

 // 전체 조회
 public List<PrintInfo> getAllPrintInfo2() {
     return repository.findAll2();
 }
 
 // 전체 조회
 public List<PrintInfo> getAllPrintInfo3() {
     return repository.findAll3();
 }
    
}
