package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import structures.ClockTimer;

public class Seller extends Agent {

	private static final long serialVersionUID = 1L;
	private String product;
	private int quantity;
	private int price;
	private int money;
	private boolean auctionStarted;
	private String shop;

	private ClockTimer clock;
	private ArrayList<AID> buyers;
	private boolean done;

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

			if (msg != null) {
				msgAction(msg);

			} else {
				noMsgAction();
			}
		}

		private void noMsgAction() {
			ACLMessage reply;
			if (clock.isTriggered()) {
				if (!auctionStarted) {
					auctionStarted = true;
					clock.setTriggered(false);
					System.out.println("auctionStarted");
				} else {
					//this WILL be changed
					System.out.println("price:"+price+",money:"+money);
					if (price > money) {
						reply = new ACLMessage(ACLMessage.PROPOSE);
						price=(int) (price*0.9);

						for (int i = 0; i < buyers.size(); i++) {
							reply.addReceiver(buyers.get(i));
						}

						reply.setContent("Seller-Auction-" + product + "-"
								+ quantity + "-" + price);
						
						send(reply);
						System.out.println(this.getAgent().getLocalName()+":"+reply.getContent());
						clock.setTriggered(false);
					}
					else 
					{
						endAuction();
					}
					

				}
				// System.out.println("BUY NOW");

			}
		}

		private void msgAction(ACLMessage msg) {
			ACLMessage reply;
			String[] msgParts = msg.getContent().split("-");
			if (msgParts[0].equals("Buyer")) {
				if (msgParts[1].equals("Enter")) {
					buyers.add(msg.getSender());
				} else if (msgParts[1].equals("Bid")) {

					// informs shop
					reply = new ACLMessage(ACLMessage.INFORM);
					reply.addReceiver(new AID(shop, AID.ISLOCALNAME));
					reply.setContent("Seller-Sold-" + msgParts[2] + "-"
							+ msgParts[3]);
					send(reply);

					// informs buyer that it accepted propose
					reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent("Seller-AcceptBid-" + msgParts[2] + "-"
							+ msgParts[3]);
					send(reply);
					buyers.remove(msg.getSender());

					quantity -= Integer.parseInt(msgParts[3]);

					if (quantity <= 0) {
						endAuction();
					}
				}
			}
		}

		private void endAuction() {
			ACLMessage reply;
			// end auction
			reply = new ACLMessage(ACLMessage.CANCEL);
			for (int i = 0; i < buyers.size(); i++) {
				reply.addReceiver(buyers.get(i));
			}
			reply.setContent("Seller-AuctionEnded");
			send(reply);
			done = true;
			this.myAgent.doDelete();
		}

		// done method
		public boolean done() {
			return done;
		}
	}

	protected void setup() {

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());

		Object[] args = getArguments();

		if (args.length == 4) {

			done = false;
			product = (String) args[0];
			quantity = (int) args[1];
			buyers = new ArrayList<AID>();
			price = 200;
			auctionStarted = false;
			shop = (String) args[2];
			money = (int) args[3];
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
