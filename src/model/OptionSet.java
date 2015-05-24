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
import java.util.ArrayList;

import exception.AutoException;

class OptionSet implements Serializable {
	private static final long serialVersionUID = 1L;

	private String _opsetName;
	// Obsolete with move to ArrayList
//	private int _optionCount;	// This is the size of the array (_options[]) that will be created
//	private int _optionCurrent;	// This is the number of options currently populated in this OptionSet
	
//	private Option _options[];
	private ArrayList<Option> _options;
	private Option _choice;

	protected String getOpsetName() { return _opsetName; }
	protected void setOpsetName(String _opsetName) { this._opsetName = _opsetName; }
	
	protected int getOpsetCount() { return _options.size(); }
//	protected void setOpsetCount(int count) { _optionCount = count; }
	
	protected int getOpsetCurrent() { return _options.size(); }
//	protected void setOpsetCurrent(int current) { _optionCurrent = current; }
	
	protected String getChoice() { return _choice.getOptionName(); }
	protected float getChoicePrice() { return _choice.getOptionCost(); }
	
	protected void setChoice(String optionName) {
		int index = getOptionIndex(optionName);
		_choice = _options.get(index);
	}
	
	protected void setDefaultChoice() {
		if (_options.size() > 0) {
			_choice = _options.get(0);
		} else {
			System.out.printf("No options to choose from!\n");
		}
	}
	
	protected void createOptionAtIndex(int index, String optionName, float optionCost) { _options.add(index, new Option(optionName, optionCost)); }
	
	protected void deleteOptionSet() {
		// Clear out the array
//		for (int i = 0; i < _options.size(); i++) { _options.get(i).deleteOption(); }
		_options.clear();
		
		// Clear out local items
		this._opsetName = "";
	}
	
	protected int getOptionIndex(String optionName) {
		int index = -1;
		for (int i = 0; i < _options.size(); i++) {
			if (optionName.equalsIgnoreCase(_options.get(i).getOptionName())) { index = i; }
		}
		
		return index;
	}
	
	protected float getOptionPrice(String optionName) {
		return _options.get(this.getOptionIndex(optionName)).getOptionCost();
	}
	protected void createOption(String optionName, float optionCost) throws AutoException {
//		if (_optionCurrent == _optionCount) { throw new AutoException(201);	}
//		else { 
			_options.add(new Option(optionName, optionCost)); 
//			_optionCurrent++;
//		}
	}
	
	protected void updateOptionPrice(String optionName, float newPrice) {
		int optionIndex = this.getOptionIndex(optionName);
		_options.get(optionIndex).setOptionCost(newPrice);
	}
	
	protected void print() {
		System.out.printf("Available options for OptionSet %s [%d]\n", this._opsetName, _options.size());
		for (int i = 0; i < _options.size(); i++) { _options.get(i).print(); }
		System.out.printf("\n");
	}
	
	protected OptionSet() { }
	
	protected OptionSet(String name, int count) {
		this._opsetName = name;
//		this._optionCount = count;
//		this._optionCurrent = 0;
		this._options = new ArrayList<Option>(count);
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