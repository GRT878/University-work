package laba12;

public class Laba12 {	
	public static void main(String[] args) {
		Data data = new Data();
        new Thread(new Producer(data)).start();
        new Thread(new Consumer(data)).start();
	}
}
