package pt.ulisboa.tecnico.learnjava.mbway.controllers;

import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.ConfirmException;

public class ConfirmMBWayController {

	
	private MBWayDataBase modelDataBase;
	
	public ConfirmMBWayController(MBWayDataBase modelDataBase) {
		this.modelDataBase = modelDataBase;
	}
	
	public MBWayDataBase getModel() {
		return this.modelDataBase;
	}
	
	public String confirm_mbway(String number,String code) throws ConfirmException {
		if(modelDataBase.getAccount(number) == null) {
			throw new ConfirmException("Wrong phone number. That number doesn't exist.");
		}	
		
		else if(modelDataBase.getAccount(number).is_active()) {
			throw new ConfirmException("MBWAY association already confirmed.");
		}
		
		else if(modelDataBase.getAccount(number).getCode().equals(code)) {
			modelDataBase.getAccount(number).set_active();
			return "MBWAY association confirmed successfully!";
			
		}
		
		else {
			throw new ConfirmException("Wrong confirmation code. Try association again.");
		}
	}

}
