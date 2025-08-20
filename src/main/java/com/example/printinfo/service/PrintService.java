package com.example.printinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.printinfo.dao.PrintDto;
import com.example.printinfo.dao.PrintRepository;

@Service
public class PrintService {

    @Autowired
    private PrintRepository repository;

    public List<PrintDto> findAll() { return repository.findAll(); }
    public PrintDto findById(Integer id) { return repository.findById(id); }
    public int insert(PrintDto dto) { return repository.insert(dto); }
    public int update(PrintDto dto) { return repository.update(dto); }
    public int delete(Integer id) { return repository.delete(id); }
}
