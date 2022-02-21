package com.data;

import java.math.BigDecimal;

public class Sotrud {
	private BigDecimal kodSotr = null;
	private String fio = null;
	private Dolj dolj = null;
	
	public Sotrud() {
		super();
	}
	public Sotrud(BigDecimal kodSotr, String fio, Dolj dolj) {
		super();
		this.kodSotr = kodSotr;
		this.fio = fio;
		this.dolj = dolj;
	}
	
	public BigDecimal getKodSotr() {
		return kodSotr;
	}
	public void setKodSotr(BigDecimal kodSotr) {
		this.kodSotr = kodSotr;
	}
	public String getFio() {
		return fio;
	}
	public void setFio(String fio) {
		this.fio = fio;
	}
	public Dolj getDolj() {
		return dolj;
	}
	public void setDolj(Dolj dolj) {
		this.dolj = dolj;
	}
	
	@Override
	public String toString() {
		return kodSotr + ", " + fio;
	}
}
