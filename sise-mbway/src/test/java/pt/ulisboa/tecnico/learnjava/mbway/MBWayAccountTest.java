package pt.ulisboa.tecnico.learnjava.mbway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayAccount;


public class MBWayAccountTest {
	
	String iban;
	String number;
	String code;
	
	@Before
	public void setUp() {
		iban = "CGD005";
		number = "933483474";
		code = "1234";
	}
	
	@Test
	public void success() throws AccountException {
		MBWayAccount Account = new MBWayAccount(iban,number,code);
		assertEquals(Account.getCode(), "1234");
		assertEquals(Account.getIban(), "CGD005");
		assertFalse(Account.is_active());
	}
	
	@Test(expected = AccountException.class)
	public void FailIbanNull() throws AccountException {
		MBWayAccount Account = new MBWayAccount(null,number,code);
	}
	
	@Test(expected = AccountException.class)
	public void FailNumberNull() throws AccountException {
		MBWayAccount Account = new MBWayAccount(iban,null,code);
	}
	


}
