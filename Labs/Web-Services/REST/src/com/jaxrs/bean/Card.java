package com.jaxrs.bean;

import java.util.Date;

public class Card{
	private String person;
	private int cardNumber;
	private double balance;
	private double procent;

	@Override
	public String toString(){
		return "Карта: Номер=" + cardNumber +
				"\tБаланс=" + balance + "\tКлиент=" +
				person + "\tПроцент: " + procent;
	}
	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
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
