package pt.ulisboa.tecnico.learnjava.mbway.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.TreeMap;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank.AccountType;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.AssociateMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.ConfirmMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.ExitMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.SplitInsuranceMBWayController;
import pt.ulisboa.tecnico.learnjava.mbway.controllers.TransferMBWayController;

import pt.ulisboa.tecnico.learnjava.mbway.domain.MBWayDataBase;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.AssociateException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.ConfirmException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SplitException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.TransferException;

public class MBWayView {	
	
	public static void exit_print(ExitMBWayController controller) {
		System.out.println(controller.exit());
		System.exit(0);
	}
	
	
	public static void associate_print(AssociateMBWayController controller, String iban, String number) throws AccountException, AssociateException {
		try{
			System.out.println(controller.associate_mbway(iban, number));
		}
		catch(AssociateException ae){
			System.out.println(ae.getMsg());
		}	
	}
	
	
	//Confirm
	public static void confirm_print(ConfirmMBWayController controller, String number, String code) {
		try {
			System.out.println(controller.confirm_mbway(number, code));
		}
		catch(ConfirmException ce) {
			System.out.println(ce.getMsg());
		}
		
	}
	
	//transfer
	public static void transfer_print(TransferMBWayController controller, String SourceNumber, String TargetNumber, String Amount) throws NumberFormatException, SibsException, AccountException, OperationException, TransferException {
		try {
			System.out.println(controller.mbway_transfer(SourceNumber, TargetNumber, Amount));
		}
		catch(TransferException te) {
			System.out.println(te.getMsg());
		}
		
	}
	
	
	//Split
	public static void split_print(SplitInsuranceMBWayController controller, int n_mebs, int Amount, TreeMap<String, String> Family) throws NumberFormatException, AccountException, SplitException {
		try {
			System.out.println(controller.mbway_split_insurance(n_mebs, Amount, Family));
		}
		catch(SplitException se) {
			System.out.println(se.getMsg());
		}	
	}
	
	
	
	public static void main(String[] args) throws IOException, AccountException, BankException, ClientException, NumberFormatException, SibsException, OperationException, AssociateException, SplitException, TransferException {
		
		Bank bank1 = new Bank("CGD");
		Client client1 = new Client(bank1, "Maria", "Soares", "123456789", "912346987", "Rua Alves Redol", 25);
		String iban1 = bank1.createAccount(AccountType.CHECKING, client1, 1000, 0);
		System.out.println(iban1);
		
		Services services = new Services();
		Sibs sibs = new Sibs(100,services);
		MBWayDataBase model = new MBWayDataBase(sibs);
		
		ExitMBWayController ExitController = new ExitMBWayController();
		AssociateMBWayController AssociateController = new AssociateMBWayController(model);
		ConfirmMBWayController ConfirmController = new ConfirmMBWayController(model);
		SplitInsuranceMBWayController SplitInsuranceController = new SplitInsuranceMBWayController(model);
		TransferMBWayController TransferController = new TransferMBWayController(model);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			String line = br.readLine();
			String[] split = line.split("\\s+");	
			
			switch(split[0]) {
				case "exit": 
					exit_print(ExitController);
					break;
				case "associate-mbway":
					associate_print(AssociateController, split[1], split[2]);
					break;
				
				case "confirm-mbway":
					confirm_print(ConfirmController,split[1], split[2]);
					break;	
				case "mbway-transfer":
					transfer_print(TransferController, split[1], split[2], split[3]);
					break;
				case "mbway-split-insurance":
					
					int numberFamily = Integer.parseInt(split[1]);
					int TotalAmount = Integer.parseInt(split[2]);
					TreeMap<String, String> Family =  new TreeMap<String, String>();
					
					String line1 = br.readLine();
					String[] split1 = line1.split("\\s+");
					
					while(!split1[0].equals("end")) {
						line1 = br.readLine();
						split1 = line1.split("\\s+");
						Family.put(split1[1], split1[2]);
					
					}
					split_print(SplitInsuranceController, numberFamily, TotalAmount, Family);
					break;
			}
			
		}
	}
}

