package pt.ulisboa.tecnico.learnjava.mbway.controllers;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayAccount;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AssociateException;

public class AssociateMBWayController {
	
	private MBWayDataBase modelDataBase;

	public AssociateMBWayController(MBWayDataBase modelDataBase) {
		this.modelDataBase = modelDataBase;
	}
	
	public MBWayDataBase getModel() {
		return this.modelDataBase;
	}
	
	public String associate_mbway(String iban, String number) throws AccountException, AssociateException {
		//Verificar input
		if (number == null || (!number.matches("^\\d+$")) || number.length()==0) {
			throw new AssociateException("Wrong phone number, try again.");
		}
		if(iban == null || iban.length()<3) {
			throw new AssociateException("Wrong Iban, try again.");
		}	
		
		try {
			//Verificar conta
			if(modelDataBase.getSibs().services.getAccountByIban(iban) == null) {
				throw new AssociateException("Wrong Iban, try again.");
			}
		}
		//Verificar Banco
		catch(AccountException ae){
			throw new AssociateException("Wrong Iban, try again.");
		}		
		
		
		//Adicionar ao TreeMap
		int code = (int)(Math.random()*10000);
		//String code = this.view.associate_print();
		modelDataBase.addMBWay(number, new MBWayAccount(iban, number, Integer.toString(code)));
		return("Code: " + Integer.toString(code) + "(don't share it with anyone)");
		
	}
	

}
