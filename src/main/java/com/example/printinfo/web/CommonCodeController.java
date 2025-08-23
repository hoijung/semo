package com.example.printinfo.web;

import com.example.printinfo.model.CommonCode;
import com.example.printinfo.service.CommonCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commoncodes")
public class CommonCodeController {

    @Autowired
    private CommonCodeService commonCodeService;

    @GetMapping
    public List<CommonCode> getAllCommonCodes() {
        return commonCodeService.getAllCommonCodes();
    }

    @GetMapping("/{id}")
    public CommonCode getCommonCodeById(@PathVariable int id) {
        return commonCodeService.getCommonCodeById(id);
    }

    @GetMapping("/group/{codeGroup}")
    public List<CommonCode> getCommonCodesByGroup(@PathVariable String codeGroup) {
        return commonCodeService.getCommonCodesByGroup(codeGroup);
    }

    @PostMapping
    public void createCommonCode(@RequestBody CommonCode commonCode) {
        commonCodeService.saveCommonCode(commonCode);
    }

    @PutMapping("/{id}")
    public void updateCommonCode(@PathVariable int id, @RequestBody CommonCode commonCode) {
        commonCode.setCodeId(id);
        commonCodeService.saveCommonCode(commonCode);
    }

    @DeleteMapping("/{id}")
    public void deleteCommonCode(@PathVariable int id) {
        commonCodeService.deleteCommonCode(id);
    }
}