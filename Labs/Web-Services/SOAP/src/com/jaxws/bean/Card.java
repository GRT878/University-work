package com.jaxws.bean;

import java.io.Serializable;
import java.util.Date;

public class Card implements Serializable{	
	private static final long serialVersionUID = -4558376901618919058L;
	private String person;
	private transient Date createDate;
	private int cardNumber;
	private double balance;
	private double procent;
	
	public Card(String person, Date createDate, int cardNumber, double balance, double procent){
		this.person = person;
		this.createDate = createDate;
		this.cardNumber = cardNumber;
		this.balance = balance; 
		this.procent = procent;
	}
	
	public Card () {}

	public String toString(){
		return "�����: �����=" + cardNumber +
				"\t������=" + balance + "\t������=" +
				person + "\t���� ��������=" + createDate + "\t�������: " + procent;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getProcent() {
		return procent;
	}

	public void setProcent(double procent) {
		this.procent = procent;
	}
}

