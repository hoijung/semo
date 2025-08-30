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
public class CommonCodeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<CommonCode> commonCodeRowMapper = new RowMapper<CommonCode>() {
        @Override
        public CommonCode mapRow(ResultSet rs, int rowNum) throws SQLException {
            CommonCode commonCode = new CommonCode();
            commonCode.setCodeId(rs.getInt("코드ID"));
            commonCode.setCodeName(rs.getString("코드명"));
            commonCode.setCodeGroup(rs.getString("코드그룹"));
            commonCode.setUseYn(rs.getBoolean("사용여부"));
            return commonCode;
        }
    };

    public List<CommonCode> findAll() {
        String sql = "SELECT * FROM 공통코드";
        return jdbcTemplate.query(sql, commonCodeRowMapper);
    }

    public CommonCode findById(int id) {
        String sql = "SELECT * FROM 공통코드 WHERE 코드ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, commonCodeRowMapper);
    }

    public List<CommonCode> findByCodeGroup(String codeGroup) {
        String sql = "SELECT * FROM 공통코드 WHERE 코드그룹 = ? AND 사용여부 = '1' ";
        return jdbcTemplate.query(sql, new Object[]{codeGroup}, commonCodeRowMapper);
    }

    public int save(CommonCode commonCode) {
        if (commonCode.getCodeId() == 0) {
            // Insert
            String sql = "INSERT INTO 공통코드 (코드명, 코드그룹, 사용여부) VALUES (?, ?, ?)";
            return jdbcTemplate.update(sql, commonCode.getCodeName(), commonCode.getCodeGroup(), commonCode.isUseYn());
        } else {
            // Update
            String sql = "UPDATE 공통코드 SET 코드명 = ?, 코드그룹 = ?, 사용여부 = ? WHERE 코드ID = ?";
            return jdbcTemplate.update(sql, commonCode.getCodeName(), commonCode.getCodeGroup(), commonCode.isUseYn(), commonCode.getCodeId());
        }
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM 공통코드 WHERE 코드ID = ?";
        return jdbcTemplate.update(sql, id);
    }
}