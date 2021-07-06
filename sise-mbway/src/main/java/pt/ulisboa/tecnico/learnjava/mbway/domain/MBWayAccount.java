package pt.ulisboa.tecnico.learnjava.mbway.domain;

public class MBWayAccount {
	
	private String code;
	private String iban;
	private String number;
	private boolean active = false;

	public MBWayAccount(String iban,String number,String code) {
		this.iban = iban;
		this.number = number;
		this.code = code;
	}

	public boolean is_active() {
		return active;
	}

	public void set_active() {
		this.active = true;
	}

	public String getCode() {
		return code;
	}

	public String getIban() {
		return iban;
	}

	
	

}
