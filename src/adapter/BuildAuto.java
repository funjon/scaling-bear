package adapter;

import java.io.IOException;

import exception.AutoException;
import model.Automobile;
import util.FileIO;

public class BuildAuto extends proxyAutomobile implements CreateAuto, UpdateAuto, FixAuto {
	private static String sharedFile;
	private float defaultPrice = 0.0f;
	private static BuildAuto builder = new BuildAuto();
	FileIO fio = new FileIO();
	
	private BuildAuto() { }
	
	public static BuildAuto getInstance() { return builder; }

	public void updateOpsetName (String modelName, String opsetName, String newName) {
		if (a.getModelName().equalsIgnoreCase(modelName)) {
			try { a.renameOpset(opsetName, newName); }
			catch (AutoException ae) {
				ae.print();
			}
		}
	}

	public void updateOptionPrice(String modelName, String opsetName, String optionName, float newPrice) {
		if (a.getModelName().equalsIgnoreCase(modelName)) {
			try { a.updateOptionPrice(opsetName, optionName, newPrice); }
			catch (AutoException ae) { ae.print(); }
		}
	}

	public void buildAuto(String filename) {
		FileIO importer = new FileIO();
		
		try { importer.setFilename(filename); }
		catch (AutoException ae) { 
			ae.print(); 
			ae.fixIssue(); 
			
			try { importer.setFilename(sharedFile); }
			catch (AutoException iae) { iae.print(); }
		}
		
		try { a = importer.readFile(); }
		catch (AutoException ae) { ae.print(); }		
	}

	public void printAuto(String modelName) {
		a.print();
	}
	
	public String getCurrentModelName() { return a.getModelName(); }

	public void setFilename(String name) {
		sharedFile = name;
	}
	
	public void fixAuto() { /* Sir Not Appearing In This Version Of Code */ }
}
