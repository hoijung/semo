package com.example.printinfo.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.printinfo.model.*;

@Repository
public class UserRepository {
 
	@Autowired
	private JdbcTemplate jdbcTemplate;


	/**
	 * 데이터베이스 결과를 UserDto 객체로 매핑하는 RowMapper입니다.
	 * 코드 중복을 줄이고 일관성을 유지하기 위해 사용됩니다.
	 */
	private final RowMapper<UserDto> userRowMapper = (rs, rowNum) -> {
		UserDto info = new UserDto();
		// DB의 int 타입을 DTO의 String 타입으로 변환합니다.
		info.setUserSeq(String.valueOf(rs.getInt("사용자ID")));
		info.setUserName(rs.getString("사용자명"));
		info.setUserId(rs.getString("아이디"));
		// 중요: 비밀번호를 평문으로 다루는 것은 보안상 매우 위험합니다. 실제 운영 환경에서는 반드시 해싱하여 저장하고 비교해야 합니다.
		info.setPassword(rs.getString("비밀번호"));
		// DB의 bit 타입을 DTO의 String 타입으로 변환합니다.
		info.setUseYn(String.valueOf(rs.getBoolean("사용여부")));
		info.setAuthority(rs.getString("권한"));
		return info;
	};

	/**
	 * 모든 사용자를 조회합니다. (Read - All)
	 */
	public List<UserDto> findAll() {
		String sql = "SELECT * FROM 사용자";
		return jdbcTemplate.query(sql, userRowMapper);
	}

	/**
	 * ID로 특정 사용자를 조회합니다. (Read - By ID)
	 * 사용자가 없을 경우 Optional.empty()를 반환합니다.
	 */
	public Optional<UserDto> findById(int id) {
		String sql = "SELECT * FROM 사용자 WHERE 사용자ID = ?";
		try {
			return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, id));
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	/**
	 * 새로운 사용자를 생성합니다. (Create)
	 * 생성된 사용자의 ID를 포함하여 UserDto 객체를 반환합니다.
	 */
	public UserDto create(UserDto user) {
		String sql = "INSERT INTO 사용자 (사용자명, 아이디, 비밀번호, 사용여부) VALUES (?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getUserId());
			ps.setString(3, user.getPassword()); // 보안 경고: 비밀번호는 해싱해야 합니다.
			ps.setBoolean(4, "1".equals(user.getUseYn()) || "true".equalsIgnoreCase(user.getUseYn()));
			return ps;
		}, keyHolder);

		if (keyHolder.getKey() != null) {
			user.setUserSeq(String.valueOf(keyHolder.getKey().intValue()));
		}
		return user;
	}

	/**
	 * 기존 사용자 정보를 수정합니다. (Update)
	 * 비밀번호 필드가 비어있지 않은 경우에만 비밀번호를 업데이트합니다.
	 */
	public int update(UserDto user) {
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			String sql = "UPDATE 사용자 SET 사용자명 = ?, 아이디 = ?, 비밀번호 = ?, 사용여부 = ? WHERE 사용자ID = ?";
			return jdbcTemplate.update(sql, user.getUserName(), user.getUserId(), user.getPassword(), "1".equals(user.getUseYn()) || "true".equalsIgnoreCase(user.getUseYn()), Integer.parseInt(user.getUserSeq()));
		} else {
			String sql = "UPDATE 사용자 SET 사용자명 = ?, 아이디 = ?, 사용여부 = ? WHERE 사용자ID = ?";
			return jdbcTemplate.update(sql, user.getUserName(), user.getUserId(), "1".equals(user.getUseYn()) || "true".equalsIgnoreCase(user.getUseYn()), Integer.parseInt(user.getUserSeq()));
		}
	}

	/**
	 * ID로 사용자를 삭제합니다. (Delete)
	 */
	public int deleteById(int id) {
		String sql = "DELETE FROM 사용자 WHERE 사용자ID = ?";
		return jdbcTemplate.update(sql, id);
	}

	// 기존 로그인 메소드 (개선됨)
	public UserDto findByUserIdAndPassword(String userId, String password) {
		String sql = "SELECT * FROM 사용자 WHERE 아이디 = ? AND 비밀번호 = ? ";

		try {
			return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				UserDto info = new UserDto();
				info.setUserName(rs.getString("사용자명"));
				info.setUserId(rs.getString("아이디"));
				info.setPassword(rs.getString("비밀번호"));
				info.setUseYn(rs.getString("사용여부"));
				info.setUserSeq(rs.getString("사용자ID"));
				info.setAuthority(rs.getString("권한"));
				return info;
			}, userId, password);
		} catch (EmptyResultDataAccessException e) {
			return null; // 또는 Optional.empty()
		}
	}
}