package com.example.printinfo.web;

import com.example.printinfo.model.Department;
import com.example.printinfo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable int id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping
    public void createDepartment(@RequestBody Department department) {
        departmentService.saveDepartment(department);
    }

    @PutMapping("/{id}")
    public void updateDepartment(@PathVariable int id, @RequestBody Department department) {
        department.setDepartmentId(id);
        departmentService.saveDepartment(department);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
    }
}