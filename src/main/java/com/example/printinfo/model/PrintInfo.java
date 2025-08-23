package com.example.printinfo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PrintInfo {

	/** 인쇄 ID (기본키) */
    private Long printId;

    public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPrintTeam() {
		return printTeam;
	}

	public void setPrintTeam(String printTeam) {
		this.printTeam = printTeam;
	}

	public String getPrintSide() {
		return printSide;
	}

	public void setPrintSide(String printSide) {
		this.printSide = printSide;
	}

	public String getPrintCount() {
		return printCount;
	}

	public void setPrintCount(String printCount) {
		this.printCount = printCount;
	}

	public String getPrintType() {
		return printType;
	}

	public void setPrintType(String printType) {
		this.printType = printType;
	}

	public String getLogoSize() {
		return logoSize;
	}

	public void setLogoSize(String logoSize) {
		this.logoSize = logoSize;
	}

	public String getLogoPosition() {
		return logoPosition;
	}

	public void setLogoPosition(String logoPosition) {
		this.logoPosition = logoPosition;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void String(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getCompanyContact() {
		return companyContact;
	}

	public void setCompanyContact(String companyContact) {
		this.companyContact = companyContact;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDeliveryDeadline() {
		return deliveryDeadline;
	}

	public void setDeliveryDeadline(String deliveryDeadline) {
		this.deliveryDeadline = deliveryDeadline;
	}

	public String getDeliveryZip() {
		return deliveryZip;
	}

	public void setDeliveryZip(String deliveryZip) {
		this.deliveryZip = deliveryZip;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getSalesChannel() {
		return salesChannel;
	}

	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRepresentativeName() {
		return representativeName;
	}

	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getSupplyAmount() {
		return supplyAmount;
	}

	public void setSupplyAmount(BigDecimal supplyAmount) {
		this.supplyAmount = supplyAmount;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Boolean getAllocationStatus() {
		return allocationStatus;
	}

	public void setAllocationStatus(Boolean allocationStatus) {
		this.allocationStatus = allocationStatus;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Boolean getPickingCompleted() {
		return pickingCompleted;
	}

	public void setPickingCompleted(Boolean pickingCompleted) {
		this.pickingCompleted = pickingCompleted;
	}

	public Boolean getReadyForShipment() {
		return readyForShipment;
	}

	public void setReadyForShipment(Boolean readyForShipment) {
		this.readyForShipment = readyForShipment;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLogoSamplePath() {
		return logoSamplePath;
	}

	public void setLogoSamplePath(String logoSamplePath) {
		this.logoSamplePath = logoSamplePath;
	}

	/** 인쇄 방법 */
    private String printMethod;

    /** 품목명 */
    private String itemName;

    /** 쇼핑백 색상 */
    private String bagColor;

    /** 사이즈 */
    private String size;

    /** 제작 장수 */
    private Integer quantity;

    /** 인쇄 담당팀 */
    private String printTeam;

    /** 인쇄 면 */
    private String printSide;

    /** 인쇄 도수 */
    private String printCount;

    /** 인쇄 방식 */
    private String printType;

    /** 로고 인쇄 크기 */
    private String logoSize;

    /** 로고 인쇄 위치 */
    private String logoPosition;

    /** 특이사항 */
    private String remarks;

    /** 주문일자 */
    private String orderDate;

    public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/** 피키예정일 */
    private String pickingDate;
    
    public String getPickingDate() {
		return pickingDate;
	}

	public void setPickingDate(String pickingDate) {
		this.pickingDate = pickingDate;
	}

	/** 업체명 + 담당자 */
    private String companyContact;

    /** 고객 ID */
    private String customerId;

    /** 전화번호 */
    private String phoneNumber;

    /** 발송 마감 기한 */
    private String deliveryDeadline;

    /** 최종 배송지 우편번호 */
    private String deliveryZip;

    /** 최종 배송지 주소 */
    private String deliveryAddress;
    
    /** 배송지 타입 */
    private String deliveryType;    

    public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	/** 판매 채널 */
    private String salesChannel;

    /** 계산서 발행 타입 */
    private String invoiceType;

    /** 상호명 */
    private String companyName;

    /** 대표자명 */
    private String representativeName;

    /** 이메일 */
    private String email;

    /** 공급가액 */
    private BigDecimal supplyAmount;

    /** 부가세액 */
    private BigDecimal taxAmount;

    /** 합계금액 */
    private BigDecimal totalAmount;

    /** 배분 여부 */
    private Boolean allocationStatus;

    /** 완료 여부 */
    private Boolean completed;

    /** 등록일시 */
    private LocalDateTime createdAt;

    /** 수정일시 */
    private LocalDateTime updatedAt;

    /** 등록팀 */
    private String createdBy;

    /** 수정팀 */
    private String updatedBy;

    /** 피킹 완료 여부 */
    private Boolean pickingCompleted;

    /** 출고 준비 여부 */
    private Boolean readyForShipment;

    /** 파일명 */
    private String fileName;

    /** 인쇄 로고 예시 (파일 경로 또는 이름) */
    private String logoSamplePath;

	private String sizeText;
	private String team;
	private String sides;
	private String printSample;

	public String getPickingYn() {
		return pickingYn;
	}

	public void setPickingYn(String pickingYn) {
		this.pickingYn = pickingYn;
	}

	public String getOutReadyYn() {
		return outReadyYn;
	}

	public void setOutReadyYn(String outReadyYn) {
		this.outReadyYn = outReadyYn;
	}

	private String pickingYn;
	private String outReadyYn;

	public Long getPrintId() {
		return printId;
	}

	public void setPrintId(Long printId) {
		this.printId = printId;
	}

	public String getPrintMethod() {
		return printMethod;
	}

	public void setPrintMethod(String printMethod) {
		this.printMethod = printMethod;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getBagColor() {
		return bagColor;
	}

	public void setBagColor(String bagColor) {
		this.bagColor = bagColor;
	}

	public String getSizeText() {
		return sizeText;
	}

	public void setSizeText(String sizeText) {
		this.sizeText = sizeText;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getSides() {
		return sides;
	}

	public void setSides(String sides) {
		this.sides = sides;
	}

	public String getPrintSample() {
		return printSample;
	}

	public void setPrintSample(String printSample) {
		this.printSample = printSample;
	}
}
