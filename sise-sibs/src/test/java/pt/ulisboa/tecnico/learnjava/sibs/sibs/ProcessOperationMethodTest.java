package pt.ulisboa.tecnico.learnjava.sibs.sibs;

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
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Processed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class ProcessOperationMethodTest {
	
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String NIF = "123456789";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "António";

	private Sibs sibs;
	private Bank sourceBank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;
	
	
	
	private Account sourceAccount;
	private Account targetAccount;
	private Account sourceAccount2;
	private Account targetAccount2;
	private Account sourceAccount3;
	private Account targetAccount3;
	
	private String TARGET_IBAN;
	private String SOURCE_IBAN;
	private String TARGET_IBAN2;
	private String SOURCE_IBAN2;
	private String TARGET_IBAN3;
	private String SOURCE_IBAN3;
	
	@Before
	public void setUp() throws BankException, AccountException, ClientException {
		this.services = new Services();
		this.sibs = new Sibs(100, services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.sourceClient = new Client(this.sourceBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.targetBank, FIRST_NAME, LAST_NAME, NIF, PHONE_NUMBER, ADDRESS, 22);
	
		SOURCE_IBAN = sourceBank.createAccount(AccountType.CHECKING, sourceClient, 100, 0);
		TARGET_IBAN = targetBank.createAccount(AccountType.CHECKING, targetClient, 100, 0);
		SOURCE_IBAN2 = sourceBank.createAccount(AccountType.CHECKING, sourceClient, 100, 0);
		TARGET_IBAN2 = targetBank.createAccount(AccountType.CHECKING, targetClient, 100, 0);
		SOURCE_IBAN3 = sourceBank.createAccount(AccountType.CHECKING, sourceClient, 100, 0);
		TARGET_IBAN3 = targetBank.createAccount(AccountType.CHECKING, targetClient, 100, 0);
		this.sourceAccount = services.getAccountByIban(SOURCE_IBAN);
		this.targetAccount = services.getAccountByIban(TARGET_IBAN);
		this.sourceAccount2 = services.getAccountByIban(SOURCE_IBAN2);
		this.targetAccount2 = services.getAccountByIban(TARGET_IBAN2);
		this.sourceAccount3 = services.getAccountByIban(SOURCE_IBAN3);
		this.targetAccount3 = services.getAccountByIban(TARGET_IBAN3);
		
	}
	
	@Test
	public void success() throws OperationException, AccountException, SibsException {
		sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, 50);
		sibs.addOperation(SOURCE_IBAN2, TARGET_IBAN2, 50);
		sibs.addOperation(SOURCE_IBAN3, TARGET_IBAN3, 50);
		
		// operation(0) is registered goes to withdrawn
		// operation(1) is withdrawn goes to deposited
		sibs.getOperation(1).process();
		// operation(2) is deposited goes to processed
		sibs.getOperation(2).process();
		sibs.getOperation(2).process();
		
		
		sibs.processOperations();

		assertTrue(sibs.getOperation(0).getState().getClass().isInstance(new Withdrawn()));
		assertEquals(this.sourceAccount.getBalance(),50);
		assertEquals(this.targetAccount.getBalance(),100);
		
		
		assertTrue(sibs.getOperation(1).getState().getClass().isInstance(new Deposited()));
		assertEquals(this.sourceAccount2.getBalance(),50);
		assertEquals(this.targetAccount2.getBalance(),150);
		
		
		assertTrue(sibs.getOperation(2).getState().getClass().isInstance(new Processed()));
		assertEquals(this.sourceAccount3.getBalance(),49);
		assertEquals(this.targetAccount3.getBalance(),150);
	}
	
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}
