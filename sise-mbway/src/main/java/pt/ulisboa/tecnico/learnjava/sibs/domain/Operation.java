package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Operation {

	private final int value;
	private final String targetIban;
	private final String sourceIban;

	public Operation(String sourceIban, String targetIban, int value) throws OperationException {
		checkParameters(value);
		this.value = value;

		if (invalidString(targetIban)) {
			throw new OperationException();
		}
		if (invalidString(sourceIban)) {
			throw new OperationException();
		}

		this.targetIban = targetIban;
		this.sourceIban = sourceIban;
	}

	private void checkParameters(int value) throws OperationException {

		if (value <= 0) {
			throw new OperationException(value);
		}
	}

	public int getValue() {
		return this.value;
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	public int commission() {
		return 1;
	}

	public String getTargetIban() {
		return this.targetIban;
	}
	public String getSourceIban() {
		return this.sourceIban;
	}

}
