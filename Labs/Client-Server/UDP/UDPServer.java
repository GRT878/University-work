package laba21;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.ParseException;
import java.util.ArrayList;

public class UDPServer {	
	public static void main(String args[]) {
		DatagramSocket aSocket = null;
		
		try{
			aSocket = new DatagramSocket(6789);
			
			byte[] buffer = new byte[1000];
			
			System.out.println("Сервер запущен");
			
			while(true){
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				
				aSocket.receive(request);
				
				String m = String.valueOf(countNumbers(new String (request.getData()).trim()));
				
				DatagramPacket reply = new DatagramPacket(
						m.getBytes(), m.getBytes().length,
						request.getAddress(), request.getPort());
				
				aSocket.send(reply); 
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if(aSocket != null) aSocket.close();
		} 
	}
	
	public static int countNumbers(String way){
		int count = 0;
		ArrayList<String> arr = new ArrayList<String>();
		double n;
		
		try {
			FileInputStream fstream = new FileInputStream(way);
			InputStreamReader inpstream = new InputStreamReader(fstream,"UTF-8");
			
			BufferedReader br = new BufferedReader(inpstream);
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				arr.add(strLine);
			}
			
			br.close();
		
			for (int i = 0; i < arr.size(); i++) {
				try {
					n = Double.parseDouble(arr.get(i));
					if (n - (int)n == 0)
						if (n % 3 == 0)
							count++;
				} catch (NumberFormatException ex) { }
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return count;
	}
}
