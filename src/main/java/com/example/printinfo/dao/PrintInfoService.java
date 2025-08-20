package com.example.printinfo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.printinfo.model.PrintInfo;

@Service
public class PrintInfoService {

    @Autowired
    private PrintInfoRepository repository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PrintInfo getPrintInfoById(int printId) {
        return repository.findById(printId);
    }
        
 // 전체 조회
    public List<PrintInfo> getAllPrintInfo1() {
        return repository.findAll1();
    }
    
    // 전체 조회
    public List<PrintInfo> getList() {
        return repository.getList();
    }    

    // 전체 조회
    public List<PrintInfo> getAllPrintInfo2() {
        return repository.findAll2();
    }
    
    // 전체 조회
    public List<PrintInfo> getAllPrintInfo3() {
        return repository.findAll3();
    }
	public boolean updatePickingStatus(int printId, String status) {
		// TODO Auto-generated method stub
		repository.updatePickingYn(printId, true);
		return true;
	}   
    
	public boolean updateOutReadyStatus(int printId, String status) {
		// TODO Auto-generated method stub
		repository.updateOutReadyYn(printId, true);
		return true;
	}
	
}