package pt.ulisboa.tecnico.learnjava.mbway.controllers;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayAccount;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.TransferException;

public class TransferMBWayController {
	
	private MBWayDataBase modelDataBase;

	public TransferMBWayController(MBWayDataBase modelDataBase) {
		this.modelDataBase = modelDataBase;
	}
	
	public String mbway_transfer(String SourceNumber, String TargetNumber, String amount) throws NumberFormatException, SibsException, AccountException, OperationException, TransferException {
		int IntAmount=0;
		
		try {
			IntAmount = Integer.parseInt(amount);
		} catch(NumberFormatException ne) {
			throw new TransferException("Wrong amount, Try again.");
		}
		
		if(IntAmount<=0) {
			throw new TransferException("Wrong amount, Try again.");
		}
		
		if(!modelDataBase.containsNumber(SourceNumber) || !modelDataBase.containsNumber(TargetNumber)) {
			throw new TransferException("Wrong phone number, Try again.");	
		}
			
		MBWayAccount SourceAccount = modelDataBase.getAccount(SourceNumber);
		MBWayAccount TargetAccount = modelDataBase.getAccount(TargetNumber);
		
		if(!SourceAccount.is_active() || !TargetAccount.is_active()) {
			throw new TransferException("Account is not confirmed.");	
		}
		

		if(modelDataBase.getSibs().services.getAccountByIban(SourceAccount.getIban()).getBalance()<IntAmount) {
			throw new TransferException("Not enough money on the source account.");	
		}
		
		modelDataBase.getSibs().transfer(SourceAccount.getIban(), TargetAccount.getIban(), IntAmount);	
		return "Transfer performed successfully!";
	}

}
