package pt.ulisboa.tecnico.learnjava.sibs.exceptions;

public class SplitException extends Exception{


	private String msg;
	
	public SplitException(String msg) {	
		this.msg = msg;
	}
		
	public String getMsg() {
		return this.msg;
	}
	
}
