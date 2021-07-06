package pt.ulisboa.tecnico.learnjava.sibs.exceptions;

public class AssociateException extends Exception {
	
	private String msg;
	
	public AssociateException(String msg) {	
		this.msg = msg;
	}
	
	public String getMsg() {
		return this.msg;
	}	
	

}
