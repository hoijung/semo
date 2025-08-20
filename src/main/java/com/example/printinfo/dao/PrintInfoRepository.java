package com.example.printinfo.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.printinfo.model.PrintInfo;
@Repository
public class PrintInfoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public PrintInfo findById(int printId) {
        String sql = "SELECT 인쇄ID AS printId, 인쇄방법 AS printMethod, 품목명 AS itemName, " +
                     "쇼핑백색상 AS bagColor, 사이즈 AS sizeText, 제작장수 AS quantity, " +
                     "인쇄담당팀 AS team, 인쇄면 AS sides , 피킹완료 AS pickingYn, 출고준비 outReadyYn, 인쇄로고예시 as printSample " +
                     "FROM 인쇄정보 WHERE 인쇄ID = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{printId}, (rs, rowNum) -> {
            PrintInfo info = new PrintInfo();
            info.setPrintId(rs.getLong("printId"));
            info.setPrintMethod(rs.getString("printMethod"));
            info.setItemName(rs.getString("itemName"));
            info.setBagColor(rs.getString("bagColor"));
            info.setSizeText(rs.getString("sizeText"));
            info.setQuantity(rs.getInt("quantity"));
            info.setTeam(rs.getString("team"));
            info.setSides(rs.getString("sides"));
            info.setPickingYn(rs.getString("pickingYn"));
            info.setOutReadyYn(rs.getString("outReadyYn"));
            info.setPrintSample(rs.getString("printSample"));
            return info;
        });
    }
    
    
    public boolean updatePickingYn(int printId, boolean status) {
        String sql = "UPDATE 인쇄정보 " +
                     "SET 피킹완료 = 1 " +
                     "WHERE 인쇄ID = ?";

        int updated = jdbcTemplate.update(sql, printId);
        return updated > 0;
    }
    
    public boolean updateOutReadyYn(int printId, boolean status) {
        String sql = "UPDATE 인쇄정보 " +
                     "SET 출고준비 = 1 " +
                     "WHERE 인쇄ID = ?";

        int updated = jdbcTemplate.update(sql, printId);
        return updated > 0;
    }    
    
    // 전체 조회
    public List<PrintInfo> getList() {
    	 String sql = "SELECT 인쇄ID, 인쇄방법, 품목명, 쇼핑백색상, 사이즈, 제작장수, " +
                 "인쇄담당팀, 인쇄면, 인쇄도수, 인쇄방식, 로고인쇄크기, 로고인쇄위치, " +
                 "특이사항, 주문일자, 업체명_담당자, 고객ID, 전화번호, 발송마감기한, " +
                 "최종배송지_우편번호, 최종배송지_주소, 판매채널, 계산서발행타입, 상호명, " +
                 "대표자명, 이메일, 공급가액, 부가세액, 합계금액, 배분여부, 완료여부, " +
                 "등록일시, 수정일시, 등록팀, 수정팀, 피킹완료, 출고준비, 파일명, 인쇄로고예시, 피킹예정일 " +
                 "FROM SEMO.dbo.인쇄정보 " + 
                 "WHERE 1 = 1 ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PrintInfo dto = new PrintInfo();
            dto.setPrintId(rs.getLong("인쇄ID"));
            dto.setPrintMethod(rs.getString("인쇄방법"));
            dto.setItemName(rs.getString("품목명"));
            dto.setBagColor(rs.getString("쇼핑백색상"));
            dto.setSize(rs.getString("사이즈"));
            dto.setQuantity(rs.getInt("제작장수"));
            dto.setPrintTeam(rs.getString("인쇄담당팀"));
            dto.setPrintSide(rs.getString("인쇄면"));
            dto.setPrintCount(rs.getString("인쇄도수"));
            dto.setPrintType(rs.getString("인쇄방식"));
            dto.setLogoSize(rs.getString("로고인쇄크기"));
            dto.setLogoPosition(rs.getString("로고인쇄위치"));
            dto.setRemarks(rs.getString("특이사항"));
            dto.setOrderDate(rs.getString("주문일자"));
            dto.setCompanyContact(rs.getString("업체명_담당자"));
            dto.setCustomerId(rs.getString("고객ID"));
            dto.setPhoneNumber(rs.getString("전화번호"));
            dto.setDeliveryDeadline(rs.getString("발송마감기한") );
            dto.setDeliveryZip(rs.getString("최종배송지_우편번호"));
            dto.setDeliveryAddress(rs.getString("최종배송지_주소"));
            dto.setSalesChannel(rs.getString("판매채널"));
            dto.setInvoiceType(rs.getString("계산서발행타입"));
            dto.setCompanyName(rs.getString("상호명"));
            dto.setRepresentativeName(rs.getString("대표자명"));
            dto.setEmail(rs.getString("이메일"));
            dto.setSupplyAmount(rs.getBigDecimal("공급가액"));
            dto.setTaxAmount(rs.getBigDecimal("부가세액"));
            dto.setTotalAmount(rs.getBigDecimal("합계금액"));
            dto.setAllocationStatus(rs.getBoolean("배분여부"));
            dto.setCompleted(rs.getBoolean("완료여부"));
            dto.setCreatedAt(rs.getTimestamp("등록일시") != null ? rs.getTimestamp("등록일시").toLocalDateTime() : null);
            dto.setUpdatedAt(rs.getTimestamp("수정일시") != null ? rs.getTimestamp("수정일시").toLocalDateTime() : null);
            dto.setCreatedBy(rs.getString("등록팀"));
            dto.setUpdatedBy(rs.getString("수정팀"));
            dto.setPickingYn(rs.getString("피킹완료"));
            dto.setOutReadyYn(rs.getString("출고준비"));
            dto.setFileName(rs.getString("파일명"));
            dto.setLogoSamplePath(rs.getString("인쇄로고예시"));
            dto.setPickingDate(rs.getString("피킹예정일") );
            return dto;
        });
    }   
    
    // 전체 조회
    public List<PrintInfo> findAll1() {
    	 String sql = "SELECT 인쇄ID, 인쇄방법, 품목명, 쇼핑백색상, 사이즈, 제작장수, " +
                 "인쇄담당팀, 인쇄면, 인쇄도수, 인쇄방식, 로고인쇄크기, 로고인쇄위치, " +
                 "특이사항, 주문일자, 업체명_담당자, 고객ID, 전화번호, 발송마감기한, " +
                 "최종배송지_우편번호, 최종배송지_주소, 판매채널, 계산서발행타입, 상호명, " +
                 "대표자명, 이메일, 공급가액, 부가세액, 합계금액, 배분여부, 완료여부, " +
                 "등록일시, 수정일시, 등록팀, 수정팀, 피킹완료, 출고준비, 파일명, 인쇄로고예시, 피킹예정일 " +
                 "FROM SEMO.dbo.인쇄정보 " + 
                 "WHERE 배분여부 = 1 ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PrintInfo dto = new PrintInfo();
            dto.setPrintId(rs.getLong("인쇄ID"));
            dto.setPrintMethod(rs.getString("인쇄방법"));
            dto.setItemName(rs.getString("품목명"));
            dto.setBagColor(rs.getString("쇼핑백색상"));
            dto.setSize(rs.getString("사이즈"));
            dto.setQuantity(rs.getInt("제작장수"));
            dto.setPrintTeam(rs.getString("인쇄담당팀"));
            dto.setPrintSide(rs.getString("인쇄면"));
            dto.setPrintCount(rs.getString("인쇄도수"));
            dto.setPrintType(rs.getString("인쇄방식"));
            dto.setLogoSize(rs.getString("로고인쇄크기"));
            dto.setLogoPosition(rs.getString("로고인쇄위치"));
            dto.setRemarks(rs.getString("특이사항"));
            dto.setOrderDate(rs.getString("주문일자") );
            dto.setCompanyContact(rs.getString("업체명_담당자"));
            dto.setCustomerId(rs.getString("고객ID"));
            dto.setPhoneNumber(rs.getString("전화번호"));
            dto.setDeliveryDeadline(rs.getString("발송마감기한"));
            dto.setDeliveryZip(rs.getString("최종배송지_우편번호"));
            dto.setDeliveryAddress(rs.getString("최종배송지_주소"));
            dto.setSalesChannel(rs.getString("판매채널"));
            dto.setInvoiceType(rs.getString("계산서발행타입"));
            dto.setCompanyName(rs.getString("상호명"));
            dto.setRepresentativeName(rs.getString("대표자명"));
            dto.setEmail(rs.getString("이메일"));
            dto.setSupplyAmount(rs.getBigDecimal("공급가액"));
            dto.setTaxAmount(rs.getBigDecimal("부가세액"));
            dto.setTotalAmount(rs.getBigDecimal("합계금액"));
            dto.setAllocationStatus(rs.getBoolean("배분여부"));
            dto.setCompleted(rs.getBoolean("완료여부"));
            dto.setCreatedAt(rs.getTimestamp("등록일시") != null ? rs.getTimestamp("등록일시").toLocalDateTime() : null);
            dto.setUpdatedAt(rs.getTimestamp("수정일시") != null ? rs.getTimestamp("수정일시").toLocalDateTime() : null);
            dto.setCreatedBy(rs.getString("등록팀"));
            dto.setUpdatedBy(rs.getString("수정팀"));
            dto.setPickingYn(rs.getString("피킹완료"));
            dto.setOutReadyYn(rs.getString("출고준비"));
            dto.setFileName(rs.getString("파일명"));
            dto.setLogoSamplePath(rs.getString("인쇄로고예시"));
            dto.setPickingDate(rs.getString("피킹예정일") );
            return dto;
        });
    }       
    
    // 전체 조회
    public List<PrintInfo> findAll2() {
    	 String sql = "SELECT 인쇄ID, 인쇄방법, 품목명, 쇼핑백색상, 사이즈, 제작장수, " +
                 "인쇄담당팀, 인쇄면, 인쇄도수, 인쇄방식, 로고인쇄크기, 로고인쇄위치, " +
                 "특이사항, 주문일자, 업체명_담당자, 고객ID, 전화번호, 발송마감기한, " +
                 "최종배송지_우편번호, 최종배송지_주소, 판매채널, 계산서발행타입, 상호명, " +
                 "대표자명, 이메일, 공급가액, 부가세액, 합계금액, 배분여부, 완료여부, " +
                 "등록일시, 수정일시, 등록팀, 수정팀, 피킹완료, 출고준비, 파일명, 인쇄로고예시, 피킹예정일 " +
                 "FROM SEMO.dbo.인쇄정보 " + 
                 "WHERE 배분여부 = 1 ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PrintInfo dto = new PrintInfo();
            dto.setPrintId(rs.getLong("인쇄ID"));
            dto.setPrintMethod(rs.getString("인쇄방법"));
            dto.setItemName(rs.getString("품목명"));
            dto.setBagColor(rs.getString("쇼핑백색상"));
            dto.setSize(rs.getString("사이즈"));
            dto.setQuantity(rs.getInt("제작장수"));
            dto.setPrintTeam(rs.getString("인쇄담당팀"));
            dto.setPrintSide(rs.getString("인쇄면"));
            dto.setPrintCount(rs.getString("인쇄도수"));
            dto.setPrintType(rs.getString("인쇄방식"));
            dto.setLogoSize(rs.getString("로고인쇄크기"));
            dto.setLogoPosition(rs.getString("로고인쇄위치"));
            dto.setRemarks(rs.getString("특이사항"));
            dto.setOrderDate(rs.getString("주문일자") );
            dto.setCompanyContact(rs.getString("업체명_담당자"));
            dto.setCustomerId(rs.getString("고객ID"));
            dto.setPhoneNumber(rs.getString("전화번호"));
            dto.setDeliveryDeadline(rs.getString("발송마감기한") );
            dto.setDeliveryZip(rs.getString("최종배송지_우편번호"));
            dto.setDeliveryAddress(rs.getString("최종배송지_주소"));
            dto.setSalesChannel(rs.getString("판매채널"));
            dto.setInvoiceType(rs.getString("계산서발행타입"));
            dto.setCompanyName(rs.getString("상호명"));
            dto.setRepresentativeName(rs.getString("대표자명"));
            dto.setEmail(rs.getString("이메일"));
            dto.setSupplyAmount(rs.getBigDecimal("공급가액"));
            dto.setTaxAmount(rs.getBigDecimal("부가세액"));
            dto.setTotalAmount(rs.getBigDecimal("합계금액"));
            dto.setAllocationStatus(rs.getBoolean("배분여부"));
            dto.setCompleted(rs.getBoolean("완료여부"));
            dto.setCreatedAt(rs.getTimestamp("등록일시") != null ? rs.getTimestamp("등록일시").toLocalDateTime() : null);
            dto.setUpdatedAt(rs.getTimestamp("수정일시") != null ? rs.getTimestamp("수정일시").toLocalDateTime() : null);
            dto.setCreatedBy(rs.getString("등록팀"));
            dto.setUpdatedBy(rs.getString("수정팀"));
            dto.setPickingYn(rs.getString("피킹완료"));
            dto.setOutReadyYn(rs.getString("출고준비"));
            dto.setFileName(rs.getString("파일명"));
            dto.setLogoSamplePath(rs.getString("인쇄로고예시"));
            dto.setPickingDate(rs.getString("피킹예정일"));
            return dto;
        });
    }   
    
    
    // 전체 조회
    public List<PrintInfo> findAll3() {
    	 String sql = "SELECT 인쇄ID, 인쇄방법, 품목명, 쇼핑백색상, 사이즈, 제작장수, " +
                 "인쇄담당팀, 인쇄면, 인쇄도수, 인쇄방식, 로고인쇄크기, 로고인쇄위치, " +
                 "특이사항, 주문일자, 업체명_담당자, 고객ID, 전화번호, 발송마감기한, " +
                 "최종배송지_우편번호, 최종배송지_주소, 판매채널, 계산서발행타입, 상호명, " +
                 "대표자명, 이메일, 공급가액, 부가세액, 합계금액, 배분여부, 완료여부, " +
                 "등록일시, 수정일시, 등록팀, 수정팀, 피킹완료, 출고준비, 파일명, 인쇄로고예시, 피킹예정일 " +
                 "FROM SEMO.dbo.인쇄정보 " + 
                 "WHERE 배분여부 = 1 " +
                 "and 피킹완료 = 1";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PrintInfo dto = new PrintInfo();
            dto.setPrintId(rs.getLong("인쇄ID"));
            dto.setPrintMethod(rs.getString("인쇄방법"));
            dto.setItemName(rs.getString("품목명"));
            dto.setBagColor(rs.getString("쇼핑백색상"));
            dto.setSize(rs.getString("사이즈"));
            dto.setQuantity(rs.getInt("제작장수"));
            dto.setPrintTeam(rs.getString("인쇄담당팀"));
            dto.setPrintSide(rs.getString("인쇄면"));
            dto.setPrintCount(rs.getString("인쇄도수"));
            dto.setPrintType(rs.getString("인쇄방식"));
            dto.setLogoSize(rs.getString("로고인쇄크기"));
            dto.setLogoPosition(rs.getString("로고인쇄위치"));
            dto.setRemarks(rs.getString("특이사항"));
            dto.setOrderDate(rs.getString("주문일자") );
            dto.setCompanyContact(rs.getString("업체명_담당자"));
            dto.setCustomerId(rs.getString("고객ID"));
            dto.setPhoneNumber(rs.getString("전화번호"));
            dto.setDeliveryDeadline(rs.getString("발송마감기한") );
            dto.setDeliveryZip(rs.getString("최종배송지_우편번호"));
            dto.setDeliveryAddress(rs.getString("최종배송지_주소"));
            dto.setSalesChannel(rs.getString("판매채널"));
            dto.setInvoiceType(rs.getString("계산서발행타입"));
            dto.setCompanyName(rs.getString("상호명"));
            dto.setRepresentativeName(rs.getString("대표자명"));
            dto.setEmail(rs.getString("이메일"));
            dto.setSupplyAmount(rs.getBigDecimal("공급가액"));
            dto.setTaxAmount(rs.getBigDecimal("부가세액"));
            dto.setTotalAmount(rs.getBigDecimal("합계금액"));
            dto.setAllocationStatus(rs.getBoolean("배분여부"));
            dto.setCompleted(rs.getBoolean("완료여부"));
            dto.setCreatedAt(rs.getTimestamp("등록일시") != null ? rs.getTimestamp("등록일시").toLocalDateTime() : null);
            dto.setUpdatedAt(rs.getTimestamp("수정일시") != null ? rs.getTimestamp("수정일시").toLocalDateTime() : null);
            dto.setCreatedBy(rs.getString("등록팀"));
            dto.setUpdatedBy(rs.getString("수정팀"));
            dto.setPickingYn(rs.getString("피킹완료"));
            dto.setOutReadyYn(rs.getString("출고준비"));
            dto.setFileName(rs.getString("파일명"));
            dto.setLogoSamplePath(rs.getString("인쇄로고예시"));
            dto.setPickingDate(rs.getString("피킹예정일"));
            return dto;
        });
    }     
}
