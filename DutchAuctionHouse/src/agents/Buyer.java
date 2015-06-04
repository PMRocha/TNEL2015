package agents;

import algorithm.NashBalance;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Buyer extends Agent {

	private static final long serialVersionUID = 1L;
	private AID seller;
	private int money;
	private double priceOfBid;
	private AID client;
	private String product;
	private int quantity;
	private int algorithm;
	private int valueGiven;

	private NashBalance nash;

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
				if (msgParts[1].equals("AcceptEntrance")) {
					if (algorithm == 1) {
						nash = new NashBalance(valueGiven,
								Double.parseDouble(msgParts[2]), 1);
						priceOfBid = valueGiven-nash.solve()*valueGiven;
					} else if (algorithm == 2) {
						nash = new NashBalance(valueGiven,
								Double.parseDouble(msgParts[2]), 30);
						priceOfBid = valueGiven-nash.solve()*valueGiven;
					} 
					else
						priceOfBid =valueGiven;
					
					System.err.println(priceOfBid);
				}
				
				else if (msgParts[1].equals("Auction")) {
					// random behaviour
					if (Double.parseDouble(msgParts[4]) <= priceOfBid) {
						ACLMessage reply = msg.createReply();
						reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
						if (quantity > Integer.parseInt(msgParts[3]))
							quantity = Integer.parseInt(msgParts[3]);

						reply.setContent("Buyer-Bid-" + product + "-"
								+ quantity);
						send(reply);
					}
				} else if (msgParts[1].equals("AcceptBid")) {
					ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
					reply.addReceiver(client);
					reply.setContent("Buyer-Bought-" + msgParts[2] + "-"
							+ msgParts[3]);
					send(reply);
					System.out.println(this.getAgent().getLocalName()
							+ " bidded for " + msgParts[3]);
					this.myAgent.doDelete();
				}
				else if (msgParts[1].equals("AuctionEnded")) {
					ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
					reply.addReceiver(client);
					reply.setContent("Buyer-Bought-" + product + "-" + 0);
					send(reply);
					this.myAgent.doDelete();
				}
				else if (msgParts[1].equals("BuyersInAuctions")) {
					if(algorithm==2)
					{
						nash = new NashBalance(valueGiven,
								Double.parseDouble(msgParts[3]),Integer.parseInt(msgParts[2] ));
						priceOfBid = valueGiven-nash.solve()*valueGiven;
						System.err.println(priceOfBid);
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
		System.out.println("create");
		if (args.length == 7) {

			product = (String) args[0];
			quantity = (int) args[1];
			money = (int) args[2];
			seller = (AID) args[3];
			client = (AID) args[4];
			valueGiven = (int) args[5];
			algorithm = (int) args[6];
			
			
		} else {
			System.err.println("Parametros inválidos no buyer:" + args.length);
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
