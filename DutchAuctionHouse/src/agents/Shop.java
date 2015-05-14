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

public class Shop extends Agent {

	private static final long serialVersionUID = 1L;
	private boolean registered;
	private AID CIC;
	
	private String product;
	private int quantity;
	private int sellerNumber;
	
	class ShopBehaviour extends SimpleBehaviour {

		private static final long serialVersionUID = 1L;
	
		// construtor do behaviour
		public ShopBehaviour(Agent a) {
			super(a);
		}

		// action method
		public void action() {
			ACLMessage msg = blockingReceive();

			String[] msgParts = msg.getContent().split("-");
			
			if (msgParts[0].equals("CIC")) {
				if(msgParts[1].equals("EnterSuccessful"))
				{
					registered=true;
				}
			}
			
			if(registered)
			{
				createAuction();
			}
			
			
		}

		
		private void createAuction() {
			
			try {
				Object[] arguments = new Object[3];
				arguments[0] = product;
				arguments[1] = quantity;
				arguments[2]= getName();
				
				AgentController sel1 =  getContainerController().createNewAgent(getLocalName()+"Seller"+sellerNumber,"agents.Seller",arguments);
				sel1.start(); //acceptNewAgent("name1", new Agent());
				
				ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
				msg.addReceiver(CIC);
				msg.setContent("Shop-CreateAuction-"+product+"-"+quantity+"-"+sel1.getName());
				System.out.println("auction");
				send(msg);
				sellerNumber++;
				
			} catch (StaleProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// done method
		public boolean done() {
			return false;
		}
	}



	protected void setup() {

		registered=false;
		
		//may change
		quantity=10;
		product="pc";
		sellerNumber=0;
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		
		//Object[] args = getArguments();

		/*if (args.length == 0) {
			System.out.println("teste");
		}  else {
			System.err.println("Parametros inválidos no client");
			System.exit(1);
		}*/

		//adds client to service
		sd.setType("Shop");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		// creates behaviour
		ShopBehaviour c = new ShopBehaviour(this);
		addBehaviour(c);
		
		
		//searches agent type CIC
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();

		sd1.setType("CIC");
		template.addServices(sd1);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			
			CIC=result[0].getName();
			msg.addReceiver(CIC);

			msg.setContent("Shop-Enter");
			send(msg);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
	}

}
