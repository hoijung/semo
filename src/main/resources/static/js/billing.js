$(document).ready(function() {
    var itemsTable = $('#itemsTable').DataTable({
        "paging": false,
        "searching": false,
        "info": false,
        "ordering": false,
        "columns": [
            { "data": "name" },
            { "data": "quantity" },
            { "data": "unitPrice" },
            { "data": "amount" },
            { "data": null, "defaultContent": "<button class='btn-delete'>삭제</button>" }
        ]
    });

    $('#btnAddItem').on('click', function() {
        itemsTable.row.add({
            "name": "<input type='text' class='itemName'>",
            "quantity": "<input type='number' class='itemQuantity' value='1'>",
            "unitPrice": "<input type='number' class='itemUnitPrice' value='0'>",
            "amount": "0",
        }).draw(false);
    });

    $('#itemsTable tbody').on('click', '.btn-delete', function() {
        itemsTable.row($(this).parents('tr')).remove().draw();
        updateTotalAmount();
    });

    $('#itemsTable tbody').on('change', '.itemQuantity, .itemUnitPrice', function() {
        var row = $(this).closest('tr');
        var quantity = row.find('.itemQuantity').val();
        var unitPrice = row.find('.itemUnitPrice').val();
        var amount = quantity * unitPrice;
        itemsTable.cell(row, 3).data(amount).draw();
        updateTotalAmount();
    });

    function updateTotalAmount() {
        var total = 0;
        var supplyCostTotal = 0;
        itemsTable.rows().every(function() {
            var rowNode = this.node();
            var quantity = $(rowNode).find('.itemQuantity').val();
            var unitPrice = $(rowNode).find('.itemUnitPrice').val();
            var amount = quantity * unitPrice;
            if (!isNaN(amount)) {
                supplyCostTotal += amount;
            }
        });

        var taxTotal = supplyCostTotal * 0.1; // 10% tax
        total = supplyCostTotal + taxTotal;

        $('#totalAmount').val(total);
    }

    $('#btnSave').on('click', function() {
        var taxinvoice = {
            // 공급자 정보 (실제로는 서버에서 설정하거나, 로그인 정보 등을 활용해야 합니다)
            invoicerCorpNum: "1234567890",
            invoicerCorpName: "공급자 상호",
            invoicerCEOName: "공급자 대표자명",
            invoicerAddr: "공급자 주소",
            invoicerContactName: "공급자 담당자명",
            invoicerEmail: "invoicer@example.com",

            // 공급받는자 정보 (폼에서 가져옵니다)
            invoiceeCorpNum: "0987654321", // 예시 사업자번호
            invoiceeType: "사업자",
            invoiceeCorpName: $('#customerName').val(),
            invoiceeCEOName: "구매자 대표자명",
            invoiceeAddr: "구매자 주소",
            invoiceeEmail: "invoicee@example.com",

            // 세금계산서 정보
            writeDate: new Date().toISOString().slice(0, 10).replace(/-/g, ""), // 오늘 날짜 (YYYYMMDD)
            issueType: "정발행",
            purposeType: "영수",
            taxType: "과세",

            // 금액 정보
            supplyCostTotal: "0",
            taxTotal: "0",
            totalAmount: "0",

            // 품목 정보
            detailList: []
        };

        var supplyCostTotal = 0;
        itemsTable.rows().every(function(index) {
            var rowNode = this.node();
            var itemName = $(rowNode).find('.itemName').val();
            var quantity = $(rowNode).find('.itemQuantity').val();
            var unitPrice = $(rowNode).find('.itemUnitPrice').val();
            var amount = quantity * unitPrice;

            supplyCostTotal += amount;

            taxinvoice.detailList.push({
                serialNum: index + 1,
                purchaseDT: taxinvoice.writeDate,
                itemName: itemName,
                qty: quantity,
                unitCost: unitPrice,
                supplyCost: amount.toString(),
                tax: (amount * 0.1).toString()
            });
        });

        var taxTotal = supplyCostTotal * 0.1;
        taxinvoice.supplyCostTotal = supplyCostTotal.toString();
        taxinvoice.taxTotal = taxTotal.toString();
        taxinvoice.totalAmount = (supplyCostTotal + taxTotal).toString();

        $.ajax({
            url: '/api/prints/issue-tax-invoice',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(taxinvoice),
            success: function(response) {
                alert('세금계산서가 발행되었습니다.');
                console.log(response);
            },
            error: function(xhr, status, error) {
                alert('세금계산서 발행에 실패했습니다.');
                console.error(xhr.responseText);
            }
        });
    });

    $('#btnPrint').on('click', function() {
        window.print();
    });
});