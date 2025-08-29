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
 
	public boolean updatePickingStatus(int printId) {
		// TODO Auto-generated method stub
		repository.updatePickingYn(printId, true);
		return true;
	}   
    
	public boolean updateOutReadyStatus(int printId) {
		// TODO Auto-generated method stub
		repository.updateOutReadyYn(printId, true);
		return true;
	}
    

    public boolean cancelPickingStatus(int printId) {
        PrintInfo printInfo = repository.findById(printId);
        if (printInfo == null) {
            throw new IllegalArgumentException("Print record not found with ID: " + printId);
        }

        if (printInfo.getOutReadyYn().equals("1")) { //출고준비완료건 
            throw new IllegalArgumentException("출고준비 완료건은 피킹취소할 수 없습니다 " + printId);
        }
        // Assuming a method in repository to update picking status to false
        return repository.updatePickingYn(printId, false);
    }

	public boolean updatePrintEnd(int printId) {
        PrintInfo printInfo = repository.findById(printId);
        if (printInfo.getOutReadyYn().equals("1")) { //출고준비완료건 
            // throw new IllegalArgumentException("출고준비 완료건은 피킹취소할 수 없습니다 " + printId);
        }
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
            throw new IllegalArgumentException("출고준비완료건은 피킹취소 불가합니다 " + printId);
        }
        return repository.updateOutReadyYn(printId, false);
    }
}