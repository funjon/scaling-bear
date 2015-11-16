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
import util.fileType;

import java.io.*;
import java.util.*;

public class FileIO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String filename;
	private fileType type;

	// Nothing really needed for the FileIO object, it's just for utility methods
	public FileIO() {}
	
	public String getFilename () { return filename; }
	
	public void setFilename(String file) throws AutoException {
		File f = new File(file);
		if (!f.exists()) { throw new AutoException(300); } else { this.filename = file; }
		// Let's try to set the filetype correctly
		if (filename.toLowerCase().contains("props")) { this.setFiletype(fileType.PROPS); } else { this.setFiletype(fileType.TXT); }
	}
	
	public void setFiletype(fileType type) { this.type = type; }
	
	// Now we support multiple file types
	public Automobile readFile() throws AutoException {
		switch (type) {
			case TXT: return readPlaintext(); 
			case PROPS: return readProperties(); 
			default: return null;
		}
	}
	
	// Ingest a specific file and return an automobile object
	public Automobile readPlaintext() throws AutoException {
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
	
	// Parser for a properties file
	public Automobile readProperties() throws AutoException {
		Properties props = new Properties();
		FileInputStream infile;
		Automobile a = null;
		
		try {
			infile = new FileInputStream(this.filename);
			props.load(infile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new AutoException(300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.printf("Failed to read the properties file in: %s\n", e.toString());
		}
		
		// Make sure we have some keys
		if (!props.getProperty("CarModel").equals(null)) {
			// We have an auto
			a = loadProperties(props);
		}
		
		return a;
	}
	
	// We need this for when we get a serialized properties file via client/server code
	public Automobile loadProperties(Properties props) throws AutoException {
		Automobile a = new Automobile();
		
		// Basic properties
		a.setModel(props.getProperty("CarModel"));
		a.setCost(Integer.parseInt(props.getProperty("BasePrice")));
		
		// I hate the way properties files work here. My plain-text parser is a little more flexible, 
		// because I don't need to have key names with static positions like these. It just feels clunkier.
		
		// Figure out how many optionsets
		a.createOptionSets(Integer.parseInt(props.getProperty("OptionCount")));
		
		// Color
		a.addOptionSet(props.getProperty("Option1"), Integer.parseInt(props.getProperty("Option1Count")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1a"), Integer.parseInt(props.getProperty("OptionCost1a")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1b"), Integer.parseInt(props.getProperty("OptionCost1b")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1c"), Integer.parseInt(props.getProperty("OptionCost1c")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1d"), Integer.parseInt(props.getProperty("OptionCost1d")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1e"), Integer.parseInt(props.getProperty("OptionCost1e")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1f"), Integer.parseInt(props.getProperty("OptionCost1f")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1g"), Integer.parseInt(props.getProperty("OptionCost1g")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1h"), Integer.parseInt(props.getProperty("OptionCost1h")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1i"), Integer.parseInt(props.getProperty("OptionCost1i")));
		a.addOptionToOptionSet(props.getProperty("Option1"), props.getProperty("OptionName1j"), Integer.parseInt(props.getProperty("OptionCost1j")));
		
		// Transmission
		a.addOptionSet(props.getProperty("Option2"), Integer.parseInt(props.getProperty("Option2Count")));
		a.addOptionToOptionSet(props.getProperty("Option2"), props.getProperty("OptionName2a"), Integer.parseInt(props.getProperty("OptionCost2a")));
		a.addOptionToOptionSet(props.getProperty("Option2"), props.getProperty("OptionName2b"), Integer.parseInt(props.getProperty("OptionCost2b")));
		
		// Brakes
		a.addOptionSet(props.getProperty("Option3"), Integer.parseInt(props.getProperty("Option3Count")));
		a.addOptionToOptionSet(props.getProperty("Option3"), props.getProperty("OptionName3a"), Integer.parseInt(props.getProperty("OptionCost3a")));
		a.addOptionToOptionSet(props.getProperty("Option3"), props.getProperty("OptionName3b"), Integer.parseInt(props.getProperty("OptionCost3b")));
		a.addOptionToOptionSet(props.getProperty("Option3"), props.getProperty("OptionName3c"), Integer.parseInt(props.getProperty("OptionCost3c")));
		
		// Side airbags
		a.addOptionSet(props.getProperty("Option4"), Integer.parseInt(props.getProperty("Option4Count")));
		a.addOptionToOptionSet(props.getProperty("Option4"), props.getProperty("OptionName4a"), Integer.parseInt(props.getProperty("OptionCost4a")));
		a.addOptionToOptionSet(props.getProperty("Option4"), props.getProperty("OptionName4b"), Integer.parseInt(props.getProperty("OptionCost4b")));

		// Moonroof
		a.addOptionSet(props.getProperty("Option5"), Integer.parseInt(props.getProperty("Option5Count")));
		a.addOptionToOptionSet(props.getProperty("Option5"), props.getProperty("OptionName5a"), Integer.parseInt(props.getProperty("OptionCost5a")));
		a.addOptionToOptionSet(props.getProperty("Option5"), props.getProperty("OptionName5b"), Integer.parseInt(props.getProperty("OptionCost5b")));

		return a;
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
