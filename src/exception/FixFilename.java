package exception;

import adapter.BuildAuto;

public class FixFilename {
	private static String defaultFilename = "config.txt";
	
	public FixFilename() { }
	
	protected void fix() {
		BuildAuto builder = BuildAuto.getInstance();
		builder.setFilename(defaultFilename);
		System.out.printf("Using default filename config.txt\n");
	}
}
