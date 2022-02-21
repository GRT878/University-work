package laba22;

import java.io.Serializable;
import java.util.Date;

public class CardOperation implements Serializable {
	  public Card card;
	  public double amount;
	  public Date operDate;
	  public Integer operTip;

	  public CardOperation(Card card, Integer operTip, double amount, Date operDate){
	       this.card = card;
	       this.amount = amount;
	       this.operDate = operDate; 
	       this.operTip = operTip;
	    }
	}
