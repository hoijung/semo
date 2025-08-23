package com.example.printinfo.model;

public class 부서 {

    private int 부서ID;
    private String 부서명;
    private String 분류번호;
    private boolean 사용여부;

    public int get부서ID() {
        return 부서ID;
    }

    public void set부서ID(int 부서ID) {
        this.부서ID = 부서ID;
    }

    public String get부서명() {
        return 부서명;
    }

    public void set부서명(String 부서명) {
        this.부서명 = 부서명;
    }

    public String get분류번호() {
        return 분류번호;
    }

    public void set분류번호(String 분류번호) {
        this.분류번호 = 분류번호;
    }

    public boolean is사용여부() {
        return 사용여부;
    }

    public void set사용여부(boolean 사용여부) {
        this.사용여부 = 사용여부;
    }
}
