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

public class Seller extends Agent {

	private static final long serialVersionUID = 1L;
	private String product;
	private int quantity;
	private int price;
	private boolean auctionStarted;
	private String shop;

	private ClockTimer clock;
	private ArrayList<AID> buyers;

	// communication behaviour
	class SellerBehaviour extends SimpleBehaviour {

		private static final long serialVersionUID = 1L;

		// construtor do behaviour
		public SellerBehaviour(Agent a) {
			super(a);
		}

		// action method
		public void action() {
			ACLMessage msg = receive();

			ACLMessage reply;
			
			if (msg != null) {
				String[] msgParts = msg.getContent().split("-");
				if (msgParts[0].equals("Buyer")) {
					if (msgParts[1].equals("Enter")) {
						buyers.add(msg.getSender());
						System.out.println(buyers.size());
					}
					else if(msgParts[1].equals("Bid"))
					{
						quantity=0;
						System.out.println("teste");
						reply = new ACLMessage(ACLMessage.INFORM);
						reply.addReceiver(new AID(shop,AID.ISLOCALNAME));
						reply.setContent("SOLD!");
						send(reply);
					}
				}
				
			} else {
				if (clock.isTriggered()) {
					if (!auctionStarted) {
						auctionStarted = true;
						clock.setTriggered(false);
					} else {
						reply = new ACLMessage(ACLMessage.PROPOSE);
						price--;

						for (int i = 0; i < buyers.size(); i++) {
							reply.addReceiver(buyers.get(i));
						}

						reply.setContent("Seller-Auction-" + product + "-"
								+ quantity + "-" + price);
						send(reply);
						clock.setTriggered(false);

					}
					// System.out.println("BUY NOW");

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

			product = (String) args[0];
			quantity = (int) args[1];
			buyers = new ArrayList<AID>();
			price = 200;
			auctionStarted = false;
			shop=(String) args[2];
			System.out.println("I'm seller and I sell " + product + " q: "
					+ quantity);
			clock = new ClockTimer(3);
			clock.runTime();
		} else {
			System.err.println("Parametros inválidos no client");
		}

		// creates behaviour
		SellerBehaviour c = new SellerBehaviour(this);
		addBehaviour(c);

		// adds client to service
		sd.setType("Seller");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

	}

}
