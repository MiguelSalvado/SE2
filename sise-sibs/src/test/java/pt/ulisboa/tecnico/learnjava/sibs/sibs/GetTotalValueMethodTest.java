package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class GetTotalValueMethodTest {
	private static final String TARGET_IBAN = "TargetIban";
	private static final String SOURCE_IBAN = "SourceIban";

	private Sibs sibs; 
	
	
	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(3, new Services());
	}
	
	@Test
	public void TestTotalValueOfZero() throws OperationException, SibsException {
        assertEquals(this.sibs.getTotalValue(), 0);
    }
	
	@Test
    public void TestTotalValueOfOne() throws OperationException, SibsException {
		this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, 100);
        assertEquals(this.sibs.getTotalValue(), 100);
    }
	
	@Test
    public void TestTotalValueOfTwo() throws OperationException, SibsException {
		this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, 100);
		this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, 350);
        assertEquals(this.sibs.getTotalValue(), 450);
    }
	
	@After
	public void tearDown() {
		this.sibs = null;
	}
}
