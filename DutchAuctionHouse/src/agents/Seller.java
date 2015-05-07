package agents;

import java.util.ArrayList;
import structures.ClockTimer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Seller extends Agent{

	private static final long serialVersionUID = 1L;
	private String product;
	private int quantity;
	
	private ClockTimer clock;
	private ArrayList<AID> buyers;
	
	//communication behaviour
	class SellerBehaviour extends SimpleBehaviour {

		private static final long serialVersionUID = 1L;
	
		// construtor do behaviour
		public SellerBehaviour(Agent a) {
			super(a);
		}

		// action method
		public void action() {
			ACLMessage msg = receive();
			
			if(clock.isTriggered())
			{
				//System.out.println("BUY NOW");
				clock.setTriggered(false);
			}
		}
	
		// done method
		public boolean done() {
			return false;
		}
	}
	
	


	protected void setup() {

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		
		Object[] args = getArguments();

		if (args.length == 2) {
			
			product=(String) args[0];
			quantity=(int) args[1];
			System.out.println("I'm seller and I sell "+ product+ " q: "+quantity);
			clock=new ClockTimer(10);
			clock.runTime();
		}  else {
			System.err.println("Parametros inválidos no client");
		}
		
		// creates behaviour
		SellerBehaviour c = new SellerBehaviour(this);
		addBehaviour(c);

		//adds client to service
		sd.setType("Seller");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
	}

}
