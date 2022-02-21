package laba22;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class CardClient {
	int tipOper; 
	int srvPort = 7896;
	String srvName;
	Socket s;
	ObjectInputStream ins;
	ObjectOutputStream outs;

	public CardClient(String serverName) {
		this.srvName = serverName;
	}

	public void startTest() throws IOException, ClassNotFoundException {
		connectToServer();
		CardOperation co = new CardOperation(new Card("Иван", new Date(), "1", 0.0, 0.1), 0, 0, new Date());
		processOperation(co);
		co = new CardOperation(new Card("Алексей", new Date(), "2", 0.0, 0.2), 0, 0, new Date());
		processOperation(co);
		co = new CardOperation(new Card("Ольга", new Date(), "3", 0.0, 0.3), 0, 0, new Date());
		processOperation(co);
		co = new CardOperation(new Card("Nataly", new Date(), "3", 0.0, 0.1), 0, 0, new Date());
		processOperation(co);
		co = new CardOperation(new Card(null, null, "1", 0, 0), 1, 1200, new Date());
		processOperation(co);
		co = new CardOperation(new Card(null, null, "2", 0, 0), 1, 300, new Date());
		processOperation(co);
		co = new CardOperation(new Card(null, null, "3", 0, 0), 1, 500, new Date());
		processOperation(co);
		co = new CardOperation(new Card(null, null, "3", 0, 0), 2, 7, new Date());
		processOperation(co);
		co = new CardOperation(new Card(null, null, "3", 0, 0), 2, 50, new Date());
		processOperation(co);
		co = new CardOperation(new Card(null, null, "1", 0, 0), 4, 0, new Date());
		processOperation(co);
		co = new CardOperation(new Card(null, null, "1", 0, 0), 3, 0, new Date());
		processOperation(co);
		co = new CardOperation(new Card(null, null, "2", 0, 0), 3, 0, new Date());
		processOperation(co);
		co = new CardOperation(new Card(null, null, "3", 0, 0), 3, 0, new Date());
		processOperation(co);
		outs.writeObject(null);
		s.close();
		log("Работа завершена");
	}

	void connectToServer() throws UnknownHostException, IOException {
		s = new Socket(srvName, srvPort);
		log("Соединение с сервером установлено\n");
		ins = new ObjectInputStream(s.getInputStream());
		outs = new ObjectOutputStream(s.getOutputStream());
		log("Созданы потоки для обмена данными\n");
	}
	
	void processOperation(CardOperation co) throws IOException,	ClassNotFoundException {
		log("Операция " + co.operTip + " с картой " + co.card.cardNumber);
		outs.writeObject(co);
		log("Результат: "+ (String) ins.readObject());
	}
	
	public static void main(String[] args) throws Exception {
		String serverAddress = JOptionPane.showInputDialog(
				null, "Введите IP адрес сервера:",
				"Вход в программу операций по картам",
				JOptionPane.PLAIN_MESSAGE);
		CardClient bc = new CardClient(serverAddress);
		try {
			bc.startTest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void log (String msg) {
		System.out.println(msg);
	}	
}

