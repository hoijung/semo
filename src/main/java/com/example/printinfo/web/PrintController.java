package com.example.printinfo.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.printinfo.dao.PrintDto;
import com.example.printinfo.model.PrintInfo;
import com.example.printinfo.service.FileStorageService;
import com.example.printinfo.service.PrintService;
import com.popbill.api.IssueResponse;
import com.popbill.api.PopbillException;
import com.popbill.api.TaxinvoiceService;
import com.popbill.api.taxinvoice.Taxinvoice;
import com.popbill.api.taxinvoice.TaxinvoiceAddContact;
import com.popbill.api.taxinvoice.TaxinvoiceDetail;
import com.example.printinfo.model.TaxInvoiceRequest; // Import the new DTO

@RestController
@RequestMapping("/api/prints")
public class PrintController {

    @Autowired
    private PrintService service;

    @Autowired
    private TaxinvoiceService taxinvoiceService;

    private static TaxinvoiceService taxinvoiceService2;

    @Autowired(required = false)
    private FileStorageService fileStorageService;

    @Value("${popbill.link-id}")
    private String linkId;

    @Value("${popbill.secret-key}")
    private String secretKey;

    @GetMapping
    public List<PrintDto> getAll() {
        return service.findAll();
    }

    @GetMapping("/search")
    public Map<String, Object> searchPrints(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {

        List<PrintInfo> list = service.search(orderDateStart, orderDateEnd, printTeam, companyContact, itemName);

        Map<String, Object> response = new HashMap<>();
        response.put("data", list);
        return response;
    }

    @GetMapping("/printList1")
    public Map<String, Object> getPrintAll1(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd,
            @RequestParam(required = false) String printTeam,
            @RequestParam(required = false) String companyContact,
            @RequestParam(required = false) String itemName) {

        List<PrintInfo> list = service.searchPrints(orderDateStart, orderDateEnd, printTeam, companyContact, itemName);
        Map<String, Object> response = new HashMap<>();
        response.put("data", list);
        return response;
    }

    @GetMapping("/{id}")
    public PrintDto getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public PrintDto create(@RequestPart("dto") PrintDto dto,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) throws IOException {
        if (fileStorageService != null && logoFile != null && !logoFile.isEmpty()) {
            String fileName = fileStorageService.storeFile(logoFile);
            dto.set인쇄로고예시(fileName);
        }
        service.insert(dto);
        return dto;
    }

    @PutMapping("/{id}")
    public PrintDto update(@PathVariable Integer id,
            @RequestPart("dto") PrintDto dto,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile) throws IOException {
        if (fileStorageService != null && logoFile != null && !logoFile.isEmpty()) {
            PrintDto existingDto = service.findById(id);
            if (existingDto != null && existingDto.get인쇄로고예시() != null) {
                fileStorageService.deleteFile(existingDto.get인쇄로고예시());
            }
            String fileName = fileStorageService.storeFile(logoFile);
            dto.set인쇄로고예시(fileName);
        }

        dto.set인쇄ID(id);
        service.update(dto);
        return dto;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(id);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/distribute")
    public PrintDto distributePrint(@PathVariable Integer id) {
        return service.distributePrint(id);
    }

    @PostMapping("/{id}/cancelDistribute")
    public ResponseEntity<?> cancelDistributePrint(@PathVariable Integer id) {
        try {
            PrintDto updatedDto = service.cancelDistributePrint(id);
            return ResponseEntity.ok(updatedDto);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // @PostMapping("/issue-tax-invoice2")
    // public ResponseEntity<?> issueTaxInvoice2(@RequestBody Taxinvoice taxinvoice) {
    //     try {
    //         String mgtKey = taxinvoice.getInvoicerCorpNum() + "-" + taxinvoice.getWriteDate() + "-"
    //                 + System.currentTimeMillis();
    //         IssueResponse response = taxinvoiceService.registIssue(taxinvoice.getInvoicerCorpNum(), taxinvoice, false);
    //         return ResponseEntity.ok(response);
    //     } catch (PopbillException e) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    //     }
    // }

    @GetMapping("/billing-list")
    public Map<String, Object> getBillingList(
            @RequestParam(required = false) String orderDateStart,
            @RequestParam(required = false) String orderDateEnd) {

        List<PrintInfo> list = service.search(orderDateStart, orderDateEnd, null, null, null);

        Map<String, Object> response = new HashMap<>();
        response.put("data", list);
        return response;
    }

    @PostMapping("/issue-tax-invoice")
    public ResponseEntity<?> issueTaxInvoice(@RequestBody TaxInvoiceRequest request) {
        Taxinvoice taxinvoice = request.getTaxinvoice();
        String printId = request.getPrintId();
        /**
         * 작성된 세금계산서 데이터를 팝빌에 저장과 동시에 발행(전자서명)하여 "발행완료" 상태로 처리합니다. [참고] 팝빌 국세청 전송 정책
         * "발행완료"된 전자세금계산서는 국세청 전송 이전에 [CancelIssue – 발행취소] 함수로 국세청 신고 대상에서 제외할 수 있습니다.
         * - [Register - 임시저장]과 [Issue - 발행] 함수 기능을 한 번의 프로세스로 처리합니다.
         * 세금계산서 발행을 위해서 공급자의 인증서가 팝빌 인증서버에 사전등록 되어야 합니다.
         * - 위수탁발행의 경우, 수탁자의 인증서 등록이 필요합니다.
         * 세금계산서 발행시 포인트가 과금되며, 공급받는자에게 발행 메일이 발송됩니다.
         * -
         * https://developers.popbill.com/reference/taxinvoice/java/api/issue#RegistIssue
         */

        
        PrintDto dto = this.getById(new Integer(printId));

        // 팝빌회원 사업자번호
        String CorpNum = "8500501563";

        // 전자세금계산서 정보
        // Taxinvoice taxinvoice = new Taxinvoice();

        // 발행형태, {정발행, 역발행, 위수탁} 중 기재
        taxinvoice.setIssueType("정발행");
        // 과세형태, {과세, 영세, 면세} 중 기재
        taxinvoice.setTaxType("과세");
        // 과금방향, {정과금, 역과금} 중 기재
        // └ 정과금 = 공급자 과금 , 역과금 = 공급받는자 과금
        // -'역과금'은 역발행 세금계산서 발행 시에만 이용가능
        taxinvoice.setChargeDirection("정과금");
        // 일련번호
        taxinvoice.setSerialNum("123");
        // 책번호 '권' 항목
        taxinvoice.setKwon((short) 1);
        // 책번호 '호' 항목
        taxinvoice.setHo((short) 1);
        // 작성일자, 날짜형식(yyyyMMdd)
        taxinvoice.setWriteDate(dto.get주문일자().replaceAll("-", ""));

        // 영수/청구, {영수, 청구, 없음} 중 기재
        taxinvoice.setPurposeType("청구");
        // 공급가액 합계
        taxinvoice.setSupplyCostTotal(dto.get공급가액().toString());
        // 세액 합계
        taxinvoice.setTaxTotal(dto.get부가세액().toString());
        // 합계금액, 공급가액 + 세액
        taxinvoice.setTotalAmount(dto.get합계금액().toString());
        // 비고
        // {invoiceeType}이 "외국인" 이면 remark1 필수
        // - 외국인 등록번호 또는 여권번호 입력
        taxinvoice.setRemark1("");

        /***************************************************************************
         * 공급자 정보
         ****************************************************************************/

        // 공급자 사업자번호 (하이픈 '-' 제외 10 자리)
        taxinvoice.setInvoicerCorpNum(CorpNum);
        // 공급자 상호
        taxinvoice.setInvoicerCorpName("베리나인");
        // 공급자 대표자 성명
        taxinvoice.setInvoicerCEOName("염신웅");
        // 공급자 주소
        taxinvoice.setInvoicerAddr("경기도 안양시 동안구 흥안대로427번길 76-1 2층 (우 : 14059)");
        // 공급자 업태
        taxinvoice.setInvoicerBizType("전자상거래업");
        // 공급자 종목
        taxinvoice.setInvoicerBizClass("전자상거래 소매업");
        // 공급자 담당자 성명
        taxinvoice.setInvoicerContactName("염신웅");
        // 공급자 담당자 부서명
        taxinvoice.setInvoicerDeptName("공급자 담당자 부서명");
        // 공급자 담당자 연락처
        taxinvoice.setInvoicerTEL("031-426-9361");
        // 공급자 담당자 휴대폰
        taxinvoice.setInvoicerHP("031-426-9361");
        // 공급자 담당자 메일
        taxinvoice.setInvoicerEmail("swyum@verynine.co.kr");

        /***************************************************************************
         * 공급받는자 정보
         ****************************************************************************/

        // [역발행시 필수] 공급받는자 문서번호, 1~24자리 (숫자, 영문, '-', '_') 를 조합하여 사업자별로 중복되지 않도록 구성
        // taxinvoice.setInvoiceeMgtKey("1231231");

        // 공급받는자 유형, [사업자, 개인, 외국인] 중 기재
        taxinvoice.setInvoiceeType("사업자");

        // 공급받는자 등록번호
        // - {invoiceeType}이 "사업자" 인 경우, 사업자번호 (하이픈 ('-') 제외 10자리)
        // - {invoiceeType}이 "개인" 인 경우, 주민등록번호 (하이픈 ('-') 제외 13자리)
        // - {invoiceeType}이 "외국인" 인 경우, "9999999999999" (하이픈 ('-') 제외 13자리)
        taxinvoice.setInvoiceeCorpNum(taxinvoice.getInvoiceeCorpNum().replaceAll("-", ""));
        // 공급받는자 상호
        taxinvoice.setInvoiceeCorpName(taxinvoice.getInvoiceeCorpName());
        // 공급받는자 대표자 성명
        taxinvoice.setInvoiceeCEOName(taxinvoice.getInvoiceeCEOName());
        // 공급받는자 업태
        taxinvoice.setInvoiceeBizType("");
        // 공급받는자 종목
        taxinvoice.setInvoiceeBizClass("");
        // 공급받는자 담당자 메일
        // 팝빌 개발환경에서 테스트하는 경우에도 안내 메일이 전송되므로,
        // 실제 거래처의 메일주소가 기재되지 않도록 주의
        taxinvoice.setInvoiceeEmail1(taxinvoice.getInvoiceeEmail1());
        /***************************************************************************
         * 품목 상세정보
         ****************************************************************************/
        taxinvoice.setDetailList(new ArrayList<TaxinvoiceDetail>());

        TaxinvoiceDetail detail = new TaxinvoiceDetail();

        detail.setSerialNum((short) 1); // 일련번호, 1부터 순차기재
        detail.setPurchaseDT(dto.get주문일자().replaceAll("-", "") ); // 거래일자
        detail.setItemName(dto.get품목명()); // 품명
        detail.setSpec(""); // 규격
        detail.setQty(""); // 수량
        detail.setUnitCost(""); // 단가
        detail.setSupplyCost(dto.get공급가액().toString()); // 공급가액
        detail.setTax(dto.get부가세액().toString()); // 세액
        // detail.setRemark("품목비고"); // 비고

        taxinvoice.getDetailList().add(detail);
        // 세금계산서 상태 이력을 관리하기 위한 메모
        String Memo = "즉시발행 메모";

        // 세금계산서 발행 안내메일 제목
        String EmailSubject = "";
        // 공급자 문서번호, 1~24자리 (숫자, 영문, '-', '_') 조합으로 사업자 별로 중복되지 않도록 구성
        taxinvoice.setInvoicerMgtKey(printId + "-" + System.currentTimeMillis());
        // taxinvoice.setInvoiceeMgtKey(printId + "-" + System.currentTimeMillis());

        try {
            System.out.println(taxinvoice.getInvoicerMgtKey());
            IssueResponse response = taxinvoiceService.registIssue(CorpNum, taxinvoice, null, Memo,
                    null, null, EmailSubject, "verynine_info");

            // 발행 성공 시 국세청 승인번호를 DB에 업데이트합니다.
            if (response.getCode() == 1) {
                service.updateNtsConfirmNum(Integer.parseInt(printId), response.getNtsConfirmNum());
            }

            return ResponseEntity.ok(response);
        } catch (PopbillException e) {
            System.out.println(e.getLocalizedMessage());
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}