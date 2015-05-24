/**
 * Jonathan Disher
 * CIS 035b
 * Lab #1 - 
 * jdisher@parad.net
 * 
 * @author jdisher
 */

/* method count - 8 */

package util;

import model.Automobile;

import java.io.*;

public class FileIO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Nothing really needed for the FileIO object, it's just for utility methods
	public FileIO() {}
	
	// Read the default file
	public Automobile readFile() { return readFile("config.txt"); }
	
	// Ingest a specific file and return an automobile object
	public Automobile readFile(String filename) {
		FileReader file = null;
		Automobile auto = new Automobile();
		try {
			file = new FileReader(filename);
			BufferedReader buffread = new BufferedReader(file);
			boolean eof = false;
			while (!eof) {
				String line = "";
				try { line = buffread.readLine(); } 
				catch (IOException e) { System.out.println("Error -- " + e.toString()); }
				
				if (line == null) { eof = true; }
				else { this.parseLine(line,auto); }
			}
			try { buffread.close(); } 
			catch (IOException e) { System.out.println("Error -- " + e.toString()); }
		} catch (FileNotFoundException e) { System.out.println("Error -- " + e.toString()); }
		
		try { file.close(); } 
		catch (IOException e) { System.out.println("Error -- " + e.toString()); }
		
		return auto;
	}
	
	// Parser v1
	public void parseLine(String line, Automobile auto) {
		
		// Build an array of elements from the ingested line
		String elements[] = line.split(",");
		
		// Initial stab at a parser. A second generation parser might use regular expressions.
		     if (elements[0].equalsIgnoreCase("model"))          { auto.setModelName(elements[1]); }
		else if (elements[0].equalsIgnoreCase("price"))          { auto.setCost(Integer.parseInt(elements[1])); } 
		else if (elements[0].equalsIgnoreCase("optionSetCount")) { auto.createOptionSets(Integer.parseInt(elements[1])); }
		else if (elements[0].equalsIgnoreCase("optionSet"))      { auto.addOptionSet(elements[1], Integer.parseInt(elements[2])); } 
		else if (elements[0].equalsIgnoreCase("option")) {
			String optionName = "";
			if (elements.length > 4) {
				for (int i = 3; i < elements.length; i++) {
					optionName += elements[i] + " ";
				}
				optionName += elements[elements.length - 1];
			} else { optionName = elements[3]; }
			
			auto.addOptionToOptionSet(elements[1], optionName, Integer.parseInt(elements[2]));
		} else { System.out.printf("Unrecognized token identified: %s\n", elements[0]); }
	}
		
	// Default version
	public static void serializeAuto(Automobile auto) { serializeAuto(auto, "automobile.ser"); }
	
	// Allow serialization of multiple objects to different files
	public static void serializeAuto(Automobile auto, String serializedAuto) {
		FileOutputStream fileOut = null;
		ObjectOutputStream objectOut = null;
		
		try { 
			fileOut = new FileOutputStream(serializedAuto); 
			objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(auto);
			objectOut.close();
			fileOut.close();
		}
		catch (FileNotFoundException e) { System.out.println("Error -- " + e.toString()); }
		catch (IOException e) { System.out.println("Error -- " + e.toString()); }
	}
	
	// Default version
	public static Automobile deserializeAuto() { return deserializeAuto("automobile.ser"); }
	
	// Allow de-serialization of multiple objects from different files
	public static Automobile deserializeAuto(String serializedAuto) {
		Automobile auto = new Automobile();
		FileInputStream fileIn = null;
		ObjectInputStream objectIn = null;
		
		try { 
			fileIn = new FileInputStream(serializedAuto);
			objectIn = new ObjectInputStream(fileIn);
			auto = (Automobile)objectIn.readObject();
			objectIn.close();
			fileIn.close();
		}
		catch (FileNotFoundException e) { System.out.println("Error -- " + e.toString()); }
		catch (IOException e) { System.out.println("Error -- " + e.toString()); }
		catch (ClassNotFoundException e) { System.out.println("Error -- " + e.toString()); }
				
		return auto;
	}
}
