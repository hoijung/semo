package com.example.printinfo.service;

import com.example.printinfo.dao.공통코드Repository;
import com.example.printinfo.model.공통코드;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class 공통코드Service {

    @Autowired
    private 공통코드Repository commonCodeRepository;

    public List<공통코드> getAllCommonCodes() {
        return commonCodeRepository.findAll();
    }

    public 공통코드 getCommonCodeById(int id) {
        return commonCodeRepository.findById(id);
    }

    public List<공통코드> getCommonCodesByGroup(String codeGroup) {
        return commonCodeRepository.findByCodeGroup(codeGroup);
    }

    public void saveCommonCode(공통코드 commonCode) {
        commonCodeRepository.save(commonCode);
    }

    public void deleteCommonCode(int id) {
        commonCodeRepository.deleteById(id);
    }
}
