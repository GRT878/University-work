package laba11;

public class Laba11 {
	public static void main(String[] args) {
		Shop sh = new Shop(500);
		Customer c1 = new Customer(sh, 3);
		Customer c2 = new Customer(sh, 5);
        
		Thread t1 = new Thread(c1);
		Thread t2 = new Thread(c2);
        
		t1.setName("Покупатель 1");
		t2.setName("Покупатель 2");

		t1.start();
		t2.start();
	}
}
