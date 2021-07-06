package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class Sibs {
	final Operation[] operations;
	Services services;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

	public void transfer(String sourceIban, String targetIban, int amount)
			throws SibsException, AccountException, OperationException {
		addOperation(sourceIban, targetIban, amount);
	}

	public int addOperation(String sourceIban, String targetIban, int value)
			throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}

		if (position == -1) {
			throw new SibsException();
		}

		Operation operation = new Operation(this,sourceIban, targetIban, value);

		this.operations[position] = operation;
		return position;
	}

	public void removeOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null; 
	}

	public Operation getOperation(int position) throws SibsException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}
	
	public int getNumberOfOperations() {
		int count = 0;
		for(Operation o : operations)
			if(o != null)
				count++;
		return count;
	}
	
	public int getTotalValue() {
		int TotalValue = 0;
		for(Operation o : operations)
			if(o != null)
				TotalValue += o.getValue();
		return TotalValue;
	}
	
	
	
	public void processOperations() throws AccountException {
		for(Operation o : operations) {
			if(o!=null)
				o.process();
		}
	}
	
	
	public void cancelOperation(int position) throws AccountException, SibsException {
		Operation o = this.getOperation(position);
		if(o == null) {
			throw new SibsException();
		}
		o.cancel();
	}
}
