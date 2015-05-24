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

import model.OptionSet;
import java.io.Serializable;

public class Automobile implements Serializable {
	private static final long serialVersionUID = 1L;

	private String _modelName;
	private int _cost;
	private int _opsets;
	private int _opsetCount;
	
	private OptionSet _opset[];
	
	public String getModelName() { return _modelName; }
	public int getCost() { return _cost; }
	public int getOpsetIndex(String name) {
		int index = -1;
		
		for (int i = 0; i < _opsetCount; i++) {
			if (name.equalsIgnoreCase(_opset[i].getOpsetName())) { index = i; }
		}
		return index;
	}
	public OptionSet getOpset(int index) { return _opset[index]; }
	public OptionSet getOpset(String name) {
		int index = getOpsetIndex(name);
		if (index == -1) { System.out.printf("OptionSet %s not found!\n", name); return null; }
		else return getOpset(index);
	}
	
	public void renameOpset(String from, String to) {
		int index = getOpsetIndex(from);
		if (index == -1) {
			System.out.printf("OptionSet %s not found!\n", from);
		} else {
			_opset[index].setOpsetName(to);
		}
	}
	public void setModelName (String name) { this._modelName = name; System.out.printf("Set model name to %s\n", name); }
	public void setCost(int cost) { this._cost = cost; System.out.printf("Set base cost to $%d\n", cost); }
	
	public void createOptionSets(int opsetCount) {
		_opsets = opsetCount;
		_opsetCount = 0;
		_opset = new OptionSet[opsetCount];
//		for (int i = 0; i < _opsetCount; i++) { _opset[i] = new OptionSet(); }
		System.out.printf("Created array for %d optionSets\n", opsetCount);
	}
	
	public void addOptionSet(String opsetName, int optionCount) {
		if (_opsetCount == _opsets) { System.out.printf("Maximum opsets reached!\n"); return; }
		_opset[_opsetCount] = new OptionSet(opsetName, optionCount);
		System.out.printf("Created optionSet %s at index %d with optionCount %d\n", opsetName, _opsetCount, optionCount);
		_opsetCount++;
	}
	
	public void addOptionSetAtIndex(int index, String opsetName, int optionCount) {
		_opset[index] = new OptionSet(opsetName, optionCount);
	}
	
	public void addOptionToOptionSet(String opsetName, String option, int cost) {
		int index = findOpset(opsetName);
		_opset[index].createOption(option, cost);
		System.out.printf("Added option %s to opset %s with cost %d\n", option, opsetName, cost);
	}
	
	public int findOpset(String name) {
		int index = 0;
		for (int i = 0; i < _opsets; i++) {
			if (_opset[i].getOpsetName().equalsIgnoreCase(name)) { index = i; }
		}
		return index;
	}
	
	public void print() {
		System.out.printf("Dumping information for model %s\n", _modelName);
		System.out.printf("Base cost of %d\n", _cost);
		System.out.printf("There are %d opsets\n", _opsets);
		for (int i = 0; i < _opsets; i++) { _opset[i].print(); }
	}

	// Constructor
	public Automobile(String name, int base, int opsetCount) {
		_modelName = name;
		_cost = base;
		_opsets = opsetCount;
		_opset = new OptionSet[_opsets];
		_opsetCount = 0;
	}
	
	// Empty constructor
	public Automobile() {}
	
}
