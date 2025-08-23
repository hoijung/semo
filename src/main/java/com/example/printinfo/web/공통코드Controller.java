package com.example.printinfo.web;

import com.example.printinfo.model.공통코드;
import com.example.printinfo.service.공통코드Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commoncodes")
public class 공통코드Controller {

    @Autowired
    private 공통코드Service commonCodeService;

    @GetMapping
    public List<공통코드> getAllCommonCodes() {
        return commonCodeService.getAllCommonCodes();
    }

    @GetMapping("/{id}")
    public 공통코드 getCommonCodeById(@PathVariable int id) {
        return commonCodeService.getCommonCodeById(id);
    }

    @GetMapping("/group/{codeGroup}")
    public List<공통코드> getCommonCodesByGroup(@PathVariable String codeGroup) {
        return commonCodeService.getCommonCodesByGroup(codeGroup);
    }

    @PostMapping
    public void createCommonCode(@RequestBody 공통코드 commonCode) {
        commonCodeService.saveCommonCode(commonCode);
    }

    @PutMapping("/{id}")
    public void updateCommonCode(@PathVariable int id, @RequestBody 공통코드 commonCode) {
        commonCode.set코드ID(id);
        commonCodeService.saveCommonCode(commonCode);
    }

    @DeleteMapping("/{id}")
    public void deleteCommonCode(@PathVariable int id) {
        commonCodeService.deleteCommonCode(id);
    }
}
