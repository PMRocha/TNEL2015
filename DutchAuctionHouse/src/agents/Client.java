package agents;

import structures.ClockTimer;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Client extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean registered;
	private String product;
	private int quantity;
	private AID CIC;

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

			// if Client isn't registered
			if (!registered) {
				if (msg != null) {
					String[] msgParts = msg.getContent().split("-");

					if (msgParts[0].equals("CIC")) {
						if (msgParts[1].equals("EnterSuccessful")) {
							registered = true;
						}
					}
				}
			}

			
				if (msg == null) {

					if (clock.isTriggered()) {
						msg = new ACLMessage(ACLMessage.QUERY_IF);
						msg.setContent("Client-AvailableAuctions-" + product
								+ "-" + quantity);
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
		Object[] args = getArguments();
		product = "poop";
		quantity = 10;
		clock = new ClockTimer(3);// refresh rate
		clock.runTime();

		if (args.length == 0) {
			System.out.println("teste");
		} else {
			System.err.println("Parametros inválidos no client");
			System.exit(1);
		}

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
