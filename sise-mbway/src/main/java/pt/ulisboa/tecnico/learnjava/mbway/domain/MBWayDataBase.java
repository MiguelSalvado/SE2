package pt.ulisboa.tecnico.learnjava.mbway.domain;

import java.util.TreeMap;

import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;

public class MBWayDataBase {
	
	private TreeMap<String, MBWayAccount> MBWay;
	private Sibs sibs;
	
	
	public MBWayDataBase(Sibs sibs) {
		// TODO Auto-generated constructor stub
		this.MBWay = new TreeMap<String, MBWayAccount>();
		this.sibs = sibs;
	}

	public void addMBWay(String number, MBWayAccount client) {
		MBWay.put(number, client);
	}

	public Sibs getSibs() {
		return sibs;
	}
	
	public MBWayAccount getAccount(String number) {
		return MBWay.get(number);
	}
	
	public boolean containsNumber(String number) {
		return this.MBWay.containsKey(number);
	}
	
	

}
