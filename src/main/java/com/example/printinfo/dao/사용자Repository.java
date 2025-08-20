package com.example.printinfo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.printinfo.model.PrintInfo;
import com.example.printinfo.model.사용자Dto;

@Repository
public class 사용자Repository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public 사용자Dto findBy아이디(String 아이디, String 비밀번호) {
		String sql = "SELECT * FROM 사용자 WHERE 아이디 = ? AND 비밀번호 = ? ";

		try {
			return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				사용자Dto info = new 사용자Dto();
				info.set사용자명(rs.getString("사용자명"));
				info.set아이디(rs.getString("아이디"));
				info.set비밀번호(rs.getString("비밀번호"));
				info.set사용여부(rs.getString("사용여부"));
				info.set사용자ID(rs.getString("사용자ID"));
				return info;
			}, 아이디 // 바인딩 파라미터
					, 비밀번호);
		} catch (EmptyResultDataAccessException e) {
			return null; // 또는 Optional.empty()
		}
	}

	public boolean updatePickingYn(int printId, boolean status) {
		String sql = "UPDATE 인쇄정보 " + "SET 피킹완료 = 1 " + "WHERE 인쇄ID = ?";

		int updated = jdbcTemplate.update(sql, printId);
		return updated > 0;
	}

	public boolean updateOutReadyYn(int printId, boolean status) {
		String sql = "UPDATE 인쇄정보 " + "SET 출고준비 = 1 " + "WHERE 인쇄ID = ?";

		int updated = jdbcTemplate.update(sql, printId);
		return updated > 0;
	}
}
