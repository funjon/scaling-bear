/**
 * Jonathan Disher
 * CIS 035b
 * Lab #1 - 
 * jdisher@parad.net
 * 
 * @author jdisher
 */

package util;

import java.io.BufferedReader;
//import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import adapter.BuildAuto;
import scale.EditOptions;

public class autoDriver {

	public static void main(String[] args) {
		String filename = null;
		BuildAuto builder = BuildAuto.getInstance();
	
		// Eclipse doesn't play well with System.console(). So using something I found online to do this.
		
//		Console cons = System.console();
		BufferedReader cons = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.printf("Enter filename for configuration: ");
//		filename = cons.readLine();
		try { 
			filename = cons.readLine(); 
			System.out.printf("Loading config options from %s\n", filename);
		} catch (IOException e) { System.out.printf("%s\n", e.toString()); }
		String modelName = builder.buildAuto(filename);
		builder.printAuto(modelName);
		
		// Change some stuff, then print it out again
		System.out.printf("Changing some stuff before printing it out again!\n\n");
		
		builder.updateOptionPrice(modelName, "color", "Infra-Red Clearcoat", 499.95f);
		builder.updateOptionPrice(modelName, "color", "French Blue Clearcoat Metallic", 999.95f);
		builder.updateOpsetName(modelName, "color", "Available Colors");
		builder.updateOpsetName(modelName, "sideairbags", "Side Airbags");
		
		// Set some options!
		builder.setOptionChoice(modelName, "Available Colors", "Infra-Red Clearcoat");
		builder.setOptionChoice(modelName, "transmission", "manual");
		builder.setOptionChoice(modelName, "brakes", "ABS with Advance Trac");
		builder.setOptionChoice(modelName, "Side Airbags", "present");
		builder.setOptionChoice(modelName, "moonroof", "present");
		
		builder.printAuto(modelName);
		System.out.printf("As configured, this car costs $%.2f\n", builder.getPrice(modelName));
		
		System.out.printf("\n\nLet's do some thread testing!\n");
		EditOptions eo1 = new EditOptions(modelName, "Available Colors", "Infra-Red Clearcoat");
		EditOptions eo2 = new EditOptions(modelName, "Available Colors", "French Blue Clearcoat Metallic");
		
		Thread t1 = new Thread(eo1);
		Thread t2 = new Thread(eo2);
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			System.out.printf("Caught an InterruptedException! %s\n", e.toString());
		}
		
		builder.printAuto(modelName);
	}
}

/* test run - lab 4
 * 
 * Note - I added the 2000ms sleep in Automobile.setOptionChoice() to make it very obvious
 * that the locking wrapped around the modification methods is in effect. Unfortunately
 * I have a lot of changes in here, so it takes a bit to run :) But I will remove
 * the Thread.sleep() for lab5 and future. This is just to make it very clear that
 * the locking is working.
 
Enter filename for configuration: /Users/jdisher/Dropbox/DeAnza/cis35b/model/config.txt
Loading config options from /Users/jdisher/Dropbox/DeAnza/cis35b/model/config.txt
Dumping information for Generic Motors Focus Wagon ZTW
Base cost of $18445.00
There are 5 opsets
Available options for OptionSet color [10]
Option Fort Knox Gold Clearcoat Metallic has price $0.00
Option Liquid Grey Clearcoat Metallic has price $0.00
Option Infra-Red Clearcoat has price $0.00
Option Grabber Green Clearcoat Metallic has price $0.00
Option Sangria Red Clearcoat Metallic has price $0.00
Option French Blue Clearcoat Metallic has price $0.00
Option Twilight Blue Clearcoat Metallic has price $0.00
Option CD Silver Clearcoat Metallic has price $0.00
Option Pitch Black Clearcoat has price $0.00
Option Cloud 9 White Clearcoat has price $0.00

Available options for OptionSet transmission [2]
Option manual has price $-815.00
Option automatic has price $0.00

Available options for OptionSet brakes [3]
Option standard has price $0.00
Option ABS has price $400.00
Option ABS with Advance Trac has price $1625.00

Available options for OptionSet sideairbags [2]
Option none has price $0.00
Option present has price $350.00

Available options for OptionSet moonroof [2]
Option none has price $0.00
Option present has price $595.00

Changing some stuff before printing it out again!

Sleeping 2s for threading testing... [Available Colors set to Infra-Red Clearcoat]
Sleeping 2s for threading testing... [transmission set to manual]
Sleeping 2s for threading testing... [brakes set to ABS with Advance Trac]
Sleeping 2s for threading testing... [Side Airbags set to present]
Sleeping 2s for threading testing... [moonroof set to present]
Dumping information for Generic Motors Focus Wagon ZTW
Base cost of $18445.00
There are 5 opsets
Available options for OptionSet Available Colors [10]
Option Fort Knox Gold Clearcoat Metallic has price $0.00
Option Liquid Grey Clearcoat Metallic has price $0.00
Option Infra-Red Clearcoat has price $499.95
Option Grabber Green Clearcoat Metallic has price $0.00
Option Sangria Red Clearcoat Metallic has price $0.00
Option French Blue Clearcoat Metallic has price $999.95
Option Twilight Blue Clearcoat Metallic has price $0.00
Option CD Silver Clearcoat Metallic has price $0.00
Option Pitch Black Clearcoat has price $0.00
Option Cloud 9 White Clearcoat has price $0.00

Available options for OptionSet transmission [2]
Option manual has price $-815.00
Option automatic has price $0.00

Available options for OptionSet brakes [3]
Option standard has price $0.00
Option ABS has price $400.00
Option ABS with Advance Trac has price $1625.00

Available options for OptionSet Side Airbags [2]
Option none has price $0.00
Option present has price $350.00

Available options for OptionSet moonroof [2]
Option none has price $0.00
Option present has price $595.00

As configured, this car costs $20699.95


Let's do some thread testing!
Sleeping for 966ms, then setting option French Blue Clearcoat Metallic for opset Available Colors on model Focus Wagon ZTW
Sleeping for 809ms, then setting option Infra-Red Clearcoat for opset Available Colors on model Focus Wagon ZTW
Sleeping 2s for threading testing... [Available Colors set to Infra-Red Clearcoat]
Option selection complete!
Sleeping 2s for threading testing... [Available Colors set to French Blue Clearcoat Metallic]
Option selection complete!
Dumping information for Generic Motors Focus Wagon ZTW
Base cost of $18445.00
There are 5 opsets
Available options for OptionSet Available Colors [10]
Option Fort Knox Gold Clearcoat Metallic has price $0.00
Option Liquid Grey Clearcoat Metallic has price $0.00
Option Infra-Red Clearcoat has price $499.95
Option Grabber Green Clearcoat Metallic has price $0.00
Option Sangria Red Clearcoat Metallic has price $0.00
Option French Blue Clearcoat Metallic has price $999.95
Option Twilight Blue Clearcoat Metallic has price $0.00
Option CD Silver Clearcoat Metallic has price $0.00
Option Pitch Black Clearcoat has price $0.00
Option Cloud 9 White Clearcoat has price $0.00

Available options for OptionSet transmission [2]
Option manual has price $-815.00
Option automatic has price $0.00

Available options for OptionSet brakes [3]
Option standard has price $0.00
Option ABS has price $400.00
Option ABS with Advance Trac has price $1625.00

Available options for OptionSet Side Airbags [2]
Option none has price $0.00
Option present has price $350.00

Available options for OptionSet moonroof [2]
Option none has price $0.00
Option present has price $595.00

 */

/* test run - lab 3
 
Enter filename for configuration: /Users/jdisher/Dropbox/DeAnza/cis35b/model/config.txt
Loading config options from /Users/jdisher/Dropbox/DeAnza/cis35b/model/config.txt
Dumping information for Generic Motors Focus Wagon ZTW
Base cost of $18445.00
There are 5 opsets
Available options for OptionSet color [10]
Option Fort Knox Gold Clearcoat Metallic has price $0.00
Option Liquid Grey Clearcoat Metallic has price $0.00
Option Infra-Red Clearcoat has price $0.00
Option Grabber Green Clearcoat Metallic has price $0.00
Option Sangria Red Clearcoat Metallic has price $0.00
Option French Blue Clearcoat Metallic has price $0.00
Option Twilight Blue Clearcoat Metallic has price $0.00
Option CD Silver Clearcoat Metallic has price $0.00
Option Pitch Black Clearcoat has price $0.00
Option Cloud 9 White Clearcoat has price $0.00

Available options for OptionSet transmission [2]
Option manual has price $-815.00
Option automatic has price $0.00

Available options for OptionSet brakes [3]
Option standard has price $0.00
Option ABS has price $400.00
Option ABS with Advance Trac has price $1625.00

Available options for OptionSet sideairbags [2]
Option none has price $0.00
Option present has price $350.00

Available options for OptionSet moonroof [2]
Option none has price $0.00
Option present has price $595.00

Changing some stuff before printing it out again!

Dumping information for Generic Motors Focus Wagon ZTW
Base cost of $18445.00
There are 5 opsets
Available options for OptionSet Available Colors [10]
Option Fort Knox Gold Clearcoat Metallic has price $0.00
Option Liquid Grey Clearcoat Metallic has price $0.00
Option Infra-Red Clearcoat has price $499.95
Option Grabber Green Clearcoat Metallic has price $0.00
Option Sangria Red Clearcoat Metallic has price $0.00
Option French Blue Clearcoat Metallic has price $999.95
Option Twilight Blue Clearcoat Metallic has price $0.00
Option CD Silver Clearcoat Metallic has price $0.00
Option Pitch Black Clearcoat has price $0.00
Option Cloud 9 White Clearcoat has price $0.00

Available options for OptionSet transmission [2]
Option manual has price $-815.00
Option automatic has price $0.00

Available options for OptionSet brakes [3]
Option standard has price $0.00
Option ABS has price $400.00
Option ABS with Advance Trac has price $1625.00

Available options for OptionSet Side Airbags [2]
Option none has price $0.00
Option present has price $350.00

Available options for OptionSet moonroof [2]
Option none has price $0.00
Option present has price $595.00

As configured, this car costs $20699.95
 
 */

/* test run - lab 2

Enter filename for configuration: foo.txt
Loading config options from foo.txt
(╯°□°）╯︵ ┻━┻: [300] File not found
Using default filename config.txt
Dumping information for model Focus Wagon ZTW
Base cost of $18445.00
There are 5 opsets
Available options for OptionSet color [10]
Option Fort Knox Gold Clearcoat Metallic has price $0.00
Option Liquid Grey Clearcoat Metallic has price $0.00
Option Infra-Red Clearcoat has price $0.00
Option Grabber Green Clearcoat Metallic has price $0.00
Option Sangria Red Clearcoat Metallic has price $0.00
Option French Blue Clearcoat Metallic has price $0.00
Option Twilight Blue Clearcoat Metallic has price $0.00
Option CD Silver Clearcoat Metallic has price $0.00
Option Pitch Black Clearcoat has price $0.00
Option Cloud 9 White Clearcoat has price $0.00

Available options for OptionSet transmission [2]
Option manual has price $-815.00
Option automatic has price $0.00

Available options for OptionSet brakes [3]
Option standard has price $0.00
Option ABS has price $400.00
Option ABS with Advance Trac has price $1625.00

Available options for OptionSet sideairbags [2]
Option none has price $0.00
Option present has price $350.00

Available options for OptionSet moonroof [2]
Option none has price $0.00
Option present has price $595.00

Changing some stuff before printing it out again!

Dumping information for model Focus Wagon ZTW
Base cost of $18445.00
There are 5 opsets
Available options for OptionSet Available Colors [10]
Option Fort Knox Gold Clearcoat Metallic has price $0.00
Option Liquid Grey Clearcoat Metallic has price $0.00
Option Infra-Red Clearcoat has price $499.95
Option Grabber Green Clearcoat Metallic has price $0.00
Option Sangria Red Clearcoat Metallic has price $0.00
Option French Blue Clearcoat Metallic has price $999.95
Option Twilight Blue Clearcoat Metallic has price $0.00
Option CD Silver Clearcoat Metallic has price $0.00
Option Pitch Black Clearcoat has price $0.00
Option Cloud 9 White Clearcoat has price $0.00

Available options for OptionSet transmission [2]
Option manual has price $-815.00
Option automatic has price $0.00

Available options for OptionSet brakes [3]
Option standard has price $0.00
Option ABS has price $400.00
Option ABS with Advance Trac has price $1625.00

Available options for OptionSet Side Airbags [2]
Option none has price $0.00
Option present has price $350.00

Available options for OptionSet moonroof [2]
Option none has price $0.00
Option present has price $595.00

 */

/*
 * Test run - lab 1 (for posterity sake)

fuwa:bin jdisher$ java util/autoDriver
Enter filename for configuration: ../config.txt
Loading config options from ../config.txt
Set model name to Focus Wagon ZTW
Set base cost to $18445
Created array for 5 optionSets
Created optionSet color at index 0 with optionCount 10
Created optionSet transmission at index 1 with optionCount 2
Created optionSet brakes at index 2 with optionCount 3
Created optionSet sideairbags at index 3 with optionCount 2
Created optionSet moonroof at index 4 with optionCount 2
Added option Fort Knox Gold Clearcoat Metallic to opset color with cost 0
Added option Liquid Grey Clearcoat Metallic to opset color with cost 0
Added option Infra-Red Clearcoat to opset color with cost 0
Added option Grabber Green Clearcoat Metallic to opset color with cost 0
Added option Sangria Red Clearcoat Metallic to opset color with cost 0
Added option French Blue Clearcoat Metallic to opset color with cost 0
Added option Twilight Blue Clearcoat Metallic to opset color with cost 0
Added option CD Silver Clearcoat Metallic to opset color with cost 0
Added option Pitch Black Clearcoat to opset color with cost 0
Added option Cloud 9 White Clearcoat to opset color with cost 0
Added option manual to opset transmission with cost -815
Added option automatic to opset transmission with cost 0
Added option standard to opset brakes with cost 0
Added option ABS to opset brakes with cost 400
Added option ABS with Advance Trac to opset brakes with cost 1625
Added option none to opset sideairbags with cost 0
Added option present to opset sideairbags with cost 350
Added option none to opset moonroof with cost 0
Added option present to opset moonroof with cost 595

Dumping information for model Focus Wagon ZTW
Base cost of 18445
There are 5 opsets
Available options for OptionSet color [10]
Option Fort Knox Gold Clearcoat Metallic has price 0
Option Liquid Grey Clearcoat Metallic has price 0
Option Infra-Red Clearcoat has price 0
Option Grabber Green Clearcoat Metallic has price 0
Option Sangria Red Clearcoat Metallic has price 0
Option French Blue Clearcoat Metallic has price 0
Option Twilight Blue Clearcoat Metallic has price 0
Option CD Silver Clearcoat Metallic has price 0
Option Pitch Black Clearcoat has price 0
Option Cloud 9 White Clearcoat has price 0

Available options for OptionSet transmission [2]
Option manual has price -815
Option automatic has price 0

Available options for OptionSet brakes [3]
Option standard has price 0
Option ABS has price 400
Option ABS with Advance Trac has price 1625

Available options for OptionSet sideairbags [2]
Option none has price 0
Option present has price 350

Available options for OptionSet moonroof [2]
Option none has price 0
Option present has price 595


Serializing auto object a
Deserializing auto object into newAuto
Set model name to Focus Wagon ZTW - Deserialized Copy

Dumping information for model Focus Wagon ZTW - Deserialized Copy
Base cost of 18445
There are 5 opsets
Available options for OptionSet color [10]
Option Fort Knox Gold Clearcoat Metallic has price 0
Option Liquid Grey Clearcoat Metallic has price 0
Option Infra-Red Clearcoat has price 0
Option Grabber Green Clearcoat Metallic has price 0
Option Sangria Red Clearcoat Metallic has price 0
Option French Blue Clearcoat Metallic has price 0
Option Twilight Blue Clearcoat Metallic has price 0
Option CD Silver Clearcoat Metallic has price 0
Option Pitch Black Clearcoat has price 0
Option Cloud 9 White Clearcoat has price 0

Available options for OptionSet transmission [2]
Option manual has price -815
Option automatic has price 0

Available options for OptionSet brakes [3]
Option standard has price 0
Option ABS has price 400
Option ABS with Advance Trac has price 1625

Available options for OptionSet sideairbags [2]
Option none has price 0
Option present has price 350

Available options for OptionSet moonroof [2]
Option none has price 0
Option present has price 595

fuwa:bin jdisher$ 

*/