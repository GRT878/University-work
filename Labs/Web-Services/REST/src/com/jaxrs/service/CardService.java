package com.jaxrs.service;

import com.jaxrs.bean.Card;
import com.jaxrs.bean.Response;

public interface CardService {
	public Response addCard(Card card);
	public Response addBonus(int number, double money);
	public Response subBonus(int number, double bonus);
	public Response setZero(int number);
	public Card getCard(int number);
	public Card test(int number);
}
