package com.example.printinfo.service;

import com.example.printinfo.dao.부서Repository;
import com.example.printinfo.model.부서;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class 부서Service {

    @Autowired
    private 부서Repository departmentRepository;

    public List<부서> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public 부서 getDepartmentById(int id) {
        return departmentRepository.findById(id);
    }

    public void saveDepartment(부서 department) {
        departmentRepository.save(department);
    }

    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
    }
}
