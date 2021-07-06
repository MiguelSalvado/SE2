package pt.ulisboa.tecnico.learnjava.sibs.exceptions;

public class ConfirmException extends Exception{

	private String msg;
	
	public ConfirmException(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}

}
