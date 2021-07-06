package pt.ulisboa.tecnico.learnjava.mbway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AssociateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.ConfirmException;

public class ConfirmMBWayTest {

	private Bank bank1;
	private Bank bank2;
	
	private Client client1;
	private Client client2;
	private Client client3;
	
	private String iban1;
	private String iban2;
	private String iban3;
	
	private ConfirmMBWayController controller;
	private AssociateMBWayController Associatecontroller;
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
		MBWayView view = new MBWayView();
		MBWayDataBase model = new MBWayDataBase(sibs);
		controller = new ConfirmMBWayController(model);
		Associatecontroller = new AssociateMBWayController(model);
	}
	
	
	@Test
	public void success() throws BankException, ClientException, AccountException, AssociateException, ConfirmException {
		String number= "917895234";
		Associatecontroller.associate_mbway(iban1, number);
		String code = controller.getModel().getAccount(number).getCode();
		controller.confirm_mbway(number, code);
		assertTrue(controller.getModel().getAccount(number).is_active());	
	}
	
	@Test(expected = ConfirmException.class)
	public void WrongCode() throws BankException, ClientException, AccountException, AssociateException, ConfirmException {
		String number= "917895234";
		Associatecontroller.associate_mbway(iban1, number);
		String code = "00000";
		controller.confirm_mbway(number, code);
	}
	
	@Test(expected = ConfirmException.class)
	public void WrongNumber() throws BankException, ClientException, AccountException, AssociateException, ConfirmException {
		String RightNumber = "967432632";
		String WrongNumber = "917895234";
		Associatecontroller.associate_mbway(iban1, RightNumber);
		String code = "00000";
		controller.confirm_mbway(WrongNumber, code);
	}
	
	
	@Test(expected = ConfirmException.class)
	public void MBWayAccountAlreadyConfirmed() throws BankException, ClientException, AccountException, AssociateException, ConfirmException {
		String number= "917895234";
		Associatecontroller.associate_mbway(iban1, number);
		String code = controller.getModel().getAccount(number).getCode();
		controller.confirm_mbway(number, code);
		controller.confirm_mbway(number, code);
	}
	
	
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
