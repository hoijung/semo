package com.example.printinfo.model;

public class 공통코드 {

    private int 코드ID;
    private String 코드명;
    private String 코드그룹;
    private boolean 사용여부;

    public int get코드ID() {
        return 코드ID;
    }

    public void set코드ID(int 코드ID) {
        this.코드ID = 코드ID;
    }

    public String get코드명() {
        return 코드명;
    }

    public void set코드명(String 코드명) {
        this.코드명 = 코드명;
    }

    public String get코드그룹() {
        return 코드그룹;
    }

    public void set코드그룹(String 코드그룹) {
        this.코드그룹 = 코드그룹;
    }

    public boolean is사용여부() {
        return 사용여부;
    }

    public void set사용여부(boolean 사용여부) {
        this.사용여부 = 사용여부;
    }
}
