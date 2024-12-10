package com.client.app.lib;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * client that connects to a server
 */
public class Client {
	// init socket and input out streams
	private Socket server = null;
	// to send data to server
	private DataOutputStream dos = null;
	// read data from server
	private BufferedReader br = null;
	// read data from keyboard
	private BufferedReader kb = null;

	private volatile String inputStr = "";

	public Client(String address, int port) {
		// establish connection
		try {

			System.out.printf("%sEstablishing connection...%s\n", Colors.YELLOW, Colors.RESET);

			// create client socket
			server = new Socket(address, port);
			System.out.printf("%sConnected!%s\n", Colors.GREEN, Colors.RESET);

			dos = new DataOutputStream(server.getOutputStream());
			br = new BufferedReader(new InputStreamReader(server.getInputStream()));
			kb = new BufferedReader(new InputStreamReader(System.in));

			Thread senderThread = new Thread(() -> {
				try {
					do {
						inputStr = kb.readLine();
						System.out.printf("%sCLIENT:%s %s\n", Colors.YELLOW, Colors.RESET, inputStr);
						dos.writeBytes(inputStr + "\n");
					} while (!inputStr.equals("quit"));
				} catch (IOException ioE) {
					System.out.println(ioE);
					return;
				}
			});

			Thread recieverThread = new Thread(() -> {
				try {
					String recievedData = null;
					do {
						recievedData = br.readLine();
						System.out.printf("%sSERVER:%s %s\n", Colors.BLUE, Colors.RESET, recievedData);
					} while (!inputStr.equals("quit"));
				} catch (IOException ioE) {
					System.out.println(ioE);
					return;
				}
			});

			senderThread.start();
			recieverThread.start();

			// program will wait until both threads stop running
			senderThread.join();
			recieverThread.join();

			// close connection after joining threads
			System.out.printf("%sClosing connection...%s\n", Colors.YELLOW, Colors.RESET);
			dos.close();
			br.close();
			kb.close();
			server.close();

		} catch (IOException | InterruptedException e) {
			System.err.println(String.format("%s %s %s", Colors.RED, e, Colors.RESET));
			return;
		}
	}

}
