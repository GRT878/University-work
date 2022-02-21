package laba11;

public class Customer implements Runnable {
	private Shop shop;
	private int count;
	
	public Customer(Shop sh, int count) {
		shop = sh;
		this.count = count;
	}
	
	@Override
	public void run() {
		int balance;
		for (int i = 0; i < count; i++) {
			balance=shop.getBalance();	
			synchronized(shop){
				if (balance != 0)
					makeSpis((int)(Math.random()*100));
				else
					break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {}
		}
		System.out.println(Thread.currentThread().getName() + " �������� ������.");
	}

	private synchronized void makeSpis(int s) {
		int balance=shop.getBalance();
		System.out.println(Thread.currentThread().getName()
				+ " ������ � �������."+ 
				" ������ � ��������="+ balance);
		if (balance >= s) {
			shop.spisanie(s);
			System.out.println(Thread.currentThread().getName()
					+ " �������� �������=" +s
					+ ". ������� ������="+ shop.getBalance());
		} else if (balance != 0) {
			shop.spisanie(balance);
			System.out.println(Thread.currentThread().getName()
					+ " �������� �������=" +balance
					+ ". ������� ������="+ shop.getBalance());
		}
	}
}

