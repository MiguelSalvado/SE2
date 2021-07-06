package pt.ulisboa.tecnico.learnjava.mbway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.mbway.cli.MBWayView;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.AssociateMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AssociateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;


public class AssociateMBWayTest {
	
	private Bank bank1;
	private Bank bank2;
	
	private Client client1;
	private Client client2;
	private Client client3;
	
	private String iban1;
	private String iban2;
	private String iban3;
	
	private AssociateMBWayController controller;
	private MBWayDataBase model;
	
	@Before
	public void setUp() throws BankException, ClientException, AccountException {
		Services services = new Services();
		bank1 = new Bank("CGD");
		bank2 = new Bank("BPI");
		
		client1 = new Client(bank1, "Maria", "Soares", "123456789", "912346987", "Rua Alves Redol", 25);
		client2 = new Client(bank2, "Clara", "Rodrigues", "987654321", "917895234", "Rua Redol", 30);
		client3 = new Client(bank2, "Marco", "Andrade", "345123678", "967896734", "Rua da Figueira", 40);
		
		iban1 = bank1.createAccount(AccountType.CHECKING, client1, 1000, 0);
		iban2 = bank2.createAccount(AccountType.CHECKING, client2, 5000, 0);
		iban3 = bank2.createAccount(AccountType.CHECKING, client3, 2500, 0);
		
		
		Sibs sibs = new Sibs(100,services);
		MBWayDataBase model = new MBWayDataBase(sibs);
		controller = new AssociateMBWayController(model);
	}
	
	
	@Test
	public void success() throws BankException, ClientException, AccountException, AssociateException {
		String number= "917895234";
		controller.associate_mbway(iban1, number);
		assertEquals(controller.getModel().getAccount(number).getIban(), iban1);
		assertFalse(controller.getModel().getAccount(number).is_active());	
	}
	
	@Test(expected = AssociateException.class)
	public void numberEmpty() throws BankException, ClientException, AccountException, AssociateException {
		controller.associate_mbway(iban1, "");	
	}
	
	@Test(expected = AssociateException.class)
	public void numberNull() throws BankException, ClientException, AccountException, AssociateException {
		String number= null;
		controller.associate_mbway(iban1, number);	
	}
	
	@Test(expected = AssociateException.class)
	public void numberWithLetters() throws BankException, ClientException, AccountException, AssociateException {
		String number= "92913281ABC";
		controller.associate_mbway(iban1, number);	
	}
	
	@Test(expected = AssociateException.class)
	public void IbanNull() throws BankException, ClientException, AccountException, AssociateException {
		String number= "92913281";
		controller.associate_mbway(null, number);	
	}
	
	@Test(expected = AssociateException.class)
	public void IbanLengthLowerThen3() throws BankException, ClientException, AccountException, AssociateException {
		String number= "92913281";
		controller.associate_mbway("CG", number);	
	}
	
	@Test(expected = AssociateException.class)
	public void BankDoesntExists() throws BankException, ClientException, AccountException, AssociateException {
		String number= "92913281";
		controller.associate_mbway("CGI001", number);	
	}
	
	@Test(expected = AssociateException.class)
	public void AccountDoesntExists() throws BankException, ClientException, AccountException, AssociateException {
		String number= "92913281";
		controller.associate_mbway("CGD001", number);	
	}
	
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}


}
