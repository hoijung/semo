package com.example.printinfo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.printinfo.model.PrintInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PrintRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PrintDto> findAll() {
        String sql = "SELECT 인쇄ID, 품목명, 쇼핑백색상, 사이즈, 제작장수, 인쇄담당팀, 인쇄면, 인쇄도수"
                + ", 인쇄방식, 로고인쇄크기, 로고인쇄위치, 특이사항, 주문일자"
                + ", 업체명_담당자, 고객ID, 전화번호"
                + ", 발송마감기한, 최종배송지_우편번호, 최종배송지_주소, 판매채널, 계산서발행타입, 상호명, 대표자명"
                + ", 이메일, 공급가액, 부가세액, 합계금액, 배분여부, 완료여부, 등록일시, 수정일시, 등록팀, 수정팀"
                + ", 피킹완료, 출고준비, 파일명, 인쇄로고예시, 피킹예정일, 배송타입, 박스규격, 기존주문여부, 인쇄방법"
                + ", 배송지주소상세, 로고인쇄색상, 조색데이터1, 조색데이터2, 조색데이터3, 인쇄참고사항 "
                + ", 중요여부, 업체메모 "
                + ", CASE EXTRACT(DOW FROM  CASE  WHEN 주문일자 LIKE '%-%'  "
                + " THEN TO_DATE(주문일자, 'YYYY-MM-DD') ELSE TO_DATE(주문일자, 'YYYYMMDD') END  ) "
                + " WHEN 0 THEN '일' "
                + " WHEN 1 THEN '월' "
                + " WHEN 2 THEN '화' "
                + " WHEN 3 THEN '수' "
                + " WHEN 4 THEN '목' "
                + " WHEN 5 THEN '금' "
                + " WHEN 6 THEN '토' "
                + " END AS 요일 "
                + ", case when 출고준비 = 'true' then '출고' "
                + "    else case when 인쇄완료 = 'true' then '인쇄' "
            	+ "	  else case when 피킹완료 = 'true' then '피킹' "
            	+ "				else case when 배분여부='true' then '배분' end "
            	+ "		   end "
                + "	end "
                + "end as 상태 "
                + " FROM 인쇄정보 ORDER BY 주문일자 DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    public PrintDto findById(Integer id) {
        String sql = "SELECT a.*, '' 상태 FROM 인쇄정보 a WHERE 인쇄ID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> mapRow(rs));
    }

    public int insert(PrintDto dto) {
        String sql = "INSERT INTO 인쇄정보 (품목명, 쇼핑백색상, 사이즈, 제작장수, 인쇄담당팀, 특이사항, 고객ID, 주문일자, 완료여부) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                "", "", "", 0,
                "", "", "", "", "");
    }

    public int distributePrint(PrintDto dto) {
        String sql = "UPDATE 인쇄정보 SET " +
                "배분여부 = 'true' " +
                ",수정일시=NOW() " +
                "WHERE 인쇄ID=?";
        return jdbcTemplate.update(sql,
                dto.get인쇄ID());
    }

    public int cancelDistributePrint(PrintDto dto) {
        String sql = "UPDATE 인쇄정보 SET " +
                "배분여부 = 'false' " +
                ",수정일시=NOW() " +
                "WHERE 인쇄ID=?";
        return jdbcTemplate.update(sql,
                dto.get인쇄ID());
    }

    public int update(PrintDto dto) {
        String sql = "UPDATE 인쇄정보 SET " +
                "품목명=?, 쇼핑백색상=?, 사이즈=?, 제작장수=?, 인쇄담당팀=?, 인쇄면=?, 인쇄도수=?, 인쇄방식=?, " +
                "로고인쇄크기=?, 로고인쇄위치=?, 특이사항=?, 주문일자=?, 업체명_담당자=?, 고객ID=?, 전화번호=?, " +
                "발송마감기한=?, 최종배송지_우편번호=?, 최종배송지_주소=?, 판매채널=?, 계산서발행타입=?, 상호명=?, " +
                "대표자명=?, 이메일=?, 공급가액=?, 부가세액=?, 합계금액=?, " +
                "수정팀=?, 피킹완료=?, 출고준비=?, 파일명=?, 인쇄로고예시=?, 피킹예정일=?, 배송타입=?, 박스규격=?, " +
                "기존주문여부=?, 인쇄방법=?, 배송지주소상세=? , 로고인쇄색상=?, 조색데이터1 =?, 조색데이터2 = ?, 조색데이터3 =?, " +
                "인쇄참고사항=?, 중요여부=?,  업체메모=?, " +
                "수정일시=NOW() " +
                "WHERE 인쇄ID=?";
        return jdbcTemplate.update(sql,
                dto.get품목명(), dto.get쇼핑백색상(), dto.get사이즈(), dto.get제작장수(), dto.get인쇄담당팀(),
                dto.get인쇄면(), dto.get인쇄도수(), dto.get인쇄방식(), dto.get로고인쇄크기(), dto.get로고인쇄위치(),
                dto.get특이사항(), dto.get주문일자(), dto.get업체명_담당자(), dto.get고객ID(), dto.get전화번호(),
                dto.get발송마감기한(), dto.get최종배송지_우편번호(), dto.get최종배송지_주소(), dto.get판매채널(),
                dto.get계산서발행타입(), dto.get상호명(), dto.get대표자명(), dto.get이메일(), dto.get공급가액(),
                dto.get부가세액(), dto.get합계금액(),
                dto.get수정팀(), dto.get피킹완료(), dto.get출고준비(), dto.get파일명(), dto.get인쇄로고예시(),
                dto.get피킹예정일(), dto.get배송타입(), dto.get박스규격(), dto.get기존주문여부(), dto.get인쇄방법(),
                dto.get배송지주소상세(), dto.get로고인쇄색상(), dto.get조색데이터1(), dto.get조색데이터2(), dto.get조색데이터3(),
                dto.get인쇄참고사항(), dto.get중요여부(), dto.get업체메모(),
                dto.get인쇄ID());
    }

    public int delete(Integer id) {
        String sql = "DELETE FROM 인쇄정보 WHERE 인쇄ID=?";
        return jdbcTemplate.update(sql, id);
    }

    public int updateColorData(int printId, String colorData1, String colorData2, String colorData3, String printTeamPhoto) {
        String sql = "UPDATE 인쇄정보 SET 조색데이터1 = ?, 조색데이터2 = ?, 조색데이터3 = ?, 인쇄팀사진 = ?, 수정일시 = NOW() WHERE 인쇄ID = ?";
        return jdbcTemplate.update(sql, colorData1, colorData2, colorData3, printTeamPhoto, printId);
    }

    private PrintDto mapRow(ResultSet rs) throws SQLException {
        PrintDto dto = new PrintDto();
        dto.set인쇄ID(rs.getInt("인쇄ID"));
        dto.set품목명(rs.getString("품목명"));
        dto.set쇼핑백색상(rs.getString("쇼핑백색상"));
        dto.set사이즈(rs.getString("사이즈"));
        dto.set제작장수(rs.getString("제작장수"));
        dto.set인쇄담당팀(rs.getString("인쇄담당팀"));
        dto.set인쇄면(rs.getString("인쇄면"));
        dto.set인쇄도수(rs.getString("인쇄도수"));
        dto.set인쇄방식(rs.getString("인쇄방식"));
        dto.set로고인쇄크기(rs.getString("로고인쇄크기"));
        dto.set로고인쇄위치(rs.getString("로고인쇄위치"));
        dto.set특이사항(rs.getString("특이사항"));
        dto.set주문일자(rs.getString("주문일자"));
        dto.set업체명_담당자(rs.getString("업체명_담당자"));
        dto.set업체명담당자(rs.getString("업체명_담당자"));
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
        dto.set배분여부(rs.getString("배분여부"));
        dto.set완료여부(rs.getString("완료여부"));
        dto.set등록팀(rs.getString("등록팀"));
        dto.set수정팀(rs.getString("수정팀"));
        dto.set피킹완료(rs.getString("피킹완료"));
        dto.set출고준비(rs.getString("출고준비"));
        dto.set파일명(rs.getString("파일명"));
        dto.set인쇄로고예시(rs.getString("인쇄로고예시"));
        dto.set피킹예정일(rs.getString("피킹예정일"));
        dto.set배송타입(rs.getString("배송타입"));
        dto.set박스규격(rs.getString("박스규격"));
        dto.set기존주문여부(rs.getString("기존주문여부"));
        dto.set인쇄방법(rs.getString("인쇄방법"));
        dto.set배송지주소상세(rs.getString("배송지주소상세"));
        dto.set로고인쇄색상(rs.getString("로고인쇄색상"));
        dto.set조색데이터1(rs.getString("조색데이터1"));
        dto.set조색데이터2(rs.getString("조색데이터2"));
        dto.set조색데이터3(rs.getString("조색데이터3"));
        dto.set인쇄참고사항(rs.getString("인쇄참고사항"));
        dto.set중요여부(rs.getString("중요여부"));
        dto.set업체메모(rs.getString("업체메모"));
        dto.set상태(rs.getString("상태"));
        return dto;
    }

    // public PrintDto findById(Integer id) {
    // String sql = "SELECT * FROM 인쇄정보 WHERE 인쇄ID = ?";
    // return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) ->
    // mapRow(rs));
    // }

    public PrintInfo findById(int printId) {
        String sql = "SELECT * "
                + "FROM 인쇄정보 WHERE 인쇄ID = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] { printId }, (rs, rowNum) -> mapRowToPrintInfo2(rs));

        // return jdbcTemplate.queryForObject(sql, new Object[] { printId }, (rs,
        // rowNum) -> {
        // PrintInfo info = new PrintInfo();
        // info.setPrintId(rs.getLong("인쇄ID"));
        // info.setPrintMethod(rs.getString("printMethod"));
        // info.setItemName(rs.getString("itemName"));
        // info.setBagColor(rs.getString("bagColor"));
        // info.setSizeText(rs.getString("sizeText"));
        // info.setQuantity(rs.getInt("quantity"));
        // info.setTeam(rs.getString("team"));
        // info.setSides(rs.getString("sides"));
        // info.setPickingYn(rs.getString("pickingYn"));
        // info.setOutReadyYn(rs.getString("outReadyYn"));
        // info.setPrintSample(rs.getString("printSample"));
        // return info;
        // });
    }

    public boolean updatePickingEnd(int printId) {
        String sql = "UPDATE 인쇄정보 " +
                "SET 피킹완료 = 'true' \n" +
                ", 피킹완료일시 = NOW() \n " +
                "WHERE 인쇄ID = ?";

        int updated = jdbcTemplate.update(sql, printId);
        return updated > 0;
    }

    public boolean cancelPickingEnd(int printId) {
        String sql = "UPDATE 인쇄정보 " +
                "SET 피킹완료 = 'false' " +
                ", 피킹완료일시 = NULL " +
                "WHERE 인쇄ID = ?";

        int updated = jdbcTemplate.update(sql, printId);
        return updated > 0;
    }

    public boolean updatePrintEnd(int printId) {
        String sql = "UPDATE 인쇄정보 " +
                "SET 인쇄완료 = 'true' " +
                ", 인쇄완료일시 = NOW() \n " +
                "WHERE 인쇄ID = ?";

        int updated = jdbcTemplate.update(sql, printId);
        return updated > 0;
    }

    public boolean cancelUpdatePrintEnd(int printId) {
        String sql = "UPDATE 인쇄정보 " +
                "SET 인쇄완료 = 'false' " +
                ", 인쇄완료일시 = NULL " +
                "WHERE 인쇄ID = ?";

        int updated = jdbcTemplate.update(sql, printId);
        return updated > 0;
    }

    public boolean updateOutReadyEnd(int printId, boolean status) {
        String sql = "UPDATE 인쇄정보 " +
                "SET 출고준비 = 'true' " +
                ", 출고완료일시 = NOW() \n " +
                "WHERE 인쇄ID = ?";

        int updated = jdbcTemplate.update(sql,  printId);
        return updated > 0;
    }

    public boolean cancelOutReadyEnd(int printId, boolean status) {
        String sql = "UPDATE 인쇄정보 " +
                "SET 출고준비 = 'false' " +
                ", 출고완료일시 = null \n " +
                "WHERE 인쇄ID = ?";

        int updated = jdbcTemplate.update(sql,  printId);
        return updated > 0;
    }

    /**
     * 탭 1: 물류팀 - 전체 목록 조회 (피킹되지 않은 건)
     * 
     * @return 배분은 된 목록
     */
    public List<PrintInfo> findAllocatedPrints(String pickingDateStart, String pickingDateEnd) {
        String baseWhereClause = " WHERE 배분여부 = 'true' ";
        return findPrints(baseWhereClause, pickingDateStart, pickingDateEnd, "", "", "");
    }

    /**
     * 탭 2: 피킹완료 목록 조회 (출고준비되지 않은 건)
     * 
     * @return 피킹은 완료되었지만 아직 출고 준비가 되지 않은 작업 목록
     */
    public List<PrintInfo> findPickedPrints(String pickingDateStart, String pickingDateEnd) {
        String baseWhereClause = " WHERE 피킹완료 = 'true' ";
        return findPrints(baseWhereClause, pickingDateStart, pickingDateEnd, "", "", "");
    }

    /**
     * 탭 3: 출고준비 목록 조회
     * 
     * @return 출고 준비가 완료된 작업 목록
     */
    public List<PrintInfo> findReadyForDispatchPrints(String pickingDateStart, String pickingDateEnd) {
        String baseWhereClause = " WHERE 출고준비 = 'true' ";
        return findPrints(baseWhereClause, pickingDateStart, pickingDateEnd, "", "", "");
    }

    /**
     * [참고] 모든 인쇄 정보를 검색해야 할 경우 사용 (기존 findPrintsByCriteria 대체)
     */
    public List<PrintInfo> findAllPrints() {
        return findPrints("", "", "", "", "", "");
    }

    /**
     * [참고] 모든 인쇄 정보를 검색해야 할 경우 사용 (기존 findPrintsByCriteria 대체)
     */
    public List<PrintInfo> findAllPrints(String pickingDateStart, String pickingDateEnd, String printTeam,
            String companyContact, String itemName) {
        return findPrints(" WHERE 1=1 and 배분여부 = 'true' ", pickingDateStart, pickingDateEnd, printTeam, "", "");
    }

    /**
     * [리팩토링] 동적 쿼리 생성을 위한 공통 메소드
     */
    private List<PrintInfo> findPrints(String baseWhereClause, String pickingDateStart, String pickingDateEnd,
            String printTeam, String companyContact, String itemName) {
        String selectClause = "SELECT 인쇄ID, 품목명, 쇼핑백색상, 사이즈, 제작장수, 인쇄담당팀, 인쇄면, 인쇄도수"
                + ", 인쇄방식, 로고인쇄크기, 로고인쇄위치, 특이사항, 주문일자"
                + ", 업체명_담당자, 고객ID, 전화번호"
                + ", 발송마감기한, 최종배송지_우편번호, 최종배송지_주소, 판매채널, 계산서발행타입, 상호명, 대표자명"
                + ", 이메일, 공급가액, 부가세액, 합계금액, 배분여부, 완료여부, 등록일시, 수정일시, 등록팀, 수정팀"
                + ", 피킹완료, 출고준비, 파일명, 인쇄로고예시, 피킹예정일, 배송타입, 박스규격, 박스수량, 기존주문여부, 인쇄방법" // 중복된 배송타입 제거
                + ", 배송지주소상세, 로고인쇄색상, 조색데이터1, 조색데이터2, 조색데이터3, 인쇄완료, 중요여부, 업체메모, 인쇄참고사항 "
                + ", 조색데이터1, 조색데이터2, 조색데이터3 , 인쇄팀사진 "
                + ", to_char(피킹완료일시,'yyyy-mm-dd hh:mm:ss') 피킹완료일시, to_char(인쇄완료일시,'yyyy-mm-dd hh:mm:ss') 인쇄완료일시 , to_char(출고완료일시,'yyyy-mm-dd hh:mm:ss') 출고완료일시 "
                + ", CASE EXTRACT(DOW FROM  CASE  WHEN 주문일자 LIKE '%-%'  "                
                + " THEN TO_DATE(주문일자, 'YYYY-MM-DD') ELSE TO_DATE(주문일자, 'YYYYMMDD') END  ) "
                + " WHEN 0 THEN '일' "
                + " WHEN 1 THEN '월' "
                + " WHEN 2 THEN '화' "
                + " WHEN 3 THEN '수' "
                + " WHEN 4 THEN '목' "
                + " WHEN 5 THEN '금' "
                + " WHEN 6 THEN '토' "
                + " END AS 요일 ";

        StringBuilder sqlBuilder = new StringBuilder(selectClause)
                .append(" FROM 인쇄정보 ")
                .append(baseWhereClause);

        List<Object> params = new java.util.ArrayList<>();

        if (pickingDateStart != null && !pickingDateStart.isEmpty()) {
            sqlBuilder.append(" AND 주문일자 >= ?");
            params.add(pickingDateStart);
        }
        if (pickingDateEnd != null && !pickingDateEnd.isEmpty()) {
            sqlBuilder.append(" AND 주문일자 <= ?");
            params.add(pickingDateEnd);
        }
        if (printTeam != null && !printTeam.isEmpty()) {
            sqlBuilder.append(" AND 인쇄담당팀 = ?");
            params.add(printTeam);
        }
        if (companyContact != null && !companyContact.isEmpty()) {
            sqlBuilder.append(" AND 업체명_담당자 LIKE ?");
            params.add("%" + companyContact + "%");
        }
        if (itemName != null && !itemName.isEmpty()) {
            sqlBuilder.append(" AND 품목명 LIKE ?");
            params.add("%" + itemName + "%");
        }

        sqlBuilder.append(" ORDER BY 주문일자 DESC");

        System.out.println(sqlBuilder.toString());
        System.out.println(params.toString());

        return jdbcTemplate.query(sqlBuilder.toString(), params.toArray(), (rs, rowNum) -> mapRowToPrintInfo(rs));
    }

    /**
     * [리팩토링] ResultSet을 PrintInfo DTO로 매핑하는 공통 메소드
     */
    private PrintInfo mapRowToPrintInfo(ResultSet rs) throws SQLException {
        PrintInfo dto = new PrintInfo();
        dto.setPrintId(rs.getLong("인쇄ID"));
        dto.setPrintMethod(rs.getString("인쇄방법"));
        dto.setItemName(rs.getString("품목명"));
        dto.setBagColor(rs.getString("쇼핑백색상"));
        dto.setSize(rs.getString("사이즈"));
        dto.setQuantity(rs.getString("제작장수"));
        dto.setPrintTeam(rs.getString("인쇄담당팀"));
        dto.setPrintSide(rs.getString("인쇄면"));
        dto.setPrintCount(rs.getString("인쇄도수"));
        dto.setPrintType(rs.getString("인쇄방식"));
        dto.setLogoColor(rs.getString("로고인쇄색상"));
        dto.setLogoSize(rs.getString("로고인쇄크기"));
        dto.setLogoPosition(rs.getString("로고인쇄위치"));
        dto.setRemarks(rs.getString("특이사항"));
        dto.setOrderDate(rs.getString("주문일자"));
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
        dto.setAllocationStatus(rs.getString("배분여부"));
        dto.setCompleted(rs.getString("완료여부"));
        dto.setCreatedAt(rs.getString("등록일시") );
        dto.setUpdatedAt(rs.getString("수정일시") );
        dto.setCreatedBy(rs.getString("등록팀"));
        dto.setUpdatedBy(rs.getString("수정팀"));
        dto.setPickingYn(rs.getString("피킹완료"));
        dto.setOutReadyYn(rs.getString("출고준비"));
        dto.setFileName(rs.getString("파일명"));
        dto.setLogoSamplePath(rs.getString("인쇄로고예시"));
        dto.setPickingDate(rs.getString("피킹예정일"));
        dto.setBoxSize(rs.getString("박스규격"));
        dto.setBoxCount(rs.getString("박스수량"));
        dto.setDeliveryType(rs.getString("배송타입"));
        dto.setPrintEndYn(rs.getString("인쇄완료"));
        dto.setOldOrderYn(rs.getString("기존주문여부"));
        dto.setImportantYn(rs.getString("중요여부"));
        dto.setPrintMemo(rs.getString("인쇄참고사항"));
        dto.setCompanyMemo(rs.getString("업체메모"));
        dto.setWeekDay(rs.getString("요일"));
        dto.setPickingEndAt(rs.getString("피킹완료일시"));
        dto.setPrintEndAt(rs.getString("인쇄완료일시"));
        dto.setOutReadyAt(rs.getString("출고완료일시"));
        dto.setColorData1(rs.getString("조색데이터1"));
        dto.setColorData2(rs.getString("조색데이터2"));
        dto.setColorData3(rs.getString("조색데이터3")); 
        dto.setPrintPhoto(rs.getString("인쇄팀사진"));
        return dto;
    }

    /**
     * [리팩토링] ResultSet을 PrintInfo DTO로 매핑하는 공통 메소드
     */
    private PrintInfo mapRowToPrintInfo2(ResultSet rs) throws SQLException {
        PrintInfo dto = new PrintInfo();
        dto.setPrintId(rs.getLong("인쇄ID"));
        dto.setPrintMethod(rs.getString("인쇄방법"));
        dto.setItemName(rs.getString("품목명"));
        dto.setBagColor(rs.getString("쇼핑백색상"));
        dto.setSize(rs.getString("사이즈"));
        dto.setQuantity(rs.getString("제작장수"));
        dto.setPrintTeam(rs.getString("인쇄담당팀"));
        dto.setPrintSide(rs.getString("인쇄면"));
        dto.setPrintCount(rs.getString("인쇄도수"));
        dto.setPrintType(rs.getString("인쇄방식"));
        dto.setLogoColor(rs.getString("로고인쇄색상"));
        dto.setLogoSize(rs.getString("로고인쇄크기"));
        dto.setLogoPosition(rs.getString("로고인쇄위치"));
        dto.setRemarks(rs.getString("특이사항"));
        dto.setOrderDate(rs.getString("주문일자"));
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
        dto.setAllocationStatus(rs.getString("배분여부"));
        dto.setCompleted(rs.getString("완료여부"));
        dto.setCreatedAt(rs.getString("등록일시") );
        dto.setUpdatedAt(rs.getString("수정일시") );
        dto.setCreatedBy(rs.getString("등록팀"));
        dto.setUpdatedBy(rs.getString("수정팀"));
        dto.setPickingYn(rs.getString("피킹완료"));
        dto.setOutReadyYn(rs.getString("출고준비"));
        dto.setFileName(rs.getString("파일명"));
        dto.setLogoSamplePath(rs.getString("인쇄로고예시"));
        dto.setPickingDate(rs.getString("피킹예정일"));
        dto.setBoxSize(rs.getString("박스규격"));
        dto.setBoxCount(rs.getString("박스수량"));
        dto.setDeliveryType(rs.getString("배송타입"));
        dto.setPrintEndYn(rs.getString("인쇄완료"));
        dto.setOldOrderYn(rs.getString("기존주문여부"));
        dto.setImportantYn(rs.getString("중요여부"));
        dto.setPrintMemo(rs.getString("인쇄참고사항"));
        dto.setCompanyMemo(rs.getString("업체메모"));
        dto.setPickingEndAt(rs.getString("피킹완료일시"));
        dto.setPrintEndAt(rs.getString("인쇄완료일시"));
        dto.setOutReadyAt(rs.getString("출고완료일시"));
        dto.setColorData1(rs.getString("조색데이터1"));
        dto.setColorData2(rs.getString("조색데이터2"));
        dto.setColorData3(rs.getString("조색데이터3")); 
        return dto;
    }
}
