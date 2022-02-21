package com.data;

import java.math.BigDecimal;

public class VRabota {
	private BigDecimal idRab = null;
	private Rabota rabota = null;
	private ZakazN zakazN = null;
	private Sotrud sotrud = null;
	private BigDecimal vremCh = null;
	private BigDecimal stoimCh = null;
	private BigDecimal idZn = null;
	
	public VRabota() {
		super();
	}
	
	public VRabota(BigDecimal idRab, Rabota rabota, ZakazN zakazN,
			Sotrud sotrud, BigDecimal vremCh, BigDecimal stoimCh) {
		super();
		this.idRab = idRab;
		this.rabota = rabota;
		this.zakazN = zakazN;
		this.sotrud = sotrud;
		this.vremCh = vremCh;
		this.stoimCh = stoimCh;
	}
	
	public BigDecimal getIdRab() {
		return idRab;
	}
	public void setIdRab(BigDecimal idRab) {
		this.idRab = idRab;
	}
	public Rabota getRabota() {
		return rabota;
	}
	public void setRabota(Rabota rabota) {
		this.rabota = rabota;
	}
	public ZakazN getZakazN() {
		return zakazN;
	}
	public void setZakazN(ZakazN zakazN) {
		this.zakazN = zakazN;
	}
	public Sotrud getSotrud() {
		return sotrud;
	}
	public void setSotrud(Sotrud sotrud) {
		this.sotrud = sotrud;
	}
	public BigDecimal getVremCh() {
		return vremCh;
	}
	public void setVremCh(BigDecimal vremCh) {
		this.vremCh = vremCh;
	}
	public BigDecimal getStoimCh() {
		return stoimCh;
	}
	public void setStoimCh(BigDecimal stoimCh) {
		this.stoimCh = stoimCh;
	}
	
	@Override
	public String toString() {
		return idRab + ", " + rabota.getKodRab() + ", "
				+ zakazN.getIdZn() + ", " + sotrud.getKodSotr()
				+ ", " + vremCh + ", " + stoimCh;
	}

	public BigDecimal getIdZn() {
		return idZn;
	}

	public void setIdZn(BigDecimal idZn) {
		this.idZn = idZn;
	}
	
}
