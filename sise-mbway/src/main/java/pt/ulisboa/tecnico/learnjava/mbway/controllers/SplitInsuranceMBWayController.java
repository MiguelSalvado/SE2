package pt.ulisboa.tecnico.learnjava.mbway.controllers;

import java.util.TreeMap;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SplitException;

public class SplitInsuranceMBWayController {

	
	private MBWayDataBase modelDataBase;
	private TreeMap<String, String> Family;
	
	public SplitInsuranceMBWayController(MBWayDataBase modelDataBase, TreeMap<String, String> Family) {
		this.modelDataBase = modelDataBase;
		this.Family = Family;
	}
	
	
	public String mbway_split_insurance(int N_family_memb, int TotalAmount) throws NumberFormatException, AccountException, SplitException {
		if(mbway_split_validate_size(N_family_memb)){
			if(mbway_split_validate_amount() != TotalAmount) {
				throw new SplitException("Something is wrong. Is the insurance amount right?");
			}
			for(String number: Family.keySet()) {
				modelDataBase.getSibs().services.withdraw(modelDataBase.getAccount(number).getIban(), Integer.parseInt(Family.get(number)));
			}
			return "Insurance paid successfully!";
		}
		return null;
	}
	
	public boolean mbway_split_validate_size(int N_family_memb) throws SplitException {
		if(N_family_memb > Family.size()) {		
			throw new SplitException("Oh no! One family member is missing.");
		}
		else if(N_family_memb < Family.size()){
			throw new SplitException("Oh no! Too many family members.");
		}
		
		return true;
	}
	
	public int mbway_split_validate_amount() throws SplitException, NumberFormatException, AccountException {
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
		return TotalPayed;
	}
	
}
