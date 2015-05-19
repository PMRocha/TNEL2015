package agents;

import java.util.ArrayList;

import structures.ClockTimer;
import structures.ClientAuctions;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Client extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean registered;
	private String product;
	private int quantity;
	private int buyerNumber;
	private AID CIC;
	private ClientAuctions clientAuctions;
	private ClockTimer clock;

	class ClientBehaviour extends SimpleBehaviour {

		private static final long serialVersionUID = 1L;

		// construtor do behaviour
		public ClientBehaviour(Agent a) {
			super(a);
		}

		// action method
		public void action() {
			ACLMessage msg = receive();

			if (msg != null) {
				String[] msgParts = msg.getContent().split("-", 3);

				if (!registered) {
					if (msgParts[0].equals("CIC")) {
						if (msgParts[1].equals("EnterSuccessful")) {
							registered = true;
						}
					}
				}
				// if client is registered
				else {
					if (msgParts[0].equals("CIC")) {
						if (msgParts[1].equals("Auctions")) {
							clientAuctions.parseStringAuction(msgParts[2]);

							// System.err.println(msgParts[2]);
							ArrayList<AID> sellers = clientAuctions
									.getAuctionsWithoutBuyer();

							// System.err.println(sellers.toString());

							if (sellers.size() > 0) {
								try {
									for (int i = 0; i < sellers.size(); i++) {
										Object[] arguments = new Object[4];
										arguments[0] = product;
										arguments[1] = quantity;
										arguments[2] = sellers.get(i);
										arguments[3] = this.getAgent().getAID();

										// initializes agents for auction
										AgentController buy1;

										buy1 = getContainerController()
												.createNewAgent(
														getLocalName()
																+ "Buyer"
																+ buyerNumber,
														"agents.Buyer",
														arguments);

										buy1.start(); // acceptNewAgent("name1",
														// new
														// Agent());
										buyerNumber++;
									}
								} catch (StaleProxyException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
					else if (msgParts[0].equals("Buyer")) {
						if(msgParts[1].equals("Bought"))
						{
							ACLMessage reply=new ACLMessage(ACLMessage.INFORM);
							reply.addReceiver(CIC);
							reply.setContent("Client-Exit");
							send(reply);
							this.myAgent.doDelete();
						}
					}
					else
						System.out.println(msg.getContent());
				}
			}
			// if message has no content
			else {
				if (clock.isTriggered()) {
					msg = new ACLMessage(ACLMessage.QUERY_IF);
					msg.setContent("Client-AvailableAuctions-" + product + "-"
							+ quantity);
					msg.addReceiver(CIC);
					send(msg);
					clock.setTriggered(false);
				}
			}
		}

		// done method
		public boolean done() {
			return quantity == 0;
		}
	}

	protected void setup() {

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());

		// arguments
		// Object[] args = getArguments();
		product = "pc";
		quantity = 10;
		clientAuctions = new ClientAuctions();
		clock = new ClockTimer(3);// refresh rate
		clock.runTime();

		/*
		 * if (args.length == 0) { System.out.println("teste"); } else {
		 * System.err.println("Parametros inválidos no client"); System.exit(1);
		 * }
		 */

		// adds client to service
		sd.setType("Client");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		// creates behaviour
		ClientBehaviour c = new ClientBehaviour(this);
		addBehaviour(c);

		// searches agent type CIC
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();

		sd1.setType("CIC");
		template.addServices(sd1);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			CIC = result[0].getName();
			msg.addReceiver(CIC);

			msg.setContent("Client-Enter");
			send(msg);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

	}

}
