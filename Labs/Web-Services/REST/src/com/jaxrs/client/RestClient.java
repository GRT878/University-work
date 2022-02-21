package com.jaxrs.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class RestClient {
	private final String USER_AGENT = "Chrome/90.0.4430.72";

	public static void main(String[] args) throws Exception {

		RestClient http = new RestClient();

		http.sendingGetRequestDummy();

		http.sendingPostRequest(1, "Ivan", 200.0, 10.0);
		http.sendingPostRequest(2, "Dmitry", 100.0, 5.0);

		http.sendingGetRequest("1");
		http.sendingGetRequest("2");

		http.sendingGetRequestAddBonus("1", "1000");
		http.sendingGetRequest("1");
		
		http.sendingGetRequestSubBonus("2", "10");
		http.sendingGetRequest("2");
		
		http.sendingGetRequestSetZero("1");
		http.sendingGetRequest("1");

	}

	private void sendingGetRequestDummy() throws Exception {
		String urlString="http://localhost:8080/RESTcard/card/99/test";
		URL url = new URL(urlString);
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int responseCode = con.getResponseCode();
		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String output;
		StringBuffer response = new StringBuffer();
		
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();
		
		System.out.println(response.toString() + "\n");
	}

	private void sendingGetRequest(String number) throws Exception {
		String urlString;
		urlString= "http://localhost:8080/RESTcard/card/" + number + "/get";

		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int responseCode = con.getResponseCode();
		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String output;
		StringBuffer response = new StringBuffer();
		
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();

		System.out.println(response.toString() + "\n");
	}

	private void sendingPostRequest(Integer cardNumber, String person, Double balance, Double procent) throws Exception {
		String url = "http://localhost:8080/RESTcard/card/add";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");

		String postData = "{\"person\":\"" + person + "\""
				+ ",\"cardNumber\":" + cardNumber.toString()
				+ ",\"balance\":" + balance.toString()
				+ ",\"procent\":" + procent.toString() + "}";

		con.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(con.getOutputStream());

		wr.writeBytes(postData);
		wr.flush();
		wr.close();

		System.out.println("nSending 'POST' request to URL : " + url);
		System.out.println("postData: " + postData);

		int responseCode = con.getResponseCode();
		if (responseCode/100 >= 3) {
			throw new RuntimeException("Failed : HTTP error code : " + responseCode);
		}
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String output;
		StringBuffer response = new StringBuffer();
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();

		System.out.println(response.toString() + "\n");
	}

	private void sendingGetRequestAddBonus(String number, String money) throws Exception {
		String urlString;
		urlString= "http://localhost:8080/RESTcard/card/" + number + "/" + money + "/addBonus";

		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int responseCode = con.getResponseCode();
		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String output;
		StringBuffer response = new StringBuffer();
		
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();

		System.out.println(response.toString() + "\n");
	}
	
	private void sendingGetRequestSubBonus(String number, String bonus) throws Exception {
		String urlString;
		urlString= "http://localhost:8080/RESTcard/card/" + number + "/" + bonus + "/subBonus";

		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int responseCode = con.getResponseCode();
		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String output;
		StringBuffer response = new StringBuffer();
		
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();

		System.out.println(response.toString() + "\n");
	}

	private void sendingGetRequestSetZero(String number) throws Exception {
		String urlString;
		urlString= "http://localhost:8080/RESTcard/card/" + number + "/setZero";

		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		
		int responseCode = con.getResponseCode();
		System.out.println("Sending get request : " + url);
		System.out.println("Response code : " + responseCode);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		String output;
		StringBuffer response = new StringBuffer();
		
		while ((output = in.readLine()) != null) {
			response.append(output);
		}
		in.close();

		System.out.println(response.toString() + "\n");
	}
}

