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
			log("Сервер стартовал");
			while (true) {
				log("Ожидание нового клиента...");
				Socket soc = ss.accept();
				log("Подсоединился клиент");
				CardService service = new CardService(this, soc);
				log("Создан сервис обслуживания бонусных карт");
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
			msg= "Ошибка. Карта с таким номером существует\n";
			log(msg);
			return msg;
		}
		else {
			ht.put(card.cardNumber, card);
			msg="Создана карта с номером " + card.cardNumber;
			log(msg);
			return msg;
		}
	}
	
	public String addBonus(String card, double money) {
		Card c = (Card) ht.get(card);
		String msg;
		double bonus;
		if (c == null) {
			msg = "Ошибка. Неправильный номер карты\n";
			log(msg);
			return msg;
		};
		bonus = c.procent * money;
		c.balance += bonus;
		ht.put(card, c);
		msg = "Начисление бонусов: " + bonus + " на карту " + card + ". Остаток=" + c.balance + "\n";
		log(msg);
		return msg;
	}
	
	public String subBonus(String card, double bonus) {
		Card c = (Card) ht.get(card);
		String msg;
		if (c == null) {
			msg = "Ошибка. Неправильный номер карты\n";
			log(msg);
			return msg;
		};
		if (c.balance < bonus){
			msg = "Ошибка. Недостаточно бонусов. На карте только " + c.balance + "\n";
			log(msg);
			return msg;
		}	
		else {
			c.balance -= bonus;
			ht.put(card, c);
			msg = "Снятие бонусов " + bonus + " с карты " + card + ". Остаток=" + c.balance + "\n";
			log(msg);
			return msg;
		}
	}
	
	public String getCard(String card) {
		Card c = (Card) ht.get(card);
		if (c == null) {
			String msg= "Ошибка. Неправильный номер карты\n";
			log(msg);
			return msg;
		};
		log("Запрос остатка бонусов по карте " + card);
		return c.toString();
	}
	
	public String setZero(String card) {
		Card c = (Card) ht.get(card);
		String msg;
		if (c == null) {
			msg = "Ошибка. Неправильный номер карты\n";
			log(msg);
			return msg;
		};
		c.balance = 0;
		ht.put(card, c);
		msg = "Обнуление бонусов на карте " + card + "\n";
		log(msg);
		return msg;
	}
	
	private void log (String msg) {
		System.out.println(msg);
	}
}
