package pt.ulisboa.tecnico.learnjava.mbway;

import static org.junit.Assert.assertEquals;

import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.mbway.cli.MBWayView;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.AssociateMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.ConfirmMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.SplitInsuranceMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AssociateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.ConfirmException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SplitException;

public class SplitInsuranceMBWayTest {

	private Bank bank1;
	private Bank bank2;
	private Sibs sibs;
	
	private Client client1;
	private Client client2;
	private Client client3;
	
	private Account Account1;
	private Account Account2;
	private Account Account3;
	
	private String iban1;
	private String iban2;
	private String iban3;
	
	private SplitInsuranceMBWayController controller;
	private ConfirmMBWayController Confirmcontroller;
	private AssociateMBWayController Associatecontroller;
	private MBWayDataBase model;
	
	private String Accountnumber1;
	private String Accountnumber2;
	private String Accountnumber3;
	
	@Before
	public void setUp() throws BankException, ClientException, AccountException, AssociateException, ConfirmException {
		Services services = new Services();
		bank1 = new Bank("CGD");
		bank2 = new Bank("BPI");
		
		client1 = new Client(bank1, "Maria", "Soares", "123456789", "912346987", "Rua Alves Redol", 25);
		client2 = new Client(bank2, "Clara", "Rodrigues", "987654321", "917895234", "Rua Redol", 30);
		client3 = new Client(bank2, "Marco", "Andrade", "345123678", "967896734", "Rua da Figueira", 40);
		
		iban1 = bank1.createAccount(AccountType.CHECKING, client1, 1000, 0);
		iban2 = bank2.createAccount(AccountType.CHECKING, client2, 5000, 0);
		iban3 = bank2.createAccount(AccountType.CHECKING, client3, 2500, 0);
		
		Account1 = services.getAccountByIban(iban1);
		Account2 = services.getAccountByIban(iban2);
		Account3 = services.getAccountByIban(iban3);
		
		sibs = new Sibs(100,services);
		MBWayView view = new MBWayView();
		MBWayDataBase model = new MBWayDataBase(sibs);
		controller = new SplitInsuranceMBWayController(model);
		Confirmcontroller = new ConfirmMBWayController(model);
		Associatecontroller = new AssociateMBWayController(model);
		
		//Account1
		Accountnumber1= "917895234";
		Associatecontroller.associate_mbway(iban1, Accountnumber1);
		String code = model.getAccount(Accountnumber1).getCode();
		Confirmcontroller.confirm_mbway(Accountnumber1, code);
		
		//Account2
		Accountnumber2= "923823273";
		Associatecontroller.associate_mbway(iban2, Accountnumber2);
		String code2 = model.getAccount(Accountnumber2).getCode();
		Confirmcontroller.confirm_mbway(Accountnumber2, code2);
		
		//Account3
		Accountnumber3= "964938348";
		Associatecontroller.associate_mbway(iban3, Accountnumber3);
		String code3 = model.getAccount(Accountnumber3).getCode();
		Confirmcontroller.confirm_mbway(Accountnumber3, code3);
		
	}
	@Test
	public void success() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, SplitException {
		int n_mebrs = 3;
		int amount = 1500;
		TreeMap<String, String> family = new TreeMap<String, String>();
		family.put(Accountnumber1, "500"); //1000 -> 500
		family.put(Accountnumber2, "500"); //5000 -> 4500
		family.put(Accountnumber3, "500"); //2500 -> 2000
		controller.mbway_split_insurance(n_mebrs, amount, family);
		assertEquals(this.sibs.services.getAccountByIban(iban1).getBalance(),500);
		assertEquals(this.sibs.services.getAccountByIban(iban2).getBalance(),4500);
		assertEquals(this.sibs.services.getAccountByIban(iban3).getBalance(),2000);
	}
	
	@Test(expected = SplitException.class)
	public void MoreMembersAddedThenExpected() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, SplitException {
		int n_mebrs = 2;
		int amount = 1500;
		TreeMap<String, String> family = new TreeMap<String, String>();
		family.put(Accountnumber1, "500");
		family.put(Accountnumber2, "500"); 
		family.put(Accountnumber3, "500");  
		controller.mbway_split_insurance(n_mebrs, amount, family);
	}
	
	@Test(expected = SplitException.class)
	public void LowerMembersAddedThenExpected() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, SplitException {
		int n_mebrs = 4;
		int amount = 1500;
		TreeMap<String, String> family = new TreeMap<String, String>();
		family.put(Accountnumber1, "500");  
		family.put(Accountnumber2, "500");  
		family.put(Accountnumber3, "500");
		controller.mbway_split_insurance(n_mebrs, amount, family);
	}
	
	@Test(expected = SplitException.class)
	public void AmountPaiedLower() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, SplitException {
		int n_mebrs = 3;
		int amount = 2000; //1500Paied
		TreeMap<String, String> family = new TreeMap<String, String>();
		family.put(Accountnumber1, "500");
		family.put(Accountnumber2, "500"); 
		family.put(Accountnumber3, "500"); 
		controller.mbway_split_insurance(n_mebrs, amount, family);
	}
	
	
	@Test(expected = SplitException.class)
	public void AmountPaiedHigher() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, SplitException {
		int n_mebrs = 3;
		int amount = 1000; //1500Paied
		TreeMap<String, String> family = new TreeMap<String, String>();
		family.put(Accountnumber1, "500");
		family.put(Accountnumber2, "500"); 
		family.put(Accountnumber3, "500"); 
		controller.mbway_split_insurance(n_mebrs, amount, family);
	}
	
	
	@Test(expected = SplitException.class)
	public void AccountDoesntExists() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, SplitException {
		int n_mebrs = 3;
		int amount = 1000; //1500Paied
		TreeMap<String, String> family = new TreeMap<String, String>();
		family.put("993218323", "500");
		family.put(Accountnumber2, "500"); 
		family.put(Accountnumber3, "500"); 
		controller.mbway_split_insurance(n_mebrs, amount, family);
	}
	
	
	@Test(expected = SplitException.class)
	public void AccountBalanceLower() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, SplitException {
		int n_mebrs = 3;
		int amount = 2500; //1500Paied
		TreeMap<String, String> family = new TreeMap<String, String>();
		family.put(Accountnumber1, "1500"); //Balance = 1000
		family.put(Accountnumber2, "500"); 
		family.put(Accountnumber3, "500"); 
		controller.mbway_split_insurance(n_mebrs, amount, family);
	}
	
	
	
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
