package com.example.printinfo.dao;

import com.example.printinfo.model.부서;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class 부서Repository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<부서> departmentRowMapper = new RowMapper<부서>() {
        @Override
        public 부서 mapRow(ResultSet rs, int rowNum) throws SQLException {
            부서 department = new 부서();
            department.set부서ID(rs.getInt("부서ID"));
            department.set부서명(rs.getString("부서명"));
            department.set분류번호(rs.getString("분류번호"));
            department.set사용여부(rs.getBoolean("사용여부"));
            return department;
        }
    };

    public List<부서> findAll() {
        String sql = "SELECT * FROM 부서";
        return jdbcTemplate.query(sql, departmentRowMapper);
    }

    public 부서 findById(int id) {
        String sql = "SELECT * FROM 부서 WHERE 부서ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, departmentRowMapper);
    }

    public int save(부서 department) {
        if (department.get부서ID() == 0) {
            // Insert
            String sql = "INSERT INTO 부서 (부서명, 분류번호, 사용여부) VALUES (?, ?, ?)";
            return jdbcTemplate.update(sql, department.get부서명(), department.get분류번호(), department.is사용여부());
        } else {
            // Update
            String sql = "UPDATE 부서 SET 부서명 = ?, 분류번호 = ?, 사용여부 = ? WHERE 부서ID = ?";
            return jdbcTemplate.update(sql, department.get부서명(), department.get분류번호(), department.is사용여부(), department.get부서ID());
        }
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM 부서 WHERE 부서ID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
