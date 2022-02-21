package com.jaxws.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import com.jaxws.bean.Card;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)

public interface CardService {
	@WebMethod
	public boolean addCard(Card card);
	@WebMethod
	public void addBonus(int number, double money);
	@WebMethod
	public void subBonus(int number, double bonus);
	@WebMethod
	public boolean setZero(int number);
	@WebMethod
	public Card getCard(int number);
}