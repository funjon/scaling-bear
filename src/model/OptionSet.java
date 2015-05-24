/**
 * Jonathan Disher
 * CIS 035b
 * Lab #1 - 
 * jdisher@parad.net
 * 
 * @author jdisher
 */

/* OptionSet method count - 13 */
/* Option method count - 8 */

package model;

import java.io.Serializable;

import exception.AutoException;

class OptionSet implements Serializable {
	private static final long serialVersionUID = 1L;

	private String _opsetName;
	private int _optionCount;	// This is the size of the array (_options[]) that will be created
	private int _optionCurrent;	// This is the number of options currently populated in this OptionSet
	private Option _options[];

	protected String getOpsetName() { return _opsetName; }
	protected void setOpsetName(String _opsetName) { this._opsetName = _opsetName; }
	
	protected int getOpsetCount() { return _optionCount; }
	protected void setOpsetCount(int count) { _optionCount = count; }
	
	protected int getOpsetCurrent() { return _optionCurrent; }
	protected void setOpsetCurrent(int current) { _optionCurrent = current; }
	
	protected void createOptionAtIndex(int index, String optionName, float optionCost) { _options[index] = new Option(optionName, optionCost); }
	
	protected void deleteOptionSet() {
		// Clear out the array
		for (int i = 0; i < _optionCurrent; i++) { _options[i].deleteOption(); }
		
		// Clear out local items
		this._opsetName = "";
		this._optionCurrent = 0;
	}
	
	protected int getOptionIndex(String optionName) {
		int index = -1;
		for (int i = 0; i < _optionCurrent; i++) {
			if (optionName.equalsIgnoreCase(_options[i].getOptionName())) { index = i; }
		}
		
		return index;
	}
	
	protected float getOptionPrice(String optionName) {
		return _options[this.getOptionIndex(optionName)].getOptionCost();
	}
	protected void createOption(String optionName, float optionCost) throws AutoException {
		if (_optionCurrent == _optionCount) { throw new AutoException(201);	}
		else { _options[_optionCurrent] = new Option(optionName, optionCost); _optionCurrent++; }
	}
	
	protected void updateOptionPrice(String optionName, float newPrice) {
		int optionIndex = this.getOptionIndex(optionName);
		_options[optionIndex].setOptionCost(newPrice);
	}
	
	protected void print() {
		System.out.printf("Available options for OptionSet %s [%d]\n", this._opsetName, _optionCount);
		for (int i = 0; i < _optionCount; i++) { _options[i].print(); }
		System.out.printf("\n");
	}
	
	protected OptionSet() { }
	
	protected OptionSet(String name, int count) {
		this._opsetName = name;
		this._optionCount = count;
		this._optionCurrent = 0;
		this._options = new Option[this._optionCount];
	}
	
	// The inner class, Option
	protected class Option implements Serializable {
		private static final long serialVersionUID = 1L;

		private String _optionName;
		private float _optionCost;

		protected String getOptionName() { return _optionName; }
		protected float getOptionCost() { return _optionCost; }
		
		protected void setOptionName(String name) { this._optionName = name; }
		protected void setOptionCost(float cost) { this._optionCost = cost; }
		
		protected void deleteOption() { this._optionName = ""; this._optionCost = 0; }
		
		protected void print() { System.out.printf("Option %s has price $%.2f\n", _optionName, _optionCost); }
		
		protected Option(String name, float cost) {
			this._optionName = name;
			this._optionCost = cost;
		}
		
		protected Option() { }
	}
}