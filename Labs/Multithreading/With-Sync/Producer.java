package laba12;

import java.io.*;

public class Producer implements Runnable{
	private final Data data;
	
	public Producer(Data data) {
		this.data = data;
	}
	
	@Override
	public void run() {
		try{
			FileInputStream fstream = new FileInputStream("I:/file.txt");
			InputStreamReader inpstream = new InputStreamReader(fstream,"UTF-8");
			
			BufferedReader br = new BufferedReader(inpstream);
			String strLine;
			
			System.out.println("Производитель начал считывать строки.");
			while ((strLine = br.readLine()) != null) {
				System.out.println("\nПроизводитель считал строку."
						+ "\nСтрока: " + strLine);
				data.putData(strLine);
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Ошибка");
		}
		
		data.putData("exit");
		System.out.println("\nПроизводитель считал все строки.");
	}
}
