package pt.ulisboa.tecnico.learnjava.mbway.controllers;

import java.util.TreeMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SplitException;

public class SplitInsuranceMBWayController {

	
	private MBWayDataBase modelDataBase;

	
	public SplitInsuranceMBWayController(MBWayDataBase modelDataBase) {
		this.modelDataBase = modelDataBase;
	}
	
	
	public String mbway_split_insurance(int N_family_memb, int TotalAmount, TreeMap<String, String> Family) throws NumberFormatException, AccountException, SplitException {
		if(N_family_memb > Family.size()) {		
			throw new SplitException("Oh no! One family member is missing.");
		}
		else if(N_family_memb < Family.size()){
			throw new SplitException("Oh no! Too many family members.");
		}
		int TotalPayed = 0;
		for(String number: Family.keySet()) {
			if(modelDataBase.getAccount(number) == null) {
				throw new SplitException("Something is wrong. Is the number: "+number+" right?");	
			}

			if(modelDataBase.getSibs().services.getAccountByIban(modelDataBase.getAccount(number).getIban()).getBalance() < Integer.parseInt(Family.get(number))) {
				throw new SplitException("Oh no! One family member doesn’t have money to pay!");	
			}
			TotalPayed += Integer.parseInt(Family.get(number));
		}
		if(TotalPayed != TotalAmount) {
			throw new SplitException("Something is wrong. Is the insurance amount right?");
		}
		
		for(String number: Family.keySet()) {
			modelDataBase.getSibs().services.withdraw(modelDataBase.getAccount(number).getIban(), Integer.parseInt(Family.get(number)));
		}
		return "Insurance paid successfully!";
	}
}
