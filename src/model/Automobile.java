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

public class Automobile implements Serializable {
	private static final long serialVersionUID = 1L;

	private String _modelName;
	private float _cost;
	private int _opsets;
	private int _opsetCount;
	
	private OptionSet _opset[];
	
	// Constructors
	public Automobile(String name, int base, int opsetCount) throws AutoException {
		if (name == null || name == "") { throw new AutoException(1); }
		_modelName = name;
		_cost = base;
		_opsets = opsetCount;
		_opset = new OptionSet[_opsets];
		_opsetCount = 0;
	}
	
	// Empty constructor
	public Automobile() throws AutoException { this("NONE",10000,10); }

	/* Getters */
	public float getCost() { return _cost; }	
	public String getModelName() { return _modelName; }
	
	/* Setters */
	public void setModelName (String name) { this._modelName = name; }
	public void setCost(int cost) { this._cost = cost; }
	
	/* CRUD - Create */
	public void createOptionSets(int opsetCount) {
		_opsets = opsetCount;
		_opsetCount = 0;
		_opset = new OptionSet[opsetCount];
//		System.out.printf("Created array for %d optionSets\n", opsetCount);
	}
	
	public void addOptionSet(String opsetName, int optionCount) throws AutoException {
		if (_opsetCount == _opsets) { throw new AutoException(101); }
		else { 
			_opset[_opsetCount] = new OptionSet(opsetName, optionCount);
//			System.out.printf("Created optionSet %s at index %d with optionCount %d\n", opsetName, _opsetCount, optionCount);
			_opsetCount++;
		}
	}
	
	public void addOptionSetAtIndex(int index, String opsetName, int optionCount) {
		_opset[index] = new OptionSet(opsetName, optionCount);
	}
	
	public void addOptionToOptionSet(int index, String option, float cost) {
		try {
			_opset[index].createOption(option, cost);
//			System.out.printf("Added option %s to opset %s with cost %.2f\n", option, _opset[index].getOpsetName(), cost);
		} catch (AutoException ae) { ae.print(); }
		
	}
	public void addOptionToOptionSet(String opsetName, String option, float cost) {
		int index = findOpset(opsetName);
		this.addOptionToOptionSet(index, option, cost);
	}
	
	/* CRUD - Retrieve */
	
	public int findOpset(String name) {
		int index = 0;
		for (int i = 0; i < _opsets; i++) {
			if (_opset[i].getOpsetName().equalsIgnoreCase(name)) { index = i; }
		}
		return index;
	}
	
	public OptionSet getOpset(String name) {
		int index = getOpsetIndex(name);
		if (index == -1) { System.out.printf("OptionSet %s not found!\n", name); return null; }
		else return getOpset(index);
	}
	
	public OptionSet getOpset(int index) { return _opset[index]; }
	
	public int getOpsetIndex(String name) {
		int index = -1;
		
		for (int i = 0; i < _opsetCount; i++) {
			if (name.equalsIgnoreCase(_opset[i].getOpsetName())) { index = i; }
		}
		return index;
	}
	
	public float getOptionPrice(int opsetIndex, String optionName) {
		return _opset[opsetIndex].getOptionPrice(optionName);
	}
	
	public float getOptionPrice(String opsetName, String optionName) throws AutoException{
		if (opsetName == null || opsetName == "") { throw new AutoException(2); }
		if (optionName == null || optionName == "") { throw new AutoException(3); }
		return getOptionPrice(getOpsetIndex(opsetName), optionName);
	}
	
	public int getOpsetCount() { return _opsetCount; }
	public int getOptionCount(int opsetIndex) { return _opset[opsetIndex].getOpsetCount(); }
	public int getOptionCount(String opsetName) { return getOptionCount(getOpsetIndex(opsetName)); }
	
	/* CRUD - Update */
	
	public void updateOptionPrice(String opsetName, String optionName, float newPrice) throws AutoException {
		int opsetIndex = 0, optionIndex = 0;
		
		// Get the index of the opset
		opsetIndex = this.findOpset(opsetName);
		if (opsetIndex == -1) { throw new AutoException(2); }
		else {
			optionIndex = _opset[opsetIndex].getOptionIndex(optionName);
			if (optionIndex == -1) { throw new AutoException(3); }
			else { _opset[opsetIndex].updateOptionPrice(optionName, newPrice); }
		}
	}
		
	public void renameOpset(String from, String to) throws AutoException {
		if (from == null || from == "") { throw new AutoException(2); }
		else if (to == null || from == "") { throw new AutoException(2); }
		else if (getOpsetIndex(from) == -1) { throw new AutoException(2); }
		else {
			_opset[getOpsetIndex(from)].setOpsetName(to);
		}
	}
	
	/* CRUD - Delete */
	
	/* Utilities */
	
	public void print() {
		System.out.printf("Dumping information for model %s\n", _modelName);
		System.out.printf("Base cost of $%.2f\n", _cost);
		System.out.printf("There are %d opsets\n", _opsets);
		for (int i = 0; i < _opsets; i++) { _opset[i].print(); }
	}
	
}
