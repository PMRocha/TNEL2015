package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class Buyer extends Agent {

	private static final long serialVersionUID = 1L;
	private AID seller;
	private int money;
	private String product;
	private int quantity;


	class BuyerBehaviour extends SimpleBehaviour {
		private static final long serialVersionUID = 1L;

		// construtor do behaviour
		public BuyerBehaviour(Agent a) {
			super(a);
		}

		// action method
		public void action() {
			ACLMessage msg = blockingReceive();
			String[] msgParts = msg.getContent().split("-");

			if (msgParts[0].equals("Seller")) {
				if (msgParts[1].equals("Auction")) {
					// random behaviour
					if (Integer.parseInt(msgParts[4])< money) {
						ACLMessage reply = msg.createReply();
						reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
						reply.setContent("Buyer-Bid-"+product+"-"+quantity);
						send(reply);
					}
				}
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

		if (args.length == 3) {

			seller = (AID) args[2];
			money=199;
			product=(String) args[0];
			quantity=(int) args[1];

		} else {
			System.err.println("Parametros inválidos no buyer" + args.length);
			System.exit(1);
		}

		// creates behaviour
		BuyerBehaviour c = new BuyerBehaviour(this);
		addBehaviour(c);

		// adds client to service
		sd.setType("Buyer");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

		msg.addReceiver(seller);

		msg.setContent("Buyer-Enter");
		send(msg);

	}

}
