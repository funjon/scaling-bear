package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSock = null;
	
	public Server() {
		try {
			// I listen without a specified host because I want to be able to connect on localhost OR a directed IP
			serverSock = new ServerSocket(24601);
			System.out.printf("Opened a socket on 24601/tcp\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		DefaultSocketServer dsc = null;
		
		try {
			while (true) {
				Socket sock;
				sock = serverSock.accept();
				System.out.printf("New connection from %s:%d\n", sock.getInetAddress().getHostAddress(), sock.getPort());
				dsc = new DefaultSocketServer(serverSock, sock);
				System.out.printf("Current models available:\n%s\n", dsc.returnModels());
				dsc.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Server srv = new Server();
		srv.run();
	}
}
