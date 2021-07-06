package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Registered extends State {
	
	public void process(Operation o) throws AccountException {
		try {
		o.getSibs().services.withdraw(o.getSourceIban(), o.getValue());
		o.setState(new Withdrawn());
		} catch(AccountException ae) {
			o.setState(new Retry(o.getState()));
		}
	}
	
	public void cancel(Operation o) {
		o.setState(new Cancelled());
	}
	
	public void rollback(Operation o) {
	}
}
