package com.data;

import java.math.BigDecimal;

public class Rabota {
	private BigDecimal kodRab = null;
	private VidRab vidR = null;
	private String naimRab = null;
	
	public Rabota() {
		super();
	}
	
	public Rabota(BigDecimal kodRab, VidRab vidR, String naimRab) {
		super();
		this.kodRab = kodRab;
		this.vidR = vidR;
		this.naimRab = naimRab;
	}
	
	public BigDecimal getKodRab() {
		return kodRab;
	}
	public void setKodRab(BigDecimal kodRab) {
		this.kodRab = kodRab;
	}
	public VidRab getVidR() {
		return vidR;
	}
	public void setVidR(VidRab vidR) {
		this.vidR = vidR;
	}
	public String getNaimRab() {
		return naimRab;
	}
	public void setNaimRab(String naimRab) {
		this.naimRab = naimRab;
	}
	
	@Override
	public String toString() {
		return kodRab + ", " + vidR.getVidRab() + ", " + naimRab;
	}


}
