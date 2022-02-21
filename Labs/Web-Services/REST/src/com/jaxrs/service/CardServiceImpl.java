package com.jaxrs.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.jaxrs.bean.Card;
import com.jaxrs.bean.Response;

@Path("/card")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
  
public class CardServiceImpl implements CardService{
	private static Map<Integer, Card> cards = new HashMap<Integer, Card>();
	
	@GET
	@Path("/{number}/test")
	public Card test(@PathParam("number") int number) {
		Card c = new Card();
		c.setPerson("Ivan");
		c.setBalance(200);
		c.setProcent(10);
		c.setCardNumber(number);
		return c;
	}

	@Override
	@POST
	@Path("/add")
	public Response addCard(Card card) {
		Response response = new Response();
		if (cards.get(card.getCardNumber()) != null) {
			response.setStatus(false);
			response.setMessage("Карта уже существует");
		} else {
			cards.put(card.getCardNumber(), card);
			response.setStatus(true);
			response.setMessage("Карта добавлена");
		}
		return response;
	}

	@Override
	@GET
	@Path("/{number}/{money}/addBonus")
	public Response addBonus(@PathParam("number") int number, @PathParam("money") double money) {
		Response response = new Response();
		Card c = (Card)cards.get(number);
		if (c == null) {
			response.setStatus(false);
			response.setMessage("Карта не найдена");
		} else {
			double bonus = c.getProcent()/100 * money;
			c.setBalance(c.getBalance() + bonus);
			response.setStatus(true);
			response.setMessage("Бонусы успешно начислены");
		}
		return response;
	}

	@Override
	@GET
	@Path("/{number}/{bonus}/subBonus")
	public Response subBonus(@PathParam("number") int number, @PathParam("bonus") double bonus) {
		Response response = new Response();
		Card c = (Card)cards.get(number);
		if (c == null) {
			response.setStatus(false);
			response.setMessage("Карта не найдена");
		} else {
			c.setBalance(c.getBalance() - bonus);
			response.setStatus(true);
			response.setMessage("Бонусы успешно списаны");
		}
		return response;
	}

	@Override
	@GET
	@Path("/{number}/setZero")
	public Response setZero(@PathParam("number") int number) {
		Response response = new Response();
		Card c = (Card)cards.get(number);
		if (c == null) {
			response.setStatus(false);
			response.setMessage("Карта не найдена");
		} else {
			c.setBalance(0);
			response.setStatus(true);
			response.setMessage("Карта обнулена");
		}
		return response;	
	}

	@Override
	@GET
	@Path("/{number}/get")
	public Card getCard(@PathParam("number") int number) {
		return cards.get(number);
	}

}


