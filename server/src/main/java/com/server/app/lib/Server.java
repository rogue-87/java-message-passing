package com.server.app.lib;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Server {

	// init socket and input stream
	private ServerSocket server = null;
	private Socket client = null;

	/**
	 * server that waits for client to connect
	 * 
	 * @param port {@link int}
	 */
	public Server(int port) {
		try {
			// create server
			server = new ServerSocket(port);

			System.out.printf("%sServer started!%s\n", Color.YELLOW, Color.RESET);

			System.out.printf("%sWaiting for client...%s\n", Color.YELLOW, Color.RESET);

			// connect server to client
			client = server.accept();
			System.out.printf("%sConnection established!%s\n", Color.GREEN, Color.RESET);

			// send data to client
			PrintStream ps = new PrintStream(client.getOutputStream());
			// read data from client
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

			String line1 = "", line2 = "";

			// reads messages from client until "quit" is sent
			while (!line1.equals("quit")) {
				line1 = br.readLine();
				switch (line1) {
					case "marco":
						System.out.printf("%sCLIENT:%s %s\n", Color.YELLOW, Color.RESET, line1);
						System.out.printf("%sResponding to client...%s\n", Color.BLUE, Color.RESET);
						ps.println(String.format("%sSERVER:%s polo", Color.BLUE, Color.RESET));
						break;
					default:
						System.out.printf("%sCLIENT:%s %s\n", Color.YELLOW, Color.RESET, line1);
						break;
				}
			}
			System.out.printf("%sClosing connection...%s\n", Color.YELLOW, Color.RESET);

			// close connection
			br.close();
			ps.close();
			server.close();
			client.close();

			System.exit(0);

		} catch (IOException ioE) {
			System.err.println(String.format("%s %s %s\n", Color.RED, ioE, Color.RESET));
		}
	}

}
