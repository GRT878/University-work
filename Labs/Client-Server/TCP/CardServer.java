package laba22;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class CardServer extends Thread {
	private int srvPort = 7896;
	private ServerSocket ss;
	private Hashtable<String,Card> ht;
	
	public CardServer() {
		ht = new Hashtable<String,Card> ();
	}

	public static void main(String[] args) {
		CardServer cs = new CardServer();
		cs.start();
	}

	public void run() {
		try {
			ss = new ServerSocket(srvPort);
			log("������ ���������");
			while (true) {
				log("�������� ������ �������...");
				Socket soc = ss.accept();
				log("������������� ������");
				CardService service = new CardService(this, soc);
				log("������ ������ ������������ �������� ����");
				service.start();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String addNewCard(Card card) {
		Card c = (Card) ht.get(card.cardNumber);
		String msg;
		if (c != null) {
			msg= "������. ����� � ����� ������� ����������\n";
			log(msg);
			return msg;
		}
		else {
			ht.put(card.cardNumber, card);
			msg="������� ����� � ������� " + card.cardNumber;
			log(msg);
			return msg;
		}
	}
	
	public String addBonus(String card, double money) {
		Card c = (Card) ht.get(card);
		String msg;
		double bonus;
		if (c == null) {
			msg = "������. ������������ ����� �����\n";
			log(msg);
			return msg;
		};
		bonus = c.procent * money;
		c.balance += bonus;
		ht.put(card, c);
		msg = "���������� �������: " + bonus + " �� ����� " + card + ". �������=" + c.balance + "\n";
		log(msg);
		return msg;
	}
	
	public String subBonus(String card, double bonus) {
		Card c = (Card) ht.get(card);
		String msg;
		if (c == null) {
			msg = "������. ������������ ����� �����\n";
			log(msg);
			return msg;
		};
		if (c.balance < bonus){
			msg = "������. ������������ �������. �� ����� ������ " + c.balance + "\n";
			log(msg);
			return msg;
		}	
		else {
			c.balance -= bonus;
			ht.put(card, c);
			msg = "������ ������� " + bonus + " � ����� " + card + ". �������=" + c.balance + "\n";
			log(msg);
			return msg;
		}
	}
	
	public String getCard(String card) {
		Card c = (Card) ht.get(card);
		if (c == null) {
			String msg= "������. ������������ ����� �����\n";
			log(msg);
			return msg;
		};
		log("������ ������� ������� �� ����� " + card);
		return c.toString();
	}
	
	public String setZero(String card) {
		Card c = (Card) ht.get(card);
		String msg;
		if (c == null) {
			msg = "������. ������������ ����� �����\n";
			log(msg);
			return msg;
		};
		c.balance = 0;
		ht.put(card, c);
		msg = "��������� ������� �� ����� " + card + "\n";
		log(msg);
		return msg;
	}
	
	private void log (String msg) {
		System.out.println(msg);
	}
}
