package laba12;

import java.util.*;

public class Data {
	private final Deque<String> data = new ArrayDeque<String>();

	public String getData() {
		synchronized (data) {
			try {
				if (data.size() == 0) 
					data.wait(1000);
			} catch (Exception e) {}
			return data.poll();
		}
	}
	
	public void putData(String s) {
		synchronized (data) {
			data.add(s);
			data.notify();
		}
	}
}
