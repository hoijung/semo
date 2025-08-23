package com.example.printinfo.dao;

import com.example.printinfo.model.공통코드;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class 공통코드Repository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<공통코드> commonCodeRowMapper = new RowMapper<공통코드>() {
        @Override
        public 공통코드 mapRow(ResultSet rs, int rowNum) throws SQLException {
            공통코드 commonCode = new 공통코드();
            commonCode.set코드ID(rs.getInt("코드ID"));
            commonCode.set코드명(rs.getString("코드명"));
            commonCode.set코드그룹(rs.getString("코드그룹"));
            commonCode.set사용여부(rs.getBoolean("사용여부"));
            return commonCode;
        }
    };

    public List<공통코드> findAll() {
        String sql = "SELECT * FROM 공통코드";
        return jdbcTemplate.query(sql, commonCodeRowMapper);
    }

    public 공통코드 findById(int id) {
        String sql = "SELECT * FROM 공통코드 WHERE 코드ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, commonCodeRowMapper);
    }

    public List<공통코드> findByCodeGroup(String codeGroup) {
        String sql = "SELECT * FROM 공통코드 WHERE 코드그룹 = ? AND 사용여부 = 1";
        return jdbcTemplate.query(sql, new Object[]{codeGroup}, commonCodeRowMapper);
    }

    public int save(공통코드 commonCode) {
        if (commonCode.get코드ID() == 0) {
            // Insert
            String sql = "INSERT INTO 공통코드 (코드명, 코드그룹, 사용여부) VALUES (?, ?, ?)";
            return jdbcTemplate.update(sql, commonCode.get코드명(), commonCode.get코드그룹(), commonCode.is사용여부());
        } else {
            // Update
            String sql = "UPDATE 공통코드 SET 코드명 = ?, 코드그룹 = ?, 사용여부 = ? WHERE 코드ID = ?";
            return jdbcTemplate.update(sql, commonCode.get코드명(), commonCode.get코드그룹(), commonCode.is사용여부(), commonCode.get코드ID());
        }
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM 공통코드 WHERE 코드ID = ?";
        return jdbcTemplate.update(sql, id);
    }
}
