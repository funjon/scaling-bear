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

import exception.AutoException;
import model.Automobile;

import java.io.*;
import java.util.*;

public class FileIO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String filename;

	// Nothing really needed for the FileIO object, it's just for utility methods
	public FileIO() {}
	
	public String getFilename () { return filename; }
	
	public void setFilename(String file) throws AutoException {
		File f = new File(file);
		if (!f.exists()) { throw new AutoException(300); } else { this.filename = file; }
	}
	
	// Ingest a specific file and return an automobile object
	public Automobile readFile() throws AutoException {
		File f = new File(filename);
		if (!f.exists()) { throw new AutoException(300); }
		
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
		else if (elements[0].equalsIgnoreCase("optionSet"))      {
			try { auto.addOptionSet(elements[1], Integer.parseInt(elements[2])); }
			catch (AutoException ae) { ae.print(); }
		} 
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
	
	public HashMap<Integer, String> loadErrors() {
		HashMap<Integer, String> errorMap = new HashMap<Integer, String>();
		FileReader file = null;
		
		try {
			file = new FileReader("errorcodes.txt");
			BufferedReader buffread = new BufferedReader(file);
			boolean eof = false;
			while (!eof) {
				String line = "";
				try { line = buffread.readLine(); }
				catch (IOException e) { System.out.println("Error == " + e.toString()); }
				
				if (line == null) { eof = true; }
				else { 
					String elements[] = line.split(",");
					int errcode = Integer.parseInt(elements[0]);
					String errorString = elements[2];
					
					// Put it in the hashmap
					errorMap.put(errcode, errorString);
				}
			}
			try { buffread.close(); } 
			catch (IOException e) { System.out.println("Error -- " + e.toString()); }
		} catch (FileNotFoundException e) { System.out.println("Error -- " + e.toString()); }
		try { file.close(); } 
		catch (IOException e) {
			System.out.printf("Error reading the errorcodes.txt: %s\n", e.toString());
		}
		return errorMap;
	}
	
	public HashMap<String, Integer> loadHandles() {
		HashMap<String, Integer> handleMap = new HashMap<String, Integer>();
		FileReader file = null;
	
		try {
			file = new FileReader("errorcodes.txt");
			BufferedReader buffread = new BufferedReader(file);
			boolean eof = false;
			while (!eof) {
				String line = "";
				try { line = buffread.readLine(); }
				catch (IOException e) { System.out.println("Error == " + e.toString()); }
				
				if (line == null) { eof = true; }
				else { 
					String elements[] = line.split(",");
					int errcode = Integer.parseInt(elements[0]);
					// Not doing anything with this right now, might later
					String errhandle = elements[1];
					
					// Put it in the hashmap
					handleMap.put(errhandle, errcode);
				}
			}
			try { buffread.close(); } 
			catch (IOException e) { System.out.println("Error -- " + e.toString()); }
		} catch (FileNotFoundException e) { System.out.println("Error -- " + e.toString()); }
		try { file.close(); } 
		catch (IOException e) {
			System.out.printf("Error reading the errorcodes.txt: %s\n", e.toString());
		}
		return handleMap;
	}
	
	// Default version
	public void serializeAuto(Automobile auto) { serializeAuto(auto, "automobile.ser"); }
	
	// Allow serialization of multiple objects to different files
	public void serializeAuto(Automobile auto, String serializedAuto) {
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
	public Automobile deserializeAuto() { return deserializeAuto("automobile.ser"); }
	
	// Allow de-serialization of multiple objects from different files
	public Automobile deserializeAuto(String serializedAuto) {
		Automobile auto = null;
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
