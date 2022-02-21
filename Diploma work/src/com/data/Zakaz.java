package com.data;

import java.math.BigDecimal;
import java.util.Date;

public class Zakaz {
	private BigDecimal idZak = null;
	private BigDecimal nomZak = null;
	private Klient klient = null;
	private String prObr = null;
	private String prOtk = null;
	private Date dataObr = null;
	private String status = null;
	private Sotrud sotrud = null;
	
	public Zakaz() {
		super();
	}
	
	public Zakaz(BigDecimal idZak, BigDecimal nomZak, Klient klient,
			String prObr, String prOtk, Date dataObr, String status,
			Sotrud sotrud) {
		super();
		this.idZak = idZak;
		this.nomZak = nomZak;
		this.klient = klient;
		this.prObr = prObr;
		this.prOtk = prOtk;
		this.dataObr = dataObr;
		this.status = status;
		this.sotrud = sotrud;
	}

	public BigDecimal getIdZak() {
		return idZak;
	}
	public void setIdZak(BigDecimal idZak) {
		this.idZak = idZak;
	}
	public BigDecimal getNomZak() {
		return nomZak;
	}
	public void setNomZak(BigDecimal nomZak) {
		this.nomZak = nomZak;
	}
	public Klient getKlient() {
		return klient;
	}
	public void setKlient(Klient klient) {
		this.klient = klient;
	}
	public String getPrObr() {
		return prObr;
	}
	public void setPrObr(String prObr) {
		this.prObr = prObr;
	}
	public String getPrOtk() {
		return prOtk;
	}
	public void setPrOtk(String prOtk) {
		this.prOtk = prOtk;
	}
	public Date getDataObr() {
		return dataObr;
	}
	public void setDataObr(Date dataObr) {
		this.dataObr = dataObr;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Sotrud getSotrud() {
		return sotrud;
	}
	public void setSotrud(Sotrud sotrud) {
		this.sotrud = sotrud;
	}
	
	@Override
	public String toString(){
		return nomZak + ", " + klient.getNaimKl();
	}
}
