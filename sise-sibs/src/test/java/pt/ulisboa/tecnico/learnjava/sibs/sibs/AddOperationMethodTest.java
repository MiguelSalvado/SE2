package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class AddOperationMethodTest {
	private static final String TARGET_IBAN = "TargetIban";
	private static final String SOURCE_IBAN = "SourceIban";
	private static final int VALUE = 100;

	private Sibs sibs; 
	
	
	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(2, new Services());
	}
	
	@Test 
	public void success() throws OperationException, SibsException{
		int position = this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
		assertEquals(position, 0);
		assertEquals(sibs.getOperation(position).getSourceIban(), "SourceIban");
		assertEquals(sibs.getOperation(position).getTargetIban(), "TargetIban");
		assertEquals(sibs.getOperation(position).getValue(), 100);
		int position2 = this.sibs.addOperation("SourceIban2", "TargetIban2", 50);
		assertEquals(position2, 1);
		assertEquals(sibs.getOperation(position2).getSourceIban(), "SourceIban2");
		assertEquals(sibs.getOperation(position2).getTargetIban(), "TargetIban2");
		assertEquals(sibs.getOperation(position2).getValue(), 50);
	}
	
	@Test(expected = SibsException.class)
    public void positionAboveLength() throws OperationException, SibsException {
			this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
			this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
			this.sibs.addOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
    }
	
	@After
	public void tearDown() {
		this.sibs = null;
	}
}
