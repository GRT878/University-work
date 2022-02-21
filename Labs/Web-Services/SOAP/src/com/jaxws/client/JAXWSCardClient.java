package com.jaxws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.jaxws.bean.Card;
import com.jaxws.service.CardService;

public class JAXWSCardClient {
	public static void main(String[] args) throws MalformedURLException {
		URL wsdlURL = new URL("http://localhost:8080/JAX-WS-Card/cards?wsdl");

		QName qname = new QName("http://service.jaxws.com/", "CardServiceImplService"); 
		
		Service service = Service.create(wsdlURL, qname);  

		CardService ps = service.getPort(CardService.class);

		Card c1 = new Card(); 
		c1.setPerson("����");
		c1.setCardNumber(1);
		c1.setCreateDate(new Date());
		c1.setProcent(10);
		c1.setBalance(100);
		
		System.out.println((ps.addCard(c1) ? "����� � ������� 1 ���������." : "����� � ������� 1 ����������")+"\n");

		Card c2 = new Card(); 
		c2.setPerson("�������");
		c2.setCardNumber(2);
		c2.setCreateDate(new Date());
		c2.setProcent(5);
		c2.setBalance(200);
		
		System.out.println((ps.addCard(c2) ? "����� � ������� 2 ���������." : "����� � ������� 2 ����������")+"\n");

		System.out.println("���������� � �����\n" + ps.getCard(1) + "\n");
		
		System.out.println("���������� � �����\n" + ps.getCard(2) + "\n");

		System.out.println("��������� ������� �� ����� 1");
		ps.addBonus(1, 1000);
		System.out.println("���������� � �����\n" + ps.getCard(1) + "\n");
		
		System.out.println("�������� ������� � ����� 2");
		ps.subBonus(2, 10);
		System.out.println("���������� � �����\n" + ps.getCard(2) + "\n");
		
		System.out.println("��������� ����� 1");
		ps.setZero(1);
		System.out.println("���������� � �����\n" + ps.getCard(1) + "\n");
	}	
}
