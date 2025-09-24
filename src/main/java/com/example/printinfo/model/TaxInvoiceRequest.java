package com.example.printinfo.model;

import com.popbill.api.taxinvoice.Taxinvoice;

public class TaxInvoiceRequest {
    private Taxinvoice taxinvoice;
    private String printId;

    public Taxinvoice getTaxinvoice() {
        return taxinvoice;
    }

    public void setTaxinvoice(Taxinvoice taxinvoice) {
        this.taxinvoice = taxinvoice;
    }

    public String getPrintId() {
        return printId;
    }

    public void setPrintId(String printId) {
        this.printId = printId;
    }
}