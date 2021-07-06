package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Deposited extends State{
	
	public void process(Operation o) throws AccountException {	
		try {
			if(o.getSibs().services.getAccountByIban(o.getSourceIban()).getBalance() > o.commission()) {
				o.getSibs().services.withdraw(o.getSourceIban(), o.commission());
				o.setState(new Processed());
			}
			else 
				throw new AccountException();
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
		o.getSibs().services.withdraw(o.getTargetIban(), o.getValue());
	}

}
