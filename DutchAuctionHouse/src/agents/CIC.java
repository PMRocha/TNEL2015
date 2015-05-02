package agents;

import structures.AuctionsList;
import structures.RegisteredPeople;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class CIC extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RegisteredPeople register;
	private AuctionsList auctions;

	class CICBehaviour extends SimpleBehaviour {

		private static final long serialVersionUID = 1L;

		// construtor do behaviour
		public CICBehaviour(Agent a) {
			super(a);
		}

		// método action
		public void action() {
			ACLMessage msg = blockingReceive();

			String[] msgParts = msg.getContent().split("-");

			if (msgParts[0].equals("Client")) {
				ClientCommunication(msgParts, msg);
			} else if (msgParts[0].equals("Shop")) {
				ShopCommunication(msgParts, msg);
			}
		}

		
		//handles messages from Client
		private void ShopCommunication(String[] msgParts, ACLMessage msg) {
			ACLMessage reply=msg.createReply();
			
			if (msgParts[1].equals("Enter")) {
				register.addShop(msg.getSender().getLocalName());
				System.out.println(register.getShops().toString());
				
				reply.setContent("CIC-EnterSuccessful");
				reply.setPerformative(ACLMessage.CONFIRM);
				send(reply);
				
			}
		}

		//handles messages from Shop
		private void ClientCommunication(String[] msgParts, ACLMessage msg) {
			ACLMessage reply=msg.createReply();
			
			if (msgParts[1].equals("Enter")) {
				register.addClient(msg.getSender().getLocalName());
				System.out.println(register.getClients().toString());
				
				reply.setContent("CIC-EnterSuccessful");
				reply.setPerformative(ACLMessage.CONFIRM);
				send(reply);
			}
		}

		// método done
		public boolean done() {
			return false;
		}
	}

	protected void setup() {

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());

		register = new RegisteredPeople();

		// Object[] args = getArguments();

		/*
		 * if (args.length == 0) { System.out.println("teste"); } else {
		 * System.err.println("Parametros inválidos em CIC"); System.exit(1); }
		 */

		sd.setType("CIC");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		// cria behaviour
		CICBehaviour c = new CICBehaviour(this);
		addBehaviour(c);
	}

}