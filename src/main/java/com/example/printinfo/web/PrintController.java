package com.example.printinfo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.printinfo.dao.PrintDto;
import com.example.printinfo.service.PrintService;

@RestController
@RequestMapping("/api/prints")
public class PrintController {

    @Autowired
    private PrintService service;

    @GetMapping
    public List<PrintDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PrintDto getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public PrintDto create(@RequestBody PrintDto dto) {
        service.insert(dto);
        return dto;
    }

    @PutMapping("/{id}")
    public PrintDto update(@PathVariable Integer id, @RequestBody PrintDto dto) {
        dto.set인쇄ID(id);
        service.update(dto);
        return dto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
