package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

import exception.AutoException;
import server.BuildCarModelOptions;

//import java.io.*;
//import java.net.*;
//import java.util.ArrayList;

public class DefaultSocketServer extends Thread implements SocketClientInterface {
	private Socket socket;

	ServerSocket serverSock;
	
	ObjectInputStream ois = null;
	
	AutoServer as = new BuildCarModelOptions();

	// These are from DSC, we don't use them here, only use the one constructor that passes two sockets
//	private String sockHost;
//	private int sockPort;
	
//	public DefaultSocketServer(String host, int port) {
//		this.sockHost = host;
//		this.sockPort = port;
//	}
	
	public DefaultSocketServer(ServerSocket serverSock, Socket socket) {
		this.serverSock = serverSock;
		this.socket = socket;
	}
	
	public boolean openConnection() {
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}
	
	public void handleSession() {
		try {
			ois.skip(Long.MAX_VALUE);
			String command = (String) ois.readObject();
			System.out.printf("Client says: %s\n", command);
			
			if (command.equals("UPLOAD")) {
				ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
				Properties props = (Properties) is.readObject();
				as.buildWithProperties(props);
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.flush();
				oos.writeObject("OK");
			} else if (command.equals("CONFIG")) {
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				oos.flush();
				
				// Send the list of models
				ArrayList<String> models = as.getModels();
				oos.writeObject(models);
				oos.flush();
				
				// Now see which one they want
				String model = (String) ois.readObject();
				as.sendAuto(oos, model);
			} else if (command.equals("QUIT")) {
				this.closeSession();
			} else {
				System.err.printf("Client asked for unimplemented command: %s\n", command);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AutoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void closeSession() {
		try {
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String returnModels() { return as.getModels().toString(); }
	
	public void run() {
		if (openConnection()) {
			handleSession();
		}
	}
}
