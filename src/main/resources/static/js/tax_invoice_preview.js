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

        // debugger

        const item = taxinvoiceData.detailList[0];
        $('#previewItems').html(`
            <tr>
                <td>${item.purchaseDT}</td>
                <td>${item.itemName}</td>
                <td>${taxinvoiceData.qty}</td>
                <td>${parseInt(taxinvoiceData.supplyCostTotal).toLocaleString()}</td>
                <td>${parseInt(taxinvoiceData.taxTotal).toLocaleString()}</td>
                <td>${parseInt(taxinvoiceData.totalAmount).toLocaleString()}</td>.toLocaleString()
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
