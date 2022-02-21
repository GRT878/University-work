package com.data;

import java.math.BigDecimal;

public class IspMater {
	private BigDecimal idIspM = null;
	private Mater mater = null;
	private ZakazN zakazN = null;
	private BigDecimal cena = null;
	private BigDecimal kolvo = null;
	private BigDecimal idZn = null;
	
	public IspMater() {
		super();
	}
	
	public IspMater(BigDecimal idIspM, Mater mater, ZakazN zakazN,
			BigDecimal cena, BigDecimal kolvo) {
		super();
		this.idIspM = idIspM;
		this.mater = mater;
		this.zakazN = zakazN;
		this.cena = cena;
		this.kolvo = kolvo;
	}
	
	public BigDecimal getIdIspM() {
		return idIspM;
	}
	public void setIdIspM(BigDecimal idIspM) {
		this.idIspM = idIspM;
	}
	public Mater getMater() {
		return mater;
	}
	public void setMater(Mater mater) {
		this.mater = mater;
	}
	public ZakazN getZakazN() {
		return zakazN;
	}
	public void setZakazN(ZakazN zakazN) {
		this.zakazN = zakazN;
	}
	public BigDecimal getCena() {
		return cena;
	}
	public void setCena(BigDecimal cena) {
		this.cena = cena;
	}
	public BigDecimal getKolvo() {
		return kolvo;
	}
	public void setKolvo(BigDecimal kolvo) {
		this.kolvo = kolvo;
	}

	public BigDecimal getIdZn() {
		return idZn;
	}

	public void setIdZn(BigDecimal idZn) {
		this.idZn = idZn;
	}
}
