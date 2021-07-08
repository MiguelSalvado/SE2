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
			
		MBWayAccount SourceAccount = get_account(SourceNumber);
		MBWayAccount TargetAccount = get_account(TargetNumber);
		
		if(modelDataBase.getSibs().services.getAccountByIban(SourceAccount.getIban()).getBalance()<IntAmount) {
			throw new TransferException("Not enough money on the source account.");	
		}
		
		modelDataBase.getSibs().transfer(SourceAccount.getIban(), TargetAccount.getIban(), IntAmount);	
		return "Transfer performed successfully!";
	}	
	
	
	public MBWayAccount get_account(String account) throws TransferException {
		/* This is the refactor for guideline Write Simple Units of Code (1 unit refactor) 
		 * Foi criada uma função que valida os numeros e retorna as contas assim foi possivel 
		 * reduzir o grau de complexidade da função mbway_tranfer, diminuindo o numero de branches).*/
		
		if(!modelDataBase.containsNumber(account)) {
			throw new TransferException("Wrong phone number, Try again.");	
		}
		MBWayAccount Account = modelDataBase.getAccount(account);
		if(!Account.is_active()) {
			throw new TransferException("Account is not confirmed.");	
		}
		return Account;
	}
}
