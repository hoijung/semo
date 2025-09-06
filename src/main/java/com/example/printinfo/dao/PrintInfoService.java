package com.example.printinfo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.printinfo.model.PrintInfo;

import ch.qos.logback.core.util.StringUtil;


@Service
public class PrintInfoService {

    @Autowired
    private PrintRepository repository;
    
    public PrintInfo getPrintInfoById(int printId) {
        return repository.findById(printId);
    }
 
	@Transactional
	public void updatePickingEnd(int printId) {
		repository.updatePickingEnd(printId);
	}   
    
	@Transactional
	public void updateOutReadyEnd(int printId) {
		repository.updateOutReadyEnd(printId, true);
	}

    	@Transactional
	public void cancelOutReadyEnd(int printId) {
		repository.cancelOutReadyEnd(printId, true);
	}
    
	@Transactional
    public void cancelPickingEnd(int printId) {
        PrintInfo printInfo = repository.findById(printId);
        if (printInfo == null) {
            throw new IllegalArgumentException("인쇄 정보를 찾을 수 없습니다. ID: " + printId);
        }

        if (StringUtil.nullStringToEmpty(printInfo.getOutReadyYn()).equals("true")) { //출고준비완료건 
            throw new IllegalArgumentException("출고준비 완료 건은 피킹취소할 수 없습니다. ID: " + printId);
        }
        repository.cancelPickingEnd(printId);
    }

	@Transactional
	public void updatePrintEnd(int printId) {
        PrintInfo printInfo = repository.findById(printId);
        if (printInfo == null) {
            throw new IllegalArgumentException("인쇄 정보를 찾을 수 없습니다. ID: " + printId);
        }

        // '피킹완료' 상태가 'true'가 아닌 경우 예외를 발생시킵니다.
        if (!"true".equals(printInfo.getPickingYn())) { 
             throw new IllegalArgumentException("피킹 미완료 건은 인쇄작업완료 할 수 없습니다. ID: " + printId);
        }
		repository.updatePrintEnd(printId);
	}    
    
	@Transactional
    public void cancelPrintEnd(int printId) {
        // 필요 시 여기에 유효성 검사 로직을 추가할 수 있습니다.
        // 예: 인쇄 완료된 항목만 취소 가능
        repository.cancelUpdatePrintEnd(printId); // Repository 메소드 이름 오타 수정: cancle -> cancel
    }    

	@Transactional
    public void cancelOutReadyStatus(int printId) {
        PrintInfo printInfo = repository.findById(printId);
        if (printInfo == null) {
            throw new IllegalArgumentException("인쇄 정보를 찾을 수 없습니다. ID: " + printId);
        }
        repository.cancelOutReadyEnd(printId, false);
    }
}