package no.hvl.dat110.aciotdevice.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {

	public RestClient() {
		// TODO Auto-generated constructor stub
	}

	private static String logpath = "/accessdevice/log";
	
	public void doPostAccessEntry(String message) {

		// TODO: implement a HTTP POST on the service to post the message

			int port = 8080;
			 String host = "localhost";
			 String uri = logpath;

			try (Socket s = new Socket(host, port)) {

				// construct the HTTP request
				
				String jsonbody = new Gson().toJson(message);

				String httpputrequest = 
						"POST " + uri + " HTTP/1.1\r\n" + 
				        "Host: " + host + "\r\n" +
						"Content-type: application/json\r\n" + 
				        "Content-length: " + jsonbody.length() + "\r\n" +
						"Connection: close\r\n" + 
				        "\r\n" + 
						jsonbody + 
						"\r\n";

				// send the response over the TCP connection
				OutputStream output = s.getOutputStream();

				PrintWriter pw = new PrintWriter(output, false);
				pw.print(httpputrequest);
				pw.flush();

				// read the HTTP response
				InputStream in = s.getInputStream();

				Scanner scan = new Scanner(in);
				StringBuilder jsonresponse = new StringBuilder();
				boolean header = true;

				while (scan.hasNext()) {

					String nextline = scan.nextLine();

					if (header) {
						System.out.println(nextline);
					} else {
						jsonresponse.append(nextline);
					}

					if (nextline.isEmpty()) {
						header = false;
					}

				}

				System.out.println("BODY:");
				System.out.println(jsonresponse.toString());

				scan.close();

			} catch (IOException ex) {
				System.err.println(ex);
			}

	}
	
	private static String codepath = "/accessdevice/code";
	
	public AccessCode doGetAccessCode() {

		AccessCode code = null;
		
		// TODO: implement a HTTP GET on the service to get current access code
		int port = 8080;
		String host = "localhost";
		String uri = codepath;
		try (Socket s = new Socket(host, port)) {

			// construct the GET request
			String httpgetrequest = "GET " + uri + " HTTP/1.1\r\n" + "Accept: application/json\r\n"
					+ "Host: localhost\r\n" + "Connection: close\r\n" + "\r\n";

			// sent the HTTP request
			OutputStream output = s.getOutputStream();

			PrintWriter pw = new PrintWriter(output, false);

			pw.print(httpgetrequest);
			pw.flush();

			// read the HTTP response
			InputStream in = s.getInputStream();

			Scanner scan = new Scanner(in);
			StringBuilder jsonresponse = new StringBuilder();
			boolean header = true;

			while (scan.hasNext()) {

				String nextline = scan.nextLine();

				if (header) {
					System.out.println(nextline);
				} else {
					jsonresponse.append(nextline);
				}

				// simplified approach to identifying start of body: the empty line
				if (nextline.isEmpty()) {
					header = false;
				}

			}

			System.out.println("BODY:");
			System.out.println(jsonresponse.toString());
			code = new Gson().fromJson(jsonresponse.toString(), AccessCode.class);
			scan.close();

		} catch (IOException ex) {
			System.err.println(ex);
		}
		
		return code;
	}
}

