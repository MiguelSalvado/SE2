package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Withdrawn extends State {
	
	public void process(Operation o) throws AccountException {
		try {
		o.getSibs().services.deposit(o.getTargetIban(), o.getValue());
		o.setState(new Deposited());
		} catch(AccountException ae) {
			o.setState(new Retry(o.getState()));
		}
		
		
	}
	public void cancel(Operation o) throws AccountException {
		o.rollback();
		o.setState(new Cancelled());
	}
	
	public void rollback(Operation o) throws AccountException {
		o.getSibs().services.deposit(o.getSourceIban(), o.getValue());
	}
}
