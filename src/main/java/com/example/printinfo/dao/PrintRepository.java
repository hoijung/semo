package com.example.printinfo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PrintRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PrintDto> findAll() {
        String sql = "SELECT 인쇄ID, 품목명, 쇼핑백색상, 사이즈, 제작장수, 인쇄담당팀, 인쇄면, 인쇄도수"
        		+ ", 인쇄방식, 로고인쇄크기, 로고인쇄위치, 특이사항, CONVERT(VARCHAR(10), 주문일자, 23) AS 주문일자"
        		+ ", 업체명_담당자, 고객ID, 전화번호"
        		+ ", 발송마감기한, 최종배송지_우편번호, 최종배송지_주소, 판매채널, 계산서발행타입, 상호명, 대표자명"
        		+ ", 이메일, 공급가액, 부가세액, 합계금액, 배분여부, 완료여부, 등록일시, 수정일시, 등록팀, 수정팀"
        		+ ", 피킹완료, 출고준비, 파일명, 인쇄로고예시, 피킹예정일, 배송타입, 박스규격, 기존주문여부, 인쇄방법"
                + ", 배송지주소상세, 로고인쇄색상, 조색데이터1, 조색데이터2, 조색데이터3 "
        		+ " FROM semo.dbo.인쇄정보 ORDER BY 인쇄ID DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    public PrintDto findById(Integer id) {
        String sql = "SELECT * FROM semo.dbo.인쇄정보 WHERE 인쇄ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> mapRow(rs));
    }

    public int insert(PrintDto dto) {
        String sql = "INSERT INTO semo.dbo.인쇄정보 (품목명, 쇼핑백색상, 사이즈, 제작장수, 인쇄담당팀, 특이사항, 고객ID, 주문일자, 완료여부) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                dto.get품목명(), dto.get쇼핑백색상(), dto.get사이즈(), dto.get제작장수(),
                dto.get인쇄담당팀(), dto.get특이사항(), dto.get고객ID(), dto.get주문일자(), dto.get완료여부());
    }

    public int update(PrintDto dto) {
        String sql = "UPDATE semo.dbo.인쇄정보 SET " +
                     "품목명=?, 쇼핑백색상=?, 사이즈=?, 제작장수=?, 인쇄담당팀=?, 인쇄면=?, 인쇄도수=?, 인쇄방식=?, " +
                     "로고인쇄크기=?, 로고인쇄위치=?, 특이사항=?, 주문일자=?, 업체명_담당자=?, 고객ID=?, 전화번호=?, " +
                     "발송마감기한=?, 최종배송지_우편번호=?, 최종배송지_주소=?, 판매채널=?, 계산서발행타입=?, 상호명=?, " +
                     "대표자명=?, 이메일=?, 공급가액=?, 부가세액=?, 합계금액=?, 배분여부=?, 완료여부=?, " +
                     "수정팀=?, 피킹완료=?, 출고준비=?, 파일명=?, 인쇄로고예시=?, 피킹예정일=?, 배송타입=?, 박스규격=?, " +
                     "기존주문여부=?, 인쇄방법=?, 배송지주소상세=? , 로고인쇄색상=?, 조색데이터1 =?, 조색데이터2 = ?, 조색데이터3 =?, " +
                     "수정일시=GETDATE() " +
                     "WHERE 인쇄ID=?";
        return jdbcTemplate.update(sql,
                dto.get품목명(), dto.get쇼핑백색상(), dto.get사이즈(), dto.get제작장수(), dto.get인쇄담당팀(),
                dto.get인쇄면(), dto.get인쇄도수(), dto.get인쇄방식(), dto.get로고인쇄크기(), dto.get로고인쇄위치(),
                dto.get특이사항(), dto.get주문일자(), dto.get업체명_담당자(), dto.get고객ID(), dto.get전화번호(),
                dto.get발송마감기한(), dto.get최종배송지_우편번호(), dto.get최종배송지_주소(), dto.get판매채널(),
                dto.get계산서발행타입(), dto.get상호명(), dto.get대표자명(), dto.get이메일(), dto.get공급가액(),
                dto.get부가세액(), dto.get합계금액(), dto.get배분여부(), dto.get완료여부(),
                dto.get수정팀(), dto.get피킹완료(), dto.get출고준비(), dto.get파일명(), dto.get인쇄로고예시(),
                dto.get피킹예정일(), dto.get배송타입(), dto.get박스규격(), dto.get기존주문여부(), dto.get인쇄방법(),
                dto.get배송지주소상세(), dto.get로고인쇄색상(), dto.get조색데이터1(), dto.get조색데이터2(), dto.get조색데이터3(),
                dto.get인쇄ID());
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM semo.dbo.인쇄정보 WHERE 인쇄ID=?";
        return jdbcTemplate.update(sql, id);
    }

    private PrintDto mapRow(ResultSet rs) throws SQLException {
        PrintDto dto = new PrintDto();
        dto.set인쇄ID(rs.getInt("인쇄ID"));
        dto.set품목명(rs.getString("품목명"));
        dto.set쇼핑백색상(rs.getString("쇼핑백색상"));
        dto.set사이즈(rs.getString("사이즈"));
        dto.set제작장수(rs.getInt("제작장수"));
        dto.set인쇄담당팀(rs.getString("인쇄담당팀"));
        dto.set인쇄면(rs.getString("인쇄면"));
        dto.set인쇄도수(rs.getString("인쇄도수"));
        dto.set인쇄방식(rs.getString("인쇄방식"));
        dto.set로고인쇄크기(rs.getString("로고인쇄크기"));
        dto.set로고인쇄위치(rs.getString("로고인쇄위치"));
        dto.set특이사항(rs.getString("특이사항"));
        dto.set주문일자(rs.getString("주문일자"));
        dto.set업체명_담당자(rs.getString("업체명_담당자"));
        dto.set고객ID(rs.getString("고객ID"));
        dto.set전화번호(rs.getString("전화번호"));
        dto.set발송마감기한(rs.getString("발송마감기한"));
        dto.set최종배송지_우편번호(rs.getString("최종배송지_우편번호"));
        dto.set최종배송지_주소(rs.getString("최종배송지_주소"));
        dto.set판매채널(rs.getString("판매채널"));
        dto.set계산서발행타입(rs.getString("계산서발행타입"));
        dto.set상호명(rs.getString("상호명"));
        dto.set대표자명(rs.getString("대표자명"));
        dto.set이메일(rs.getString("이메일"));
        dto.set공급가액(rs.getBigDecimal("공급가액"));
        dto.set부가세액(rs.getBigDecimal("부가세액"));
        dto.set합계금액(rs.getBigDecimal("합계금액"));
        dto.set배분여부(rs.getBoolean("배분여부"));
        dto.set완료여부(rs.getBoolean("완료여부"));
        dto.set등록팀(rs.getString("등록팀"));
        dto.set수정팀(rs.getString("수정팀"));
        dto.set피킹완료(rs.getBoolean("피킹완료"));
        dto.set출고준비(rs.getBoolean("출고준비"));
        dto.set파일명(rs.getString("파일명"));
        dto.set인쇄로고예시(rs.getString("인쇄로고예시"));
        dto.set피킹예정일(rs.getString("피킹예정일"));
        dto.set배송타입(rs.getString("배송타입"));
        dto.set박스규격(rs.getString("박스규격"));
        dto.set기존주문여부(rs.getBoolean("기존주문여부"));
        dto.set인쇄방법(rs.getString("인쇄방법"));
        dto.set배송지주소상세(rs.getString("배송지주소상세"));
        dto.set로고인쇄색상(rs.getString("로고인쇄색상"));
        dto.set조색데이터1(rs.getString("조색데이터1"));
        dto.set조색데이터2(rs.getString("조색데이터2"));
        dto.set조색데이터3(rs.getString("조색데이터3"));
        return dto;
    }
}

