package com.example.printinfo.dao;

import org.springframework.web.multipart.MultipartFile;

public class ColorDataDto {
    private int printId;
    private String colorData1;
    private String colorData2;
    private String colorData3;
    private MultipartFile photo;

    // Getters and setters
    public int getPrintId() {
        return printId;
    }

    public void setPrintId(int printId) {
        this.printId = printId;
    }

    public String getColorData1() {
        return colorData1;
    }

    public void setColorData1(String colorData1) {
        this.colorData1 = colorData1;
    }

    public String getColorData2() {
        return colorData2;
    }

    public void setColorData2(String colorData2) {
        this.colorData2 = colorData2;
    }

    public String getColorData3() {
        return colorData3;
    }

    public void setColorData3(String colorData3) {
        this.colorData3 = colorData3;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}