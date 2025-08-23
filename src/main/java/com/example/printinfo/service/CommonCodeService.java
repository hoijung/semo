package com.example.printinfo.service;

import com.example.printinfo.dao.CommonCodeRepository;
import com.example.printinfo.model.CommonCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonCodeService {

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    public List<CommonCode> getAllCommonCodes() {
        return commonCodeRepository.findAll();
    }

    public CommonCode getCommonCodeById(int id) {
        return commonCodeRepository.findById(id);
    }

    public List<CommonCode> getCommonCodesByGroup(String codeGroup) {
        return commonCodeRepository.findByCodeGroup(codeGroup);
    }

    public void saveCommonCode(CommonCode commonCode) {
        commonCodeRepository.save(commonCode);
    }

    public void deleteCommonCode(int id) {
        commonCodeRepository.deleteById(id);
    }
}