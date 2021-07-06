package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class GetNumberOfOperationsMethodTest {
	private static final String TARGET_IBAN = "TargetIban";
	private static final String SOURCE_IBAN = "SourceIban";
	private static final int VALUE = 100;

	private Sibs sibs; 
	
	
	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(3, new Services());
	}
	
	
	@Test
    public void TestNumberOfOperationsEmpty() {
        assertEquals(this.sibs.getNumberOfOperations(), 0);
    }
	
	@Test
	public void TestNumberAddedOperations() throws OperationException, SibsException {
		this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
		assertEquals(this.sibs.getNumberOfOperations(), 2);
	}
	
	@Test
	public void TestNumberAddedAndRemovedOperations() throws OperationException, SibsException {
		this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.removeOperation(1);
		assertEquals(this.sibs.getNumberOfOperations(), 1);
	}
	
	@After
	public void tearDown() {
		this.sibs = null;
	}
}
