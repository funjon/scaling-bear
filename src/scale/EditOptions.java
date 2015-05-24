package scale;

import java.util.Random;

import adapter.BuildAuto;

public class EditOptions implements Runnable {

	private BuildAuto _builder;
	
	private String _modelName;
	private String _opsetName;
	private String _optionName;
	
	public EditOptions(String modelName, String opsetName, String optionName) { 
		_builder = BuildAuto.getInstance();
		
		_modelName = modelName;
		_opsetName = opsetName;
		_optionName = optionName;
	}
	
	private int _randomSleep(int min, int max) {
		Random rn = new Random();
		int randint = rn.nextInt((max - min) + 1) + min;
		return randint;
	}
	
	public void run() {
		int rsleep = _randomSleep(0,1000);
		System.out.printf("Sleeping for %dms, then setting option %s for opset %s on model %s\n", rsleep, _optionName, _opsetName, _modelName);
		
		try { Thread.sleep(rsleep); }
		catch (InterruptedException e) { System.out.printf("Caught an InterruptedException! %s\n", e.toString()); }
		
		_builder.setOptionChoice(_modelName, _opsetName, _optionName);
		System.out.printf("Option selection complete!\n");
	}

}
