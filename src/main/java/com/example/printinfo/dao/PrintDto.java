package com.example.printinfo.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PrintDto {
    public Integer get인쇄ID() {
		return 인쇄ID;
	}
	public void set인쇄ID(Integer 인쇄id) {
		인쇄ID = 인쇄id;
	}
	public String get품목명() {
		return 품목명;
	}
	public void set품목명(String 품목명) {
		this.품목명 = 품목명;
	}
	public String get쇼핑백색상() {
		return 쇼핑백색상;
	}
	public void set쇼핑백색상(String 쇼핑백색상) {
		this.쇼핑백색상 = 쇼핑백색상;
	}
	public String get사이즈() {
		return 사이즈;
	}
	public void set사이즈(String 사이즈) {
		this.사이즈 = 사이즈;
	}
	public Integer get제작장수() {
		return 제작장수;
	}
	public void set제작장수(Integer 제작장수) {
		this.제작장수 = 제작장수;
	}
	public String get인쇄담당팀() {
		return 인쇄담당팀;
	}
	public void set인쇄담당팀(String 인쇄담당팀) {
		this.인쇄담당팀 = 인쇄담당팀;
	}
	public String get인쇄면() {
		return 인쇄면;
	}
	public void set인쇄면(String 인쇄면) {
		this.인쇄면 = 인쇄면;
	}
	public String get인쇄도수() {
		return 인쇄도수;
	}
	public void set인쇄도수(String 인쇄도수) {
		this.인쇄도수 = 인쇄도수;
	}
	public String get인쇄방식() {
		return 인쇄방식;
	}
	public void set인쇄방식(String 인쇄방식) {
		this.인쇄방식 = 인쇄방식;
	}
	public String get로고인쇄크기() {
		return 로고인쇄크기;
	}
	public void set로고인쇄크기(String 로고인쇄크기) {
		this.로고인쇄크기 = 로고인쇄크기;
	}
	public String get로고인쇄위치() {
		return 로고인쇄위치;
	}
	public void set로고인쇄위치(String 로고인쇄위치) {
		this.로고인쇄위치 = 로고인쇄위치;
	}
	public String get특이사항() {
		return 특이사항;
	}
	public void set특이사항(String 특이사항) {
		this.특이사항 = 특이사항;
	}
	public String get주문일자() {
		return 주문일자;
	}
	public void set주문일자(String 주문일자) {
		this.주문일자 = 주문일자;
	}
	public String get업체명_담당자() {
		return 업체명_담당자;
	}
	public void set업체명_담당자(String 업체명_담당자) {
		this.업체명_담당자 = 업체명_담당자;
	}
	public String get고객ID() {
		return 고객ID;
	}
	public void set고객ID(String 고객id) {
		고객ID = 고객id;
	}
	public String get전화번호() {
		return 전화번호;
	}
	public void set전화번호(String 전화번호) {
		this.전화번호 = 전화번호;
	}
	public String get발송마감기한() {
		return 발송마감기한;
	}
	public void set발송마감기한(String 발송마감기한) {
		this.발송마감기한 = 발송마감기한;
	}
	public String get최종배송지_우편번호() {
		return 최종배송지_우편번호;
	}
	public void set최종배송지_우편번호(String 최종배송지_우편번호) {
		this.최종배송지_우편번호 = 최종배송지_우편번호;
	}
	public String get최종배송지_주소() {
		return 최종배송지_주소;
	}
	public void set최종배송지_주소(String 최종배송지_주소) {
		this.최종배송지_주소 = 최종배송지_주소;
	}
	public String get판매채널() {
		return 판매채널;
	}
	public void set판매채널(String 판매채널) {
		this.판매채널 = 판매채널;
	}
	public String get계산서발행타입() {
		return 계산서발행타입;
	}
	public void set계산서발행타입(String 계산서발행타입) {
		this.계산서발행타입 = 계산서발행타입;
	}
	public String get상호명() {
		return 상호명;
	}
	public void set상호명(String 상호명) {
		this.상호명 = 상호명;
	}
	public String get대표자명() {
		return 대표자명;
	}
	public void set대표자명(String 대표자명) {
		this.대표자명 = 대표자명;
	}
	public String get이메일() {
		return 이메일;
	}
	public void set이메일(String 이메일) {
		this.이메일 = 이메일;
	}
	public BigDecimal get공급가액() {
		return 공급가액;
	}
	public void set공급가액(BigDecimal 공급가액) {
		this.공급가액 = 공급가액;
	}
	public BigDecimal get부가세액() {
		return 부가세액;
	}
	public void set부가세액(BigDecimal 부가세액) {
		this.부가세액 = 부가세액;
	}
	public BigDecimal get합계금액() {
		return 합계금액;
	}
	public void set합계금액(BigDecimal 합계금액) {
		this.합계금액 = 합계금액;
	}
	public Boolean get배분여부() {
		return 배분여부;
	}
	public void set배분여부(Boolean 배분여부) {
		this.배분여부 = 배분여부;
	}
	public Boolean get완료여부() {
		return 완료여부;
	}
	public void set완료여부(Boolean 완료여부) {
		this.완료여부 = 완료여부;
	}
	public String get등록팀() {
		return 등록팀;
	}
	public void set등록팀(String 등록팀) {
		this.등록팀 = 등록팀;
	}
	public String get수정팀() {
		return 수정팀;
	}
	public void set수정팀(String 수정팀) {
		this.수정팀 = 수정팀;
	}
	public Boolean get피킹완료() {
		return 피킹완료;
	}
	public void set피킹완료(Boolean 피킹완료) {
		this.피킹완료 = 피킹완료;
	}
	public Boolean get출고준비() {
		return 출고준비;
	}
	public void set출고준비(Boolean 출고준비) {
		this.출고준비 = 출고준비;
	}
	public String get파일명() {
		return 파일명;
	}
	public void set파일명(String 파일명) {
		this.파일명 = 파일명;
	}
	public String get인쇄로고예시() {
		return 인쇄로고예시;
	}
	public void set인쇄로고예시(String 인쇄로고예시) {
		this.인쇄로고예시 = 인쇄로고예시;
	}
	public String get피킹예정일() {
		return 피킹예정일;
	}
	public void set피킹예정일(String 피킹예정일) {
		this.피킹예정일 = 피킹예정일;
	}
	public String get배송타입() {
		return 배송타입;
	}
	public void set배송타입(String 배송타입) {
		this.배송타입 = 배송타입;
	}
	public String get박스규격() {
		return 박스규격;
	}
	public void set박스규격(String 박스규격) {
		this.박스규격 = 박스규격;
	}
	public String get기존주문여부() {
		return 기존주문여부;
	}
	public void set기존주문여부(String 기존주문여부) {
		this.기존주문여부 = 기존주문여부;
	}
	public String get인쇄방법() {
		return 인쇄방법;
	}
	public void set인쇄방법(String 인쇄방법) {
		this.인쇄방법 = 인쇄방법;
	}
	private Integer 인쇄ID;
    private String 품목명;
    private String 쇼핑백색상;
    private String 사이즈;
    private Integer 제작장수;
    private String 인쇄담당팀;
    private String 인쇄면;
    private String 인쇄도수;
    private String 인쇄방식;
    private String 로고인쇄크기;
    private String 로고인쇄위치;
    private String 특이사항;
    private String 주문일자;
    private String 업체명_담당자;
    private String 고객ID;
    private String 전화번호;
    private String 발송마감기한;
    private String 최종배송지_우편번호;
    private String 최종배송지_주소;
	private String 배송지주소상세;
    public String get배송지주소상세() {
		return 배송지주소상세;
	}
	public void set배송지주소상세(String 배송지주소상세) {
		this.배송지주소상세 = 배송지주소상세;
	}
	public String get로고인쇄색상() {
		return 로고인쇄색상;
	}
	public void set로고인쇄색상(String 로고인쇄색상) {
		this.로고인쇄색상 = 로고인쇄색상;
	}
	private String 판매채널;
    private String 계산서발행타입;
    private String 상호명;
    private String 대표자명;
    private String 이메일;
    private BigDecimal 공급가액;
    private BigDecimal 부가세액;
    private BigDecimal 합계금액;
    private Boolean 배분여부;
    private Boolean 완료여부;
    private String 등록팀;
    private String 수정팀;
    private Boolean 피킹완료;
    private Boolean 출고준비;
    private String 파일명;
    private String 인쇄로고예시;
    private String 피킹예정일;
    private String 배송타입;
    private String 박스규격;
    private String 기존주문여부;
    private String 인쇄방법;
    private String 로고인쇄색상;
    private String 조색데이터1;
    public String get조색데이터1() {
		return 조색데이터1;
	}
	public void set조색데이터1(String 조색데이터1) {
		this.조색데이터1 = 조색데이터1;
	}
	public String get조색데이터2() {
		return 조색데이터2;
	}
	public void set조색데이터2(String 조색데이터2) {
		this.조색데이터2 = 조색데이터2;
	}
	public String get조색데이터3() {
		return 조색데이터3;
	}
	public void set조색데이터3(String 조색데이터3) {
		this.조색데이터3 = 조색데이터3;
	}
	private String 조색데이터2;
    private String 조색데이터3;
	private String 인쇄참고사항;
	
	public String get인쇄참고사항() {
		return 인쇄참고사항;
	}
    public void set인쇄참고사항(String 인쇄참고사항) {
        this.인쇄참고사항 = 인쇄참고사항;
    }

    // Getter/Setter 생략 (Lombok 사용 가능)
}

