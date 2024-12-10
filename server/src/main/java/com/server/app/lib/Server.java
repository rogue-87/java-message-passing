package com.server.app.lib;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;

public class Server {

	// init socket and input stream
	private ServerSocket server = null;
	private Socket client = null;

	// to send data to client
	private DataOutputStream dos = null;
	// read data from client
	private BufferedReader br = null;
	// read data from keyboard
	private BufferedReader kb = null;

	private volatile String inputStr = "";

	/**
	 * server that waits for client to connect
	 * 
	 * @param port {@link int}
	 */
	public Server(int port) {
		try {
			// create server
			server = new ServerSocket(port);

			System.out.printf("%sServer started!%s\n", Colors.YELLOW, Colors.RESET);
			System.out.printf("%sWaiting for client...%s\n", Colors.YELLOW, Colors.RESET);

			// will wait until a client connects to the server
			client = server.accept();
			System.out.printf("%sConnection established!%s\n", Colors.GREEN, Colors.RESET);

			dos = new DataOutputStream(client.getOutputStream());
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			kb = new BufferedReader(new InputStreamReader(System.in));

			Thread senderThread = new Thread(() -> {
				try {
					do {
						inputStr = kb.readLine();
						System.out.printf("%sSERVER:%s %s\n", Colors.BLUE, Colors.RESET, inputStr);
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
						System.out.printf("%sCLIENT:%s %s\n", Colors.YELLOW, Colors.RESET, recievedData);
					} while (!inputStr.equals("quit"));
				} catch (IOException ioE) {
					System.out.println(ioE);
					return;
				}
			});

			senderThread.start();
			recieverThread.start();

			senderThread.join();
			recieverThread.join();

			// close connection
			System.out.printf("%sClosing connection...%s\n", Colors.YELLOW, Colors.RESET);
			dos.close();
			br.close();
			kb.close();
			server.close();
			client.close();

		} catch (IOException | InterruptedException e) {
			System.err.println(String.format("%s %s %s\n", Colors.RED, e, Colors.RESET));
		}
	}

}
