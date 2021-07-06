package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;

public class Retry extends State {
	
	private int retries = 1;
	private State previous_state;
	
	public Retry(State state) {
		this.previous_state = state;
	}
	
	public void process(Operation o) throws AccountException {	
		if(retries >= 3) {
			o.setState(previous_state);
			o.rollback();
			o.setState(new Error());
		}
		else {
			o.setState(previous_state);
			o.process();
			if(o.getState().getClass().isInstance(new Retry(previous_state))) {
				o.setState(this);
			}
			this.retries += 1;
		}

	}

	public void cancel(Operation o) {
	}

}
