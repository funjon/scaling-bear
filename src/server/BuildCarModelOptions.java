package server;

import java.util.ArrayList;
import java.util.Properties;
import java.io.ObjectOutputStream;

import exception.AutoException;
import adapter.BuildAuto;

public class BuildCarModelOptions implements AutoServer {
	private static BuildAuto builder = null;
	
	public ArrayList<String> getModels() {
		if (builder == null) { builder = BuildAuto.getInstance(); }
		return builder.getModels();
	}
	
	public void buildWithProperties(String filename) {
		if (builder == null) { builder = BuildAuto.getInstance(); }
		builder.buildAuto(filename);
	}
	
	public void buildWithProperties(Properties props) throws AutoException {
		if (builder == null) { builder = BuildAuto.getInstance(); }
		builder.buildAuto(props);
	}
	
	public void sendAuto(ObjectOutputStream oos, String model) {
		if (builder == null) { builder = BuildAuto.getInstance(); }
		builder.sendAuto(oos, model);
	}
}
