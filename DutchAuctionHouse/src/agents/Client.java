package agents;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;

import structures.ClientAuctions;
import structures.ClockTimer;

public class Client extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean registered;
	private String product;
	private int quantity;
	private int money;
	private int buyerNumber;
	private int algorithm;
	private int valueGiven;
	private AID CIC;
	private ClientAuctions clientAuctions;
	private ClockTimer clock;
	//Writer writer;

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
							
							if (sellers.size() > 0) {
								try {
									for (int i = 0; i < sellers.size(); i++) {
										Object[] arguments = new Object[7];
										arguments[0] = product;
										arguments[1] = quantity;
										arguments[2] = money;
										arguments[3] = sellers.get(i);
										arguments[4] = this.getAgent().getAID();
										arguments[5] = valueGiven;
										arguments[6] = algorithm;

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
					} else if (msgParts[0].equals("Buyer")) {
						if (msgParts[1].equals("Bought")) {
							msgParts = msg.getContent().split("-");
							quantity -= Integer.parseInt(msgParts[3]);
							money-=Double.parseDouble(msgParts[4]);
							if (quantity <= 0) {
								ACLMessage reply = new ACLMessage(
										ACLMessage.INFORM);
								reply.addReceiver(CIC);
								reply.setContent("Client-Exit");
								try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(getLocalName()+".log", true)))) {
								    out.write("Exited with "+money+" money");
								}catch (IOException e) {
								    //exception handling left as an exercise for the reader
								}
								send(reply);
								this.myAgent.doDelete();
							}

						}
					} else
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

		clientAuctions = new ClientAuctions();
		clock = new ClockTimer(6);// refresh rate
		clock.runTime();
		Object[] args = getArguments();

		if (args.length == 5) {
			File file = new File(getLocalName()+".log");
			if(file.length()!=0){
				Writer writer;
				try {
					writer = new BufferedWriter(new OutputStreamWriter(
					          new FileOutputStream(file), "utf-8"));
					writer.write("");
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}

			product = (String) args[0];
			quantity = (int) args[1];
			money = (int) args[2];
			valueGiven=(int) args[3];
			algorithm=(int) args[4];
			
			//0 normal 1 hash 2nash all
			switch(algorithm){
			case 0:
				try {
					Writer writer = new BufferedWriter(new OutputStreamWriter(
				              new FileOutputStream(file), "utf-8"));
					writer.write("Client Agent created with normal algorithm.\n");
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					Writer writer = new BufferedWriter(new OutputStreamWriter(
				              new FileOutputStream(file), "utf-8"));
					writer.write("Client Agent created with nash vs 1 algorithm.\n");
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					Writer writer = new BufferedWriter(new OutputStreamWriter(
				              new FileOutputStream(file), "utf-8"));
					writer.write("Client Agent created with nash vs all algorithm.\n");
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			

		} else {
			System.err.println("Parametros inválidos no client"+ args.length);
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
