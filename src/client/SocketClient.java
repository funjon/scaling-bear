package client;

import java.io.*;
import java.net.*;
import java.util.*;

import model.Automobile;

public class SocketClient extends Thread implements SocketClientInterface{
	private Socket sock;
	private String host;
	private int port;
	
	private boolean done = false;
	
	private ObjectOutputStream oos = null;
	FileInputStream infile = null;

	public SocketClient(String host, int port) {
		this.host = host;
		this.port = port;
		
		try {
			this.sock = new Socket(host, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SocketClient(Socket socket) { this.sock = socket; }
		
	public boolean openConnection() {
		try {
			sock = new Socket(host, port);
			oos = new ObjectOutputStream(sock.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public void handleSession() {
		BufferedReader cons = new BufferedReader(new InputStreamReader(System.in));
		String command = null;
		
		System.out.printf("Welcome to Jon's Car Configuration Client.\n");
		System.out.printf("Please enter a command (upload, config, quit): ");
		
		try { 
			command = cons.readLine().toUpperCase(); 
		} catch (IOException e) { System.out.printf("%s\n", e.toString()); }
		
		try {
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if (command.equals("UPLOAD")) {
				oos.writeObject(command);
				System.out.printf("Please enter a properties file path: ");
				String file = cons.readLine();
				
				// Load the properties object
				Properties props = new Properties();
				infile = new FileInputStream(file);
				props.load(infile);
				
				// Write it!
				ObjectOutputStream os = new ObjectOutputStream(sock.getOutputStream());
				os.writeObject(props);
				
				sock.shutdownOutput();
				
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				ois.skip(Long.MAX_VALUE);
				String resp = (String) ois.readObject();
				System.out.printf("Server responded with: %s\n", resp);
				ois.close();
			} else if (command.equals("CONFIG")) {
				oos.writeObject(command);
				// Let's get a list of models
				oos.flush();
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				ois.skip(Long.MAX_VALUE);
				ArrayList<String> models = (ArrayList<String>) ois.readObject();
				
				System.out.printf("Available models from server: \n");
				System.out.printf("------------------------------\n");
				for (int index = 0; index < models.size(); index++) {
					System.out.printf("%2d) %s\n", index + 1, models.get(index));
				}			
				System.out.printf("Which model would you like to configure: ");
				int conf = Integer.parseInt(cons.readLine());
				oos.writeObject(models.get(conf - 1));
				oos.flush();
				
				Automobile a = (Automobile) ois.readObject();
				if (a != null) {
					System.out.printf("Received the following auto from the server: %s\n", a.getModelName());
					configureAuto(a);
				}
				
				ois.close();
			} else if (command.equals("QUIT")) {
				oos.writeObject(command);
				oos.close();
				
				this.done = true;
			} else { System.err.printf("Invalid input [%s] entered. Please try again!\n", command); }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeSession() {
		// TODO Auto-generated method stub
		try {
			if (infile != null) { infile.close(); }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (!this.done) {
			if (openConnection()) {
				handleSession();
				closeSession();
			}
		}
	}
	
	public void configureAuto(Automobile a) {
		boolean done = false;
		BufferedReader cons = new BufferedReader(new InputStreamReader(System.in));
		int opsetSelection, optionSelection, index;
		ArrayList<String> opsets = null;
		ArrayList<String> options = null;
		
		System.out.printf("----------------------------------------\n");
		
		while (!done) {
			try {
				// Get the list of opset names
				opsets = a.getOpsetNames();
				System.out.printf("Options:\n--------\n");
				
				// Display the list
				for (index = 1; index <= opsets.size(); index++) {
					System.out.printf("%2d) %s\n", index, opsets.get(index - 1));
				}
				
				// Prompt the user for a choice
				System.out.printf("Which option to change (0 to quit): ");
				opsetSelection = Integer.parseInt(cons.readLine());
				
				// See if we're done
				if (opsetSelection == 0) { done = true; continue; }
				
				// Print the available options in this optionSet
				System.out.printf("\nAvailable options in Option Set %s\n", opsets.get(opsetSelection - 1));
				System.out.printf("-------------------------------------------------------\n");
				options = a.getOptionNames(opsetSelection - 1);
				System.out.printf("Got %d options\n", options.size());
				String selectedOption = a.getOptionChoice(opsets.get(opsetSelection - 1));
				for (index = 1; index <= options.size(); index++) {
					if (options.get(index - 1).equals(selectedOption)) { System.out.printf("[X] "); }
					else { System.out.printf("[ ] "); }
					System.out.printf("%02d) %s\n", index, options.get(index - 1));
				}
				
				// Prompt for more choice
				System.out.printf("Which option to select? ");
				optionSelection = Integer.parseInt(cons.readLine());
				
				// Pass that choice into the object
				a.setOptionChoice(opsets.get(opsetSelection - 1), options.get(optionSelection - 1));
		
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Now print the configured auto
		printConfiguredAuto(a);
	}
	
	public void printConfiguredAuto(Automobile a) {
		ArrayList<String> opsets = a.getOpsetNames();
		float price = a.getCost();
		System.out.printf("Your new %s %s, as configured:\n", a.getMake(), a.getModel());
		System.out.printf("-------------------------------------------------------------------\n");
		System.out.printf("Base price: $%.2f\n\n", a.getCost());
		for (int index = 0; index < opsets.size(); index++) {
			System.out.printf("[%15s] - %35s - $%.2f\n", opsets.get(index), a.getOptionChoice(opsets.get(index)), a.getOptionChoicePrice(opsets.get(index)));
			price += a.getOptionChoicePrice(opsets.get(index));
		}
		System.out.printf("-------------------------------------------------------------------\n");
		System.out.printf("%57s $%.2f\n", "Configured price: ", price);
	}
	
	public static void main(String args[]) {
		SocketClient client = null;
		String host;
		int port;
		
		if (args.length == 2) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		} else {
			host = "127.0.0.1";
			port = 24601;
		}
		
		System.out.printf("Connecting to %s:%d\n", host, port);

		client = new SocketClient(host, port);
		if (client != null) { client.start(); }
		else { System.out.printf("Failed to create a socket!\n"); }
	}
	
}
