package com.example.printinfo.model;


import lombok.Getter;
import lombok.Setter;

public class 사용자Dto {
    public String get아이디() {
		return 아이디;
	}
	public void set아이디(String 아이디) {
		this.아이디 = 아이디;
	}
	public String get비밀번호() {
		return 비밀번호;
	}
	public void set비밀번호(String 비밀번호) {
		this.비밀번호 = 비밀번호;
	}
	//사용자명, 아이디, 비밀번호, 사용여부, 사용자ID
	private String 아이디;
    private String 비밀번호;
    private String 사용자명;
    private String 사용여부;
    private String 사용자ID;
    private String id;
    
    public String get사용자명() {
		return 사용자명;
	}
	public void set사용자명(String 사용자명) {
		this.사용자명 = 사용자명;
	}
	public String get사용여부() {
		return 사용여부;
	}
	public void set사용여부(String 사용여부) {
		this.사용여부 = 사용여부;
	}
	public String get사용자ID() {
		return 사용자ID;
	}
	public void set사용자ID(String 사용자id) {
		사용자ID = 사용자id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
