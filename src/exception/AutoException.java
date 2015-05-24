package exception;

import java.util.HashMap;
import util.FileIO;

public class AutoException extends Exception {
	private static final long serialVersionUID = 2722886127L;
	
	private int errorCode = 0;
	private String errorMessage = "";
	private HashMap<Integer, String> errorCodes;
	private HashMap<String, Integer> errorHandles;
	private String tableflip = "(╯°□°）╯︵ ┻━┻:";
	
	public AutoException() { super(); }
	
	public AutoException(int code, String message) {
		super(message);
		loadErrors();
		this.setCode(code);
		this.setMessage(message);
	}
	
	public AutoException(String handle) {
		if (errorHandles.containsKey(handle)) {
			setCode(errorHandles.get(handle));
			setMessage(errorCodes.get(this.getCode()));
		}
	}
	
	public AutoException(int code) {
		this(code, "");
		if (errorCodes.containsKey(code)) {
			setMessage(errorCodes.get(code));
		}
	}

	public void setCode(int code) { this.errorCode = code; }
	public void setMessage(String message) { this.errorMessage = message; }
	
	public int getCode() { return errorCode; }
	public String getMessage() { return errorMessage; }
	
	public void print() {
		System.out.printf("%s [%d] %s\n", tableflip, errorCode, errorMessage);
	}
	
	public void fixIssue() {
		switch (errorCode) {
		case 300:
			FixFilename fixer = new FixFilename();
			fixer.fix();
			break;
		}
	}
	
	private void loadErrors() {
		FileIO fio = new FileIO();
		errorCodes = fio.loadErrors();
		errorHandles = fio.loadHandles();
	}
}

/*
 * Exception codes
 * 
 * 1-99 - Reserved
 * 
 * 100-199 - OptionSet related
 * - 100: Duplicate optionSet
 * - 101: Too many optionSets (ex allocated 5, encountered 6)
 * - 102: Undeclared optionSet
 * 200-299 - Option related
 * - 200: Duplicate option
 * - 201: Too many options (ex allocated 10, encountered 11)
 * 300-399 - File I/O related 
 * - 300: File not found
 */