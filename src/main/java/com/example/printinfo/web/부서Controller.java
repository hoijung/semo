package com.example.printinfo.web;

import com.example.printinfo.model.부서;
import com.example.printinfo.service.부서Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class 부서Controller {

    @Autowired
    private 부서Service departmentService;

    @GetMapping
    public List<부서> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public 부서 getDepartmentById(@PathVariable int id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping
    public void createDepartment(@RequestBody 부서 department) {
        departmentService.saveDepartment(department);
    }

    @PutMapping("/{id}")
    public void updateDepartment(@PathVariable int id, @RequestBody 부서 department) {
        department.set부서ID(id);
        departmentService.saveDepartment(department);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
    }
}
