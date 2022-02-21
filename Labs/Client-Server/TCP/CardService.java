package laba22;

import java.io.*;
import java.net.*;

public class CardService extends Thread {
	ObjectInputStream ins;
	ObjectOutputStream outs;
	CardServer cs;
	Socket sok;
	
	public CardService(CardServer cs, Socket sok) {	
		this.cs = cs;
		this.sok = sok;
		try {
			this.outs = new ObjectOutputStream(sok.getOutputStream());
			this.ins = new ObjectInputStream(sok.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Работают потоки на сокете " + sok + "\n");
	}

	public void run() {
		System.out.println("Сервис обслуживания бонусных карт стартовал\n");
		boolean work = true;
		String msg;
		Object o;
		while (work) {
			try {
				o = ins.readObject();
				if (o == null) {
					work = false;
				} else if (o instanceof CardOperation) {
					CardOperation co = (CardOperation) o;
					msg = "";
					if (co.operTip == 0) {
						msg = cs.addNewCard(co.card);
						outs.writeObject(msg);
					} else if (co.operTip == 1) {
						msg = cs.addBonus(co.card.cardNumber, co.amount);
						outs.writeObject(msg);
					} else if (co.operTip == 2) {
						msg = cs.subBonus(co.card.cardNumber, co.amount);
						outs.writeObject(msg);
					} else if (co.operTip == 3)
						outs.writeObject(cs.getCard(co.card.cardNumber));
					else if (co.operTip == 4) {
						msg = cs.setZero(co.card.cardNumber);
						outs.writeObject(msg);
					}	
				} else
					System.out.println("Ошибочная операция");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

