package com.example.printinfo.service;

import com.example.printinfo.dao.DepartmentRepository;
import com.example.printinfo.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(int id) {
        return departmentRepository.findById(id);
    }

    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
    }
}