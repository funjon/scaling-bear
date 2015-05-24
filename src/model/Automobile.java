/**
 * Jonathan Disher
 * CIS 035b
 * Lab #1 - 
 * jdisher@parad.net
 * 
 * @author jdisher
 */

/* method count - 16 */

package model;

import exception.AutoException;
import model.OptionSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Automobile implements Serializable {
	private static final long serialVersionUID = 1L;

	private String _make;
	private String _model;
	private String _modelName; // this comes from the config file
	private float _cost;
	
//	private OptionSet _opset[];
	private ArrayList<OptionSet> _opset;
	private HashMap<String, OptionSet> _opsetHash; // Wouldn't it be better to store these in a HM/LHM?
	
	// Constructors
	public Automobile(String name, int base, int opsetCount) throws AutoException {
		if (name == null || name == "") { throw new AutoException(1); }
		_modelName = name;
		_make = "Generic Motors";
		_model = name;
		_cost = base;
		_opset = new ArrayList<OptionSet>(opsetCount);
		_opsetHash = new HashMap<String, OptionSet>();
	}
	
	// Empty constructor
	public Automobile() throws AutoException { this("NONE",10000,10); }

	/* Getters */
	public String getModelName() { return _modelName; }
	public String getMake() { return _make; }
	public String getModel() { return _model; }
	public float getCost() { return _cost; } // This is the base cost
	public float getTotalCost() {
		float cost = this.getCost();
		for (int opsetIndex = 0; opsetIndex < _opset.size(); opsetIndex++) {
			cost += _opset.get(opsetIndex).getChoicePrice();
		}
		return cost;
	}
	
	/* Setters */
	public synchronized void setCost(int cost) { this._cost = cost; } // should this exist? we get the cost from the config file
	public synchronized void setModelName (String modelName) { this._modelName = modelName; }
	public synchronized void setMake(String make) { this._make = make; }
	public synchronized void setModel(String model) { this._model = model; }
	
	public synchronized void setDefaultOptionChoices() {
		for (int opsetIndex = 0; opsetIndex < _opset.size(); opsetIndex++) {
			_opset.get(opsetIndex).setDefaultChoice();
		}
	}
	
	/* CRUD - Create */
	public synchronized void createOptionSets(int opsetCount) {
		_opset = new ArrayList<OptionSet>(opsetCount);
		_opsetHash = new HashMap<String, OptionSet>();
//		System.out.printf("Created array for %d optionSets\n", opsetCount);
	}
	
	public synchronized void addOptionSet(String opsetName, int optionCount) throws AutoException {
		// No longer need to worry about too many opsets, the ArrayList is extensible
//		if (_opsetCount == _opsets) { throw new AutoException(101); }
//		else { 
		OptionSet os = new OptionSet(opsetName, optionCount);
		_opset.add(os);
		_opsetHash.put(opsetName, os);
//			System.out.printf("Created optionSet %s at index %d with optionCount %d\n", opsetName, _opsetCount, optionCount);
//		}
	}
	
	public synchronized void addOptionSetAtIndex(int index, String opsetName, int optionCount) {
		OptionSet os = new OptionSet(opsetName, optionCount);
		_opset.add(index, os);
		_opsetHash.put(opsetName, os);
	}
	
	public synchronized void addOptionToOptionSet(int index, String option, float cost) {
		try {
			_opset.get(index).createOption(option, cost);
//			System.out.printf("Added option %s to opset %s with cost %.2f\n", option, _opset.get(index).getOpsetName(), cost);
		} catch (AutoException ae) { ae.print(); }
		
	}
	public synchronized void addOptionToOptionSet(String opsetName, String option, float cost) {
		int index = findOpset(opsetName);
		this.addOptionToOptionSet(index, option, cost);
	}
	
	/* CRUD - Retrieve */
	
	
	public int findOpset(String name) {
		int index = 0;
		for (int i = 0; i < _opset.size(); i++) {
			if (_opset.get(i).getOpsetName().equalsIgnoreCase(name)) { index = i; }
		}
		return index;
	}
	
	public OptionSet getOpset(String name) {
		int index = getOpsetIndex(name);
		if (index == -1) { System.out.printf("OptionSet %s not found!\n", name); return null; }
		else return getOpset(index);
	}
	
	public OptionSet getOpset(int index) { return _opset.get(index); }
	
	public int getOpsetIndex(String name) {
		int index = -1;
		
		for (int i = 0; i < _opset.size(); i++) {
			if (name.equalsIgnoreCase(_opset.get(i).getOpsetName())) { index = i; }
		}
		return index;
	}
	
	public float getOptionPrice(int opsetIndex, String optionName) {
		return _opset.get(opsetIndex).getOptionPrice(optionName);
	}
	
	public float getOptionPrice(String opsetName, String optionName) throws AutoException{
		if (opsetName == null || opsetName == "") { throw new AutoException(2); }
		if (optionName == null || optionName == "") { throw new AutoException(3); }
		return getOptionPrice(getOpsetIndex(opsetName), optionName);
	}
	
	public int getOpsetCount() { return _opset.size(); }
	public int getOptionCount(int opsetIndex) { return _opset.get(opsetIndex).getOpsetCount(); }
	public int getOptionCount(String opsetName) { return getOptionCount(getOpsetIndex(opsetName)); }
	
	public synchronized void setOptionChoice(String opsetName, String optionName) {
		System.out.printf("Sleeping 2s for threading testing... [%s set to %s]\n", opsetName, optionName);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.printf("Caught an InterruptedException: %s\n", e.toString());
		}
		
		int opsetIndex = getOpsetIndex(opsetName);
		_opset.get(opsetIndex).setChoice(optionName);
	}
	
	public String getOptionChoice(String opsetName) { 
		int opsetIndex = getOpsetIndex(opsetName);
		return _opset.get(opsetIndex).getChoice();
	}
	
	public float getOptionChoicePrice(String opsetName) {
		int opsetIndex = getOpsetIndex(opsetName);
		return _opset.get(opsetIndex).getChoicePrice();
	}
	
	
	/* CRUD - Update */
	
	public synchronized void updateOptionPrice(String opsetName, String optionName, float newPrice) throws AutoException {
		int opsetIndex = 0, optionIndex = 0;
		
		// Get the index of the opset
		opsetIndex = this.findOpset(opsetName);
		if (opsetIndex == -1) { throw new AutoException(2); }
		else {
			optionIndex = _opset.get(opsetIndex).getOptionIndex(optionName);
			if (optionIndex == -1) { throw new AutoException(3); }
			else { _opset.get(opsetIndex).updateOptionPrice(optionName, newPrice); }
		}
	}
		
	public synchronized void renameOpset(String from, String to) throws AutoException {
		if (from == null || from == "") { throw new AutoException(2); }
		else if (to == null || from == "") { throw new AutoException(2); }
		else if (getOpsetIndex(from) == -1) { throw new AutoException(2); }
		else {
			_opset.get(getOpsetIndex(from)).setOpsetName(to);
		}
	}
	
	/* CRUD - Delete */
	
	/* Utilities */
	
	public void print() {
		System.out.printf("Dumping information for %s %s\n", _make, _modelName);
		System.out.printf("Base cost of $%.2f\n", _cost);
		System.out.printf("There are %d opsets\n", _opset.size());
		for (int i = 0; i < _opset.size(); i++) { _opset.get(i).print(); }
	}
	
}
