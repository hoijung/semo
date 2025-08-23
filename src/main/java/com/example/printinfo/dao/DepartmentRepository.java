package com.example.printinfo.dao;

import com.example.printinfo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DepartmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Department> departmentRowMapper = new RowMapper<Department>() {
        @Override
        public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
            Department department = new Department();
            department.setDepartmentId(rs.getInt("부서ID"));
            department.setDepartmentName(rs.getString("부서명"));
            department.setClassificationNumber(rs.getString("분류번호"));
            department.setUseYn(rs.getBoolean("사용여부"));
            return department;
        }
    };

    public List<Department> findAll() {
        String sql = "SELECT * FROM 부서";
        return jdbcTemplate.query(sql, departmentRowMapper);
    }

    public Department findById(int id) {
        String sql = "SELECT * FROM 부서 WHERE 부서ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, departmentRowMapper);
    }

    public int save(Department department) {
        if (department.getDepartmentId() == 0) {
            // Insert
            String sql = "INSERT INTO 부서 (부서명, 분류번호, 사용여부) VALUES (?, ?, ?)";
            return jdbcTemplate.update(sql, department.getDepartmentName(), department.getClassificationNumber(), department.isUseYn());
        } else {
            // Update
            String sql = "UPDATE 부서 SET 부서명 = ?, 분류번호 = ?, 사용여부 = ? WHERE 부서ID = ?";
            return jdbcTemplate.update(sql, department.getDepartmentName(), department.getClassificationNumber(), department.isUseYn(), department.getDepartmentId());
        }
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM 부서 WHERE 부서ID = ?";
        return jdbcTemplate.update(sql, id);
    }
}