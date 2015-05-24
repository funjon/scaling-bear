package adapter;

import java.io.IOException;
import java.util.LinkedHashMap;

import exception.AutoException;
import model.Automobile;
import util.FileIO;

public class BuildAuto extends proxyAutomobile implements CreateAuto, UpdateAuto, FixAuto {
	private static String sharedFile;
	private float defaultPrice = 0.0f;
	private static BuildAuto builder = new BuildAuto();
	FileIO fio = new FileIO();
	
	private BuildAuto() { autos = new LinkedHashMap<String, Automobile>(); }
	
	public static BuildAuto getInstance() { return builder; }

	public void updateOpsetName (String modelName, String opsetName, String newName) {
		if (autos.containsKey(modelName)) {
			try { autos.get(modelName).renameOpset(opsetName, newName); }
			catch (AutoException ae) {
				ae.print();
			}
		} else {
			System.out.printf("Unknown model %s!\n", modelName);
		}
	}

	public void updateOptionPrice(String modelName, String opsetName, String optionName, float newPrice) {
		if (autos.containsKey(modelName)) {
			try { autos.get(modelName).updateOptionPrice(opsetName, optionName, newPrice); }
			catch (AutoException ae) { ae.print(); }
		} else {
			System.out.printf("Unknown model %s!\n", modelName);
		}
	}
	
	public float getPrice(String modelName) {
		float price = 0.0f;
		if (autos.containsKey(modelName)) {
			return autos.get(modelName).getTotalCost();
		}
		return price;
	}
	
	public void setOptionChoice(String modelName, String opsetName, String optionName) {
		if (autos.containsKey(modelName)) {
			autos.get(modelName).setOptionChoice(opsetName, optionName);
		}
	}

	public String buildAuto(String filename) {
		FileIO importer = new FileIO();
		Automobile a = null;
		
		try { importer.setFilename(filename); }
		catch (AutoException ae) { 
			ae.print(); 
			ae.fixIssue(); 
			
			try { importer.setFilename(sharedFile); }
			catch (AutoException iae) { iae.print(); }
		}
		
		try { 
			a = importer.readFile();
			// Set the default options
			if (autos.containsKey(a.getModelName())) { System.out.printf("Model %s already exists!\n", a.getModelName()); }
			else { 
				a.setDefaultOptionChoices();
				autos.put(a.getModelName(), a);
			}
		}
		catch (AutoException ae) { ae.print(); }	
		
		return a.getModelName();
	}

	public void printAuto(String modelName) {
		autos.get(modelName).print();
	}
	
//	public String getCurrentModelName() { return a.getModelName(); }

	public void setFilename(String name) {
		sharedFile = name;
	}
	
	public void fixAuto() { /* Sir Not Appearing In This Version Of Code */ }
}
