package com.data;

import java.math.BigDecimal;

public class Dolj {
	private BigDecimal kodDol = null;
	private String naimDol = null;
	
	public Dolj() {
		super();
	}
	
	public Dolj(BigDecimal kodDol, String naimDol) {
		super();
		this.kodDol = kodDol;
		this.naimDol = naimDol;
	}
	
	public BigDecimal getKodDol() {
		return kodDol;
	}
	public void setKodDol(BigDecimal kodDol) {
		this.kodDol = kodDol;
	}
	public String getNaimDol() {
		return naimDol;
	}
	public void setNaimDol(String naimDol) {
		this.naimDol = naimDol;
	}
	
	@Override
	public String toString() {
		return kodDol + ", " + naimDol;
	}
	
}
