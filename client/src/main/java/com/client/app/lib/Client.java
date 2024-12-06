package com.client.app.lib;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * client that connects to a server
 */
public class Client {
	// init socket and input out streams
	private Socket socket = null;
	private DataOutputStream dos = null;
	private BufferedReader br = null;
	private BufferedReader kb = null;

	public Client(String address, int port) {
		// establish connection
		try {

			System.out.printf("%sEstablishing connection...%s\n", Color.YELLOW, Color.RESET);
			// create client socket
			socket = new Socket(address, port);
			System.out.printf("%sConnected!%s\n", Color.GREEN, Color.RESET);

			// send data to server
			dos = new DataOutputStream(socket.getOutputStream());
			// read data coming from server
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// read data from keyboard
			kb = new BufferedReader(new InputStreamReader(System.in));

			// string to read message from input
			String line1 = "", line2 = "";

			// keep reading until "quit" is input
			do {
				line1 = kb.readLine();
				switch (line1) {
					case "marco":
						dos.writeBytes(line1 + "\n");
						line2 = br.readLine();
						System.out.println(line2);
						break;
					default:
						// send to the server
						dos.writeBytes(line1 + "\n");
						break;
				}
			} while (!line1.equals("quit"));

			dos.close();
			br.close();
			kb.close();
			socket.close();

		} catch (UnknownHostException uhE) {

			System.err.println(String.format("%s %s %s", Color.RED, uhE.toString(), Color.RESET));
			return;
		} catch (IOException ioE) {

			System.err.println(String.format("%s %s %s", Color.RED, ioE.toString(), Color.RESET));
			return;
		}

	}

}
