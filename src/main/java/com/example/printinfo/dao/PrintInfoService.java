package com.example.printinfo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.printinfo.model.PrintInfo;

@Service
public class PrintInfoService {

    @Autowired
    private PrintRepository repository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PrintInfo getPrintInfoById(int printId) {
        return repository.findById(printId);
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

    public boolean cancelPickingStatus(int printId) {
        // Assuming a method in repository to update picking status to false
        return repository.updatePickingYn(printId, false);
    }

	public boolean updatePrintEnd(int printId, String status) {
		// TODO Auto-generated method stub
		repository.updatePrintEnd(printId, true);
		return true;
	}    
    
    public boolean cancelPrintEnd(int printId) {
        // Assuming a method in repository to update picking status to false
        return repository.updatePrintEnd(printId, false);
    }    


    public boolean cancelOutReadyStatus(int printId) {
        PrintInfo printInfo = repository.findById(printId);
        if (printInfo == null) {
            throw new IllegalArgumentException("Print record not found with ID: " + printId);
        }
        // Assuming 'outReadyYn' in PrintInfo model represents '출고완료'
        // and it's a String "1" for true, "0" for false
//        if ("1".equals(printInfo.getOutReadyYn())) { // Check if already "출고완료"
//            throw new IllegalStateException("출고완료된 레코드는 출고준비를 취소할 수 없습니다.");
//        }
        return repository.updateOutReadyYn(printId, false);
    }
}