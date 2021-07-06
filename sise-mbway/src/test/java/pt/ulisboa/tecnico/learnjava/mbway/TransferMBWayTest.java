package pt.ulisboa.tecnico.learnjava.mbway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import pt.ulisboa.tecnico.learnjava.mbway.controllers.TransferMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AssociateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.ConfirmException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.TransferException;

public class TransferMBWayTest {

	private Bank bank1;
	private Bank bank2;
	private Sibs sibs;
	
	private Client client1;
	private Client client2;
	private Client client3;
	
	private Account SourceAccount;
	private Account TargetAccount;
	
	private String iban1;
	private String iban2;
	private String iban3;
	
	private TransferMBWayController controller;
	private ConfirmMBWayController Confirmcontroller;
	private AssociateMBWayController Associatecontroller;
	private MBWayDataBase model;
	
	private String Sourcenumber;
	private String Targetnumber;
	
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
		
		Account SourceAccount = services.getAccountByIban(iban1);
		Account TargetAccount = services.getAccountByIban(iban2);
		
		
		sibs = new Sibs(100,services);
		MBWayView view = new MBWayView();
		MBWayDataBase model = new MBWayDataBase(sibs);
		controller = new TransferMBWayController(model);
		Confirmcontroller = new ConfirmMBWayController(model);
		Associatecontroller = new AssociateMBWayController(model);
		
		//Source Account
		Sourcenumber= "917895234";
		Associatecontroller.associate_mbway(iban1, Sourcenumber);
		String code = model.getAccount(Sourcenumber).getCode();
		Confirmcontroller.confirm_mbway(Sourcenumber, code);
		
		//Target Account
		Targetnumber= "923823273";
		Associatecontroller.associate_mbway(iban2, Targetnumber);
		String code2 = model.getAccount(Targetnumber).getCode();
		Confirmcontroller.confirm_mbway(Targetnumber, code2);
	}
	
	
	@Test
	public void success() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, TransferException {
		
		controller.mbway_transfer(Sourcenumber, Targetnumber, "100");
		assertEquals(this.sibs.services.getAccountByIban(iban1).getBalance(),900);
		assertEquals(this.sibs.services.getAccountByIban(iban2).getBalance(),5100);
	}
	
	@Test(expected = TransferException.class)
	public void AmountLowerThenZero() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, TransferException {
		controller.mbway_transfer(Sourcenumber, Targetnumber, "-100");
	}
	
	@Test(expected = TransferException.class)
	public void AmountWithLetters() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, TransferException {
		controller.mbway_transfer(Sourcenumber, Targetnumber, "100AB");
	}
	
	@Test(expected = TransferException.class)
	public void AmountLowerThenSourceBalance() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, TransferException {

		controller.mbway_transfer(Sourcenumber, Targetnumber, "1500");
	}
	
	@Test(expected = TransferException.class)
	public void SourceNumberDoesntExists() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, TransferException {

		controller.mbway_transfer("96123213", Targetnumber, "100");
	}
	
	
	@Test(expected = TransferException.class)
	public void TargetNumberDoesntExists() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, TransferException {	
		
		controller.mbway_transfer(Sourcenumber, "96123213", "100");
	}
	
	@Test(expected = TransferException.class)
	public void SourceAccountNotConfirmed() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, AssociateException, TransferException {
		//Source Account
		String Sourcenumber2= "94765765";
		Associatecontroller.associate_mbway(iban3, Sourcenumber2);
		
		controller.mbway_transfer(Sourcenumber2, Targetnumber, "100");
	}
	
	@Test(expected = TransferException.class)
	public void TargetAccountNotConfirmed() throws BankException, ClientException, AccountException, NumberFormatException, SibsException, OperationException, AssociateException, TransferException {
		//Target Account
		String Targernumber2= "94765765";
		Associatecontroller.associate_mbway(iban3, Targernumber2);
		
		controller.mbway_transfer(Sourcenumber, Targernumber2, "100");
	}
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
