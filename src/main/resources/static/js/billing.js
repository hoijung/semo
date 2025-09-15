$(document).ready(function () {
    loadMenu('billing.html');

    const today = new Date();
    const formattedToday = today.toISOString().split("T")[0];
    $('#orderDateEnd').val(formattedToday);

    const startDay = new Date();
    startDay.setDate(startDay.getDate() - 31);
    const formattedStart = startDay.toISOString().split("T")[0];
    $('#orderDateStart').val(formattedStart);

    const table = $('#billingGrid').DataTable({
        responsive: true,
        dom: 'rtip',
        ajax: {
            url: '/api/prints/billing-list?' + $('#searchForm').serialize(),
            dataSrc: 'data'
        },
        scrollY: getTableHeight("320"),
        scrollX: true,
        columns: [
            { data: 'orderDate', title: '주문일자', className: 'dt-center' },
            { data: 'companyContact', title: '업체명(고객명)' },
            { data: 'itemName', title: '품목명' },
            { data: 'quantity', title: '수량' },
            {
                data: 'supplyAmount',
                title: '공급가액',
            },
            {
                data: 'taxAmount',
                title: '세액',
            },
            {
                data: 'totalAmount',
                title: '합계금액',
            }
        ],
        searching: true,
        lengthChange: false,
        paging: false,
        info: false,
        language: {
            emptyTable: "데이터가 없습니다."
        }
    });

    $('#customSearch').on('keyup', function () {
        table.search(this.value).draw();
    });

    $('#btnSearch').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
        const query = $('#searchForm').serialize();
        table.ajax.url('/api/prints/billing-list?' + query).load();
    });

    $('#billingGrid tbody').on('click', 'tr', function () {
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        } else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });

    $('#btnIssueTaxInvoice').on('click', function () {
        const selectedRows = table.rows('.selected').data();
        if (selectedRows.length !== 1) {
            alert('발행할 항목을 하나만 선택해주세요.');
            return;
        }

        const data = selectedRows[0];

        const taxinvoiceToIssue = {
            invoicerCorpNum: "1234567890",
            invoicerCorpName: "공급자 상호",
            invoicerCEOName: "공급자 대표자명",
            invoicerAddr: "공급자 주소",
            invoicerContactName: "공급자 담당자명",
            invoicerEmail: "invoicer@example.com",

            invoiceeCorpNum: "0987654321",
            invoiceeType: "사업자",
            invoiceeCorpName: data.companyContact,
            invoiceeCEOName: "구매자 대표자명",
            invoiceeAddr: "구매자 주소",
            invoiceeEmail: "invoicee@example.com",

            writeDate: new Date().toISOString().slice(0, 10).replace(/-/g, ""),
            issueType: "정발행",
            purposeType: "영수",
            taxType: "과세",

            supplyCostTotal: data.supplyAmount.toString(),
            taxTotal: data.taxAmount.toString(),
            totalAmount: data.totalAmount.toString(),

            detailList: [{
                serialNum: 1,
                purchaseDT: new Date().toISOString().slice(0, 10).replace(/-/g, ""),
                itemName: data.itemName,
                qty: data.quantity.toString(),
                unitCost: (data.supplyAmount / data.quantity).toString(),
                supplyCost: data.supplyAmount.toString(),
                tax: data.taxAmount.toString()
            }]
        };

        const previewUrl = `tax_invoice_preview.html?data=${encodeURIComponent(JSON.stringify(taxinvoiceToIssue))}`;
        window.open(previewUrl, 'taxInvoicePreview', 'width=800,height=600');
    });

    window.addEventListener('message', function (event) {
        if (event.data.type === 'issue-tax-invoice') {
            const taxinvoice = event.data.data;
            $.ajax({
                url: '/api/prints/issue-tax-invoice',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(taxinvoice),
                success: function (response) {
                    alert('세금계산서가 발행되었습니다.');
                    console.log(response);
                    table.ajax.reload(null, false);
                },
                error: function (xhr, status, error) {
                    alert('세금계산서 발행에 실패했습니다.');
                    console.error(xhr.responseText);
                }
            });
        }
    });
});