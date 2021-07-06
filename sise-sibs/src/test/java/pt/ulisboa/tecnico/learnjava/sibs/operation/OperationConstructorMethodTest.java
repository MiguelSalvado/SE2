package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Registered;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class OperationConstructorMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;
	private Sibs sibs;

	@Test
	public void success() throws OperationException {
		this.sibs = new Sibs(2, new Services());
		Operation operation = new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, VALUE);

		assertEquals(VALUE, operation.getValue());
		assertEquals(SOURCE_IBAN, operation.getSourceIban());
		assertEquals(TARGET_IBAN, operation.getTargetIban());
		assertEquals(2, operation.commission());
		assertTrue(operation.getState().getClass().isInstance(new Registered()));
	}

	@Test(expected = OperationException.class)
	public void nullSourceIban() throws OperationException {
		new Operation(sibs, null, TARGET_IBAN, VALUE);
	}
	
	@Test(expected = OperationException.class)
	public void nullTargetIban() throws OperationException {
		new Operation(sibs, SOURCE_IBAN, null, VALUE);
	}

	@Test(expected = OperationException.class)
	public void emptyTargetIban() throws OperationException {
		new Operation(sibs, SOURCE_IBAN, "", VALUE);
	}
	
	@Test(expected = OperationException.class)
	public void emptySourceIban() throws OperationException {
		new Operation(sibs, "", TARGET_IBAN, VALUE);
	}
	
	@Test(expected = OperationException.class)
	public void negativeValue() throws OperationException {
		new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, -VALUE);
	}
	
	@Test(expected = OperationException.class)
	public void zeroValue() throws OperationException {
		new Operation(sibs, SOURCE_IBAN, TARGET_IBAN, 0);
	}

}
