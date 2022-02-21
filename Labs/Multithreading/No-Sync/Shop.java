package laba11;

public class Shop {
	private int balance;
	
	public Shop(int balance) {
		this.balance=balance;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public void spisanie(int v) {
		balance = balance - v;
	}
	
	public void zachislenie(int v) {
		balance = balance + v;
	}
}
