package com.rpt;

import java.math.BigDecimal;

public class RptData {
	private String model;
	private String month;
	private Integer kolvo;
	private BigDecimal stoimrab;
	private BigDecimal stoimm;
	private BigDecimal avgtime;
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getKolvo() {
		return kolvo;
	}
	public void setKolvo(Integer kolvo) {
		this.kolvo = kolvo;
	}
	public BigDecimal getStoimrab() {
		return stoimrab;
	}
	public void setStoimrab(BigDecimal strab) {
		this.stoimrab = strab;
	}
	public BigDecimal getStoimm() {
		return stoimm;
	}
	public void setStoimm(BigDecimal stm) {
		this.stoimm = stm;
	}
	public BigDecimal getAvgtime() {
		return avgtime;
	}
	public void setAvgtime(BigDecimal avgtime) {
		this.avgtime = avgtime;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
}
