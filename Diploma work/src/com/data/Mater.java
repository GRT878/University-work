package com.data;

import java.math.BigDecimal;

public class Mater {
	private BigDecimal kodM = null;
	private String naimM = null;
	private BigDecimal cenaEd = null;
	
	public Mater(){
		super();
	}
	
	public Mater(BigDecimal kodM, String naimM, BigDecimal cenaEd) {
		super();
		this.kodM = kodM;
		this.naimM = naimM;
		this.cenaEd = cenaEd;
	}
	
	public BigDecimal getKodM() {
		return kodM;
	}
	public void setKodM(BigDecimal kodM) {
		this.kodM = kodM;
	}
	public String getNaimM() {
		return naimM;
	}
	public void setNaimM(String naimM) {
		this.naimM = naimM;
	}
	public BigDecimal getCenaEd() {
		return cenaEd;
	}
	public void setCenaEd(BigDecimal cenaEd) {
		this.cenaEd = cenaEd;
	}

	@Override
	public String toString() {
		return kodM + ", " + naimM;
	}
	
	
}
