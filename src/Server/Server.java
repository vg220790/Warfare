package Server;

import java.io.*;
import java.net.*;
import java.util.Date;

import Database.DBManager;

public class Server {

	private static Server server_instance = null; 
	ServerSocket server = null;
	Socket socket = null;
	DataInputStream inputStream;
	PrintStream outputStream;
	String line = "";
	
	private Server() {
		try {
			server = new ServerSocket(7000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Server getInstance() { 
        if (server_instance == null) 
        	server_instance = new Server(); 
        return server_instance;
	}
	
	public void openServer() {
		try {
			System.out.println(new Date() + " --> Server waits for client...");
			socket = server.accept(); // blocking
			System.out.println(new Date() + " --> Client connected from "
					+ socket.getInetAddress() + ":" + socket.getPort());
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new PrintStream(socket.getOutputStream());
			outputStream.println("Welcome to server!");
			while (!line.equals("goodbye")) {
				line = inputStream.readLine();
				outputStream.println(line);
				System.out.println(new Date() + " --> Recieved from client: "
						+ line);
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				socket.close();
				server.close();
				System.out
						.println("Sever is closing after client is disconnected");
			} catch (IOException e) { }
		}
 	}
}

