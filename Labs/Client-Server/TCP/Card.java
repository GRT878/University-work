package laba22;

import java.io.Serializable;
import java.util.Date;

public class Card implements Serializable{	
	public String person;
	public transient Date createDate;
	public String cardNumber;
	public double balance;
	public double procent;
	
	public Card(String person, Date createDate, String cardNumber, double balance, double procent){
		this.person = person;
		this.createDate = createDate;
		this.cardNumber = cardNumber;
		this.balance = balance; 
		this.procent = procent;
	}

	public String toString(){
		return "�����: �����=" + cardNumber +
				"\t������=" + balance + "\t������=" +
				person + "\t���� ��������=" + createDate + "\t�������: " + procent;
	}
}
