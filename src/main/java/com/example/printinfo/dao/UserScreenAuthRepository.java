package com.example.printinfo.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.printinfo.model.UserScreenAuthDto;

@Repository
public class UserScreenAuthRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<UserScreenAuthDto> rowMapper = (rs, rowNum) -> {
        UserScreenAuthDto auth = new UserScreenAuthDto();
        auth.setUserId(rs.getString("아이디"));
        auth.setScreenId(rs.getString("화면id"));
        auth.setCanCreate(rs.getString("등록"));
        auth.setCanUpdate(rs.getString("수정"));
        auth.setCanDelete(rs.getString("삭제"));
        return auth;
    };

    public List<UserScreenAuthDto> findByUserId(String userId) {
        String sql = "SELECT * FROM 사용자화면권한 WHERE 아이디 = ?";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    public void saveAll(List<UserScreenAuthDto> auths) {
        if (auths == null || auths.isEmpty()) {
            return;
        }

        String userId = auths.get(0).getUserId();
        // jdbcTemplate.update("DELETE FROM 사용자화면권한 WHERE 아이디 = ?", userId);

        jdbcTemplate.batchUpdate(
            "INSERT INTO 사용자화면권한 (아이디, 화면id, 등록, 수정, 삭제) VALUES (?, ?, ?, ?, ?)",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    UserScreenAuthDto auth = auths.get(i);
                    ps.setString(1, auth.getUserId());
                    ps.setString(2, auth.getScreenId());
                    ps.setString(3, auth.getCanCreate());
                    ps.setString(4, auth.getCanUpdate());
                    ps.setString(5, auth.getCanDelete());
                }

                @Override
                public int getBatchSize() {
                    return auths.size();
                }
            }
        );
    }
}
