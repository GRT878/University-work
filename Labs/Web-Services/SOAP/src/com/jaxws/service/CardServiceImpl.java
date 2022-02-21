package com.jaxws.service;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import com.jaxws.bean.Card;

@WebService(endpointInterface = "com.jaxws.service.CardService")  
public class CardServiceImpl implements CardService{
	private static Map<Integer, Card> cards = new HashMap<Integer, Card>();
	
	@Override
	public boolean addCard(Card card) {
		if (cards.get(card.getCardNumber()) != null) return false;
		cards.put(card.getCardNumber(), card);
		return true;
	}

	@Override
	public void addBonus(int number, double money) {
		Card c = (Card)cards.get(number);
		if (c == null) {
			
		} else {
			double bonus = c.getProcent()/100 * money;
			c.setBalance(c.getBalance() + bonus);
		}
	}

	@Override
	public void subBonus(int number, double bonus) {
		Card c = (Card)cards.get(number);
		if (c == null) {
			
		} else {
			c.setBalance(c.getBalance() - bonus);
		}
	}

	@Override
	public boolean setZero(int number) {
		Card c = (Card)cards.get(number);
		if (c == null) {
			return false;
		} else {
			c.setBalance(0);
			return true;
		}	
	}

	@Override
	public Card getCard(int number) {
		Card c = (Card)cards.get(number);
		if (c == null) {
			return null;
		} else {
			return c;
		}
	}

}

