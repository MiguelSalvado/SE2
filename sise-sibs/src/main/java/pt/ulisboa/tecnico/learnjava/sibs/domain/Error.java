package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Error extends State{
	
	public void process(Operation o) throws AccountException {	
	}

	public void cancel(Operation o) {
	}

}
