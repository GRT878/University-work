package com.data;

import java.math.BigDecimal;

public class Klient {
	private BigDecimal kodKl = null;
	private String naimKl = null;
	private String doc = null;
	private String tel = null;
	
	public Klient() {
		super();
	}
	
	public Klient(BigDecimal kodKl, String naimKl, String doc, String tel) {
		super();
		this.kodKl = kodKl;
		this.naimKl = naimKl;
		this.doc = doc;
		this.tel = tel;
	}
	
	public BigDecimal getKodKl() {
		return kodKl;
	}
	public void setKodKl(BigDecimal kodKl) {
		this.kodKl = kodKl;
	}
	public String getNaimKl() {
		return naimKl;
	}
	public void setNaimKl(String naimKl) {
		this.naimKl = naimKl;
	}
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	@Override
	public String toString() {
		return kodKl + ", " + naimKl;
	}
}
