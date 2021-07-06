package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Operation {

	private final int value;
	private final String targetIban;
	private final String sourceIban;
	private Sibs sibs;
	private State state;
	

	public Operation(Sibs sibs,String sourceIban, String targetIban, int value) throws OperationException {
		checkParameters(value);
		this.value = value;

		if (invalidString(targetIban)) {
			throw new OperationException();
		}
		if (invalidString(sourceIban)) {
			throw new OperationException();
		}
		
		this.targetIban = targetIban;
		this.sourceIban = sourceIban;
		this.state = new Registered();
		this.sibs = sibs;
		}

	private void checkParameters(int value) throws OperationException {

		if (value <= 0) {
			throw new OperationException(value);
		}
	}

	public int getValue() {
		return this.value;
	}
	
	public Sibs getSibs() {
		return this.sibs;
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	public int commission() {
		return (int) (this.getValue()*0.02);
	}

	public String getTargetIban() {
		return this.targetIban;
	}
	
	public String getSourceIban() {
		return this.sourceIban;
	}
	
	public void process() throws AccountException {
		state.process(this);
	}
	
	public void cancel() throws AccountException {
		if(state != new Processed())
			state.cancel(this);
	}
	
	public void rollback() throws AccountException {
		if(state != new Processed())
			state.rollback(this);
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return this.state;
	}
	
	
		

}
