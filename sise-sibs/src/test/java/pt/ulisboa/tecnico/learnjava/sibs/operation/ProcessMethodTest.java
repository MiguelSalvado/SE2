package pt.ulisboa.tecnico.learnjava.sibs.operation;

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
import pt.ulisboa.tecnico.learnjava.sibs.domain.Cancelled;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Deposited;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Processed;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Retry;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Error;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Withdrawn;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class ProcessMethodTest {
	
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
	private String TARGET_IBAN;
	private String SOURCE_IBAN;
	
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
		this.sourceAccount = services.getAccountByIban(SOURCE_IBAN);
		
		this.targetAccount = services.getAccountByIban(TARGET_IBAN);
		
	}
	
	@Test
	public void successFromRegisteredToWithdrawn() throws BankException, AccountException, SibsException, OperationException, ClientException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 50);
		operation.process();
		assertTrue(operation.getState().getClass().isInstance(new Withdrawn()));
		assertEquals(this.sourceAccount.getBalance(),50);
		assertEquals(this.targetAccount.getBalance(),100);
	}
	
	
	@Test
	public void successFromRegisteredToDeposited() throws BankException, AccountException, SibsException, OperationException, ClientException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 50);
		operation.process();
		operation.process();
		assertTrue(operation.getState().getClass().isInstance(new Deposited()));
		assertEquals(this.sourceAccount.getBalance(),50);
		assertEquals(this.targetAccount.getBalance(),150);
	}
	
	@Test
	public void successFromRegisteredToProcessed() throws BankException, AccountException, SibsException, OperationException, ClientException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 50);
		operation.process();
		operation.process();
		operation.process();
		assertTrue(operation.getState().getClass().isInstance(new Processed()));
		assertEquals(this.sourceAccount.getBalance(),49);
		assertEquals(this.targetAccount.getBalance(),150);
	}
	
	@Test
	public void ProcessAProcessedOperation() throws BankException, AccountException, SibsException, OperationException, ClientException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 50);
		operation.process();
		operation.process();
		operation.process();
		operation.process();
		assertTrue(operation.getState().getClass().isInstance(new Processed()));
		assertEquals(this.sourceAccount.getBalance(),49);
		assertEquals(this.targetAccount.getBalance(),150);
	}
	
	@Test
	public void ProcessCancelledOperation() throws BankException, AccountException, SibsException, OperationException, ClientException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 50);
		operation.cancel();
		operation.process();
		assertTrue(operation.getState().getClass().isInstance(new Cancelled()));
		assertEquals(this.sourceAccount.getBalance(),100);
		assertEquals(this.targetAccount.getBalance(),100);
	}
	
	@Test
	public void ProcessWithRegisteredToWithdrawnFail() throws OperationException, AccountException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 150);
		operation.process();
		assertTrue(operation.getState().getClass().isInstance(new Retry(operation.getState())));
		
	}
	
	@Test 
	public void ProcessWithWithdrawnToDepositedFail() throws OperationException, AccountException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, "BPI000", 50);
		operation.process(); // Withdrawn
		operation.process(); // Retry
		assertTrue(operation.getState().getClass().isInstance(new Retry(operation.getState())));
	}
	
	
	@Test 
	public void ProcessWithDepositedToProcessedFail() throws OperationException, AccountException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 100);
		operation.process(); // Withdrawn
		operation.process(); // Deposited
		operation.process(); // Retry
		assertTrue(operation.getState().getClass().isInstance(new Retry(operation.getState())));
	}
	
	
	@Test
	public void ProcessWithRegisteredToWithdrawnError() throws OperationException, AccountException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 150);
		operation.process(); // 0 - 1 
		operation.process(); // 1 - 2 
		operation.process(); // 2 - 3 
		operation.process(); // Error
		assertTrue(operation.getState().getClass().isInstance(new Error()));
	}
	
	
	@Test
	public void ProcessWithWithdrawnToDepositedError() throws OperationException, AccountException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, "BPI000", 50);
		operation.process(); // Withdrawn
		operation.process(); // 0 - 1 
		operation.process(); // 1 - 2 
		operation.process(); // 2 - 3 
		operation.process(); // Error
		assertTrue(operation.getState().getClass().isInstance(new Error()));
		
		assertEquals(this.sourceAccount.getBalance(),100);
		assertEquals(this.targetAccount.getBalance(),100);
	}
	
	@Test
	public void ProcessWithDepositedToProcessedError() throws OperationException, AccountException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 100);
		operation.process(); // Withdrawn
		operation.process(); // Deposited
		operation.process(); // 0 - 1 
		operation.process(); // 1 - 2 
		operation.process(); // 2 - 3 
		operation.process(); // Error
		assertTrue(operation.getState().getClass().isInstance(new Error()));
		
		assertEquals(this.sourceAccount.getBalance(),100);
		assertEquals(this.targetAccount.getBalance(),100);
	}
	
	
	@Test
	public void ProcessFromRetryToWithdrawnSuccess() throws OperationException, AccountException, BankException, ClientException {
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 150);
		operation.process(); // Retry - From Registered To Withdrawn
		assertTrue(operation.getState().getClass().isInstance(new Retry(operation.getState())));
		
		//Deposit amount into source_iban
		this.services.deposit(SOURCE_IBAN, 100);
		
		operation.process(); // Success - From Registered to Withdrawn
		assertTrue(operation.getState().getClass().isInstance(new Withdrawn()));
		
		assertEquals(this.sourceAccount.getBalance(),50);
		assertEquals(this.targetAccount.getBalance(),100);
	}
	
	
	@After
	public void tearDown() {
		Bank.clearBanks();
	}
}
