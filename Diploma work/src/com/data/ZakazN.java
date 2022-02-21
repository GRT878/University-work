package com.data;

import java.math.BigDecimal;
import java.util.Date;

public class ZakazN {
	private BigDecimal idZn = null;
	private BigDecimal nomZn = null;
	private Zakaz zakaz = null;
	private Date dataOform = null;
	private Date srokIsp = null;
	private String model = null;
	private String regNom = null;
	private Sotrud sotrud = null;
	private BigDecimal stRab = null;
	private BigDecimal stM = null;
	
	public ZakazN() {
		super();
	}
	
	public ZakazN(BigDecimal idZn, BigDecimal nomZn, Zakaz zakaz,
			Date dataOform, Date srokIsp, String model, String regNom,
			Sotrud sotrud, BigDecimal stRab, BigDecimal stN) {
		super();
		this.idZn = idZn;
		this.nomZn = nomZn;
		this.zakaz = zakaz;
		this.dataOform = dataOform;
		this.srokIsp = srokIsp;
		this.model = model;
		this.regNom = regNom;
		this.sotrud = sotrud;
		this.stRab = stRab;
		this.stM = stN;
	}
	
	public BigDecimal getIdZn() {
		return idZn;
	}
	public void setIdZn(BigDecimal idZn) {
		this.idZn = idZn;
	}
	public BigDecimal getNomZn() {
		return nomZn;
	}
	public void setNomZn(BigDecimal nomZn) {
		this.nomZn = nomZn;
	}
	public Zakaz getZakaz() {
		return zakaz;
	}
	public void setZakaz(Zakaz zakaz) {
		this.zakaz = zakaz;
	}
	public Date getDataOform() {
		return dataOform;
	}
	public void setDataOform(Date dataOform) {
		this.dataOform = dataOform;
	}
	public Date getSrokIsp() {
		return srokIsp;
	}
	public void setSrokIsp(Date srokIsp) {
		this.srokIsp = srokIsp;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getRegNom() {
		return regNom;
	}
	public void setRegNom(String regNom) {
		this.regNom = regNom;
	}
	public Sotrud getSotrud() {
		return sotrud;
	}
	public void setSotrud(Sotrud sotrud) {
		this.sotrud = sotrud;
	}
	public BigDecimal getStRab() {
		return stRab;
	}
	public void setStRab(BigDecimal stRab) {
		this.stRab = stRab;
	}
	public BigDecimal getStM() {
		return stM;
	}
	public void setStM(BigDecimal stM) {
		this.stM = stM;
	}
}
