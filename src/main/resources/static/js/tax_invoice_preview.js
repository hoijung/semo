$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const taxinvoiceData = JSON.parse(decodeURIComponent(urlParams.get('data')));

    // Populate preview
    if (taxinvoiceData) {
        $('#previewInvoicerCorpNum').text(taxinvoiceData.invoicerCorpNum);
        $('#previewInvoicerCorpName').text(taxinvoiceData.invoicerCorpName);
        $('#previewInvoicerCEOName').text(taxinvoiceData.invoicerCEOName);
        $('#previewInvoicerAddr').text(taxinvoiceData.invoicerAddr);

        $('#previewInvoiceeCorpNum').text(taxinvoiceData.invoiceeCorpNum);
        $('#previewInvoiceeCorpName').text(taxinvoiceData.invoiceeCorpName);
        $('#previewInvoiceeCEOName').text(taxinvoiceData.invoiceeCEOName);
        $('#previewInvoiceeAddr').text(taxinvoiceData.invoiceeAddr);

        const item = taxinvoiceData.detailList[0];
        $('#previewItems').html(`
            <tr>
                <td>${item.purchaseDT}</td>
                <td>${item.itemName}</td>
                <td>${item.qty}</td>
                <td>${parseInt(item.unitCost).toLocaleString()}</td>
                <td>${parseInt(item.supplyCost).toLocaleString()}</td>
                <td>${parseInt(item.tax).toLocaleString()}</td>
            </tr>
        `);

        $('#previewTotalAmount').text(parseInt(taxinvoiceData.totalAmount).toLocaleString());
    }

    $('#btnConfirmIssue').on('click', function () {
        if (window.opener && !window.opener.closed) {
            window.opener.postMessage({ type: 'issue-tax-invoice', data: taxinvoiceData }, '*');
            window.close();
        }
    });

    $('#btnClose').on('click', function () {
        window.close();
    });
});
