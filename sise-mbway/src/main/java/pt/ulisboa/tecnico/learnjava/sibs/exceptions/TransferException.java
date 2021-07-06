package pt.ulisboa.tecnico.learnjava.sibs.exceptions;

public class TransferException extends Exception{

	
	private String msg;
	
	public TransferException(String msg) {	
		this.msg = msg;
	}
		
	public String getMsg() {
		return this.msg;
	}

}
