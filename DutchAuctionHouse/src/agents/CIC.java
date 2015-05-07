package agents;

import javafx.application.Application;
import gui.CICGUI;
import structures.AuctionsList;
import structures.RegisteredPeople;
import jade.core.AID;
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
	public RegisteredPeople getRegister() {
		return register;
	}

	public void setRegister(RegisteredPeople register) {
		this.register = register;
	}

	public AuctionsList getAuctions() {
		return auctions;
	}

	public void setAuctions(AuctionsList auctions) {
		this.auctions = auctions;
	}

	private RegisteredPeople register;
	private AuctionsList auctions;
	private CICGUI gui;

	class CICBehaviour extends SimpleBehaviour {

		private static final long serialVersionUID = 1L;

		// construtor do behaviour
		public CICBehaviour(Agent a) {
			super(a);
		}

		// método action
		public void action() {
			ACLMessage msg = blockingReceive();

			System.out.println(msg.getContent());
			String[] msgParts = msg.getContent().split("-");

			if (msgParts[0].equals("Client")) {
				ClientCommunication(msgParts, msg);
			} else if (msgParts[0].equals("Shop")) {
				ShopCommunication(msgParts, msg);
			}
		}

		//handles messages from Shop
		private void ShopCommunication(String[] msgParts, ACLMessage msg) {
			ACLMessage reply=msg.createReply();
			
			if (msgParts[1].equals("Enter")) {
				register.addShop(new AID(msg.getSender().getLocalName(),AID.ISLOCALNAME));

				reply.setContent("CIC-EnterSuccessful");
				reply.setPerformative(ACLMessage.CONFIRM);
				send(reply);
			}
			else if(msgParts[1].equals("CreateAuction"))
			{
				System.out.println(msgParts[2]+msgParts[3]+msgParts[4]);
				auctions.addAuction(msgParts[2],Integer.parseInt(msgParts[3]),new AID(msgParts[4],AID.ISLOCALNAME));
				auctions.getAuctions().toString();
			}
			else
				System.out.println(msg.getContent());
		}

		//handles messages from Client
		private void ClientCommunication(String[] msgParts, ACLMessage msg) {
			ACLMessage reply=msg.createReply();
			
			if (msgParts[1].equals("Enter")) {
				register.addClient(new AID(msg.getSender().getLocalName(),AID.ISLOCALNAME));
				System.out.println(register.getClients().toString());
				
				reply.setContent("CIC-EnterSuccessful");
				reply.setPerformative(ACLMessage.CONFIRM);
				send(reply);
			}
			else if (msgParts[1].equals("AvailableAuctions")) {
				
				if(auctions.getAuctionsOfProduct(msgParts[2])==null)
				{
				reply.setContent("CIC-NoAuction");
				reply.setPerformative(ACLMessage.FAILURE);
				}
				else
				{
					reply.setContent("CIC-Auctions-"+auctions.getAuctionsOfProduct(msgParts[2]).toString());
					reply.setPerformative(ACLMessage.INFORM);
				}
				send(reply);
			}
			else
				System.out.println(msg.getContent());
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
		auctions = new AuctionsList();
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
		Application.launch(CICGUI.class);
	}

}