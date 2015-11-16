package server;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import exception.AutoException;

public interface AutoServer {
	public ArrayList<String> getModels();
	public void buildWithProperties(String filename);
	public void buildWithProperties(Properties props) throws AutoException;
	public void sendAuto(ObjectOutputStream oos, String model);
}
