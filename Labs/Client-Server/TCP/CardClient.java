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
		CardOperation co = new CardOperation(new Card("����", new Date(), "1", 0.0, 0.1), 0, 0, new Date());
		processOperation(co);
		co = new CardOperation(new Card("�������", new Date(), "2", 0.0, 0.2), 0, 0, new Date());
		processOperation(co);
		co = new CardOperation(new Card("�����", new Date(), "3", 0.0, 0.3), 0, 0, new Date());
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
		log("������ ���������");
	}

	void connectToServer() throws UnknownHostException, IOException {
		s = new Socket(srvName, srvPort);
		log("���������� � �������� �����������\n");
		ins = new ObjectInputStream(s.getInputStream());
		outs = new ObjectOutputStream(s.getOutputStream());
		log("������� ������ ��� ������ �������\n");
	}
	
	void processOperation(CardOperation co) throws IOException,	ClassNotFoundException {
		log("�������� " + co.operTip + " � ������ " + co.card.cardNumber);
		outs.writeObject(co);
		log("���������: "+ (String) ins.readObject());
	}
	
	public static void main(String[] args) throws Exception {
		String serverAddress = JOptionPane.showInputDialog(
				null, "������� IP ����� �������:",
				"���� � ��������� �������� �� ������",
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

