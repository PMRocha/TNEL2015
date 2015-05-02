package agents;


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


	class ClientBehaviour extends SimpleBehaviour {

		private static final long serialVersionUID = 1L;

		// construtor do behaviour
		public ClientBehaviour(Agent a) {
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
			
			if (registered)
			{
				
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

		if (args.length == 0) {
			System.out.println("teste");
		}  else {
			System.err.println("Parametros inválidos no client");
			System.exit(1);
		}

		//adds client to service
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
		
		
		//searches agent type CIC
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();

		sd1.setType("CIC");
		template.addServices(sd1);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			for (int i = 0; i < result.length; ++i)
				msg.addReceiver(result[i].getName());

			msg.setContent("Client-Enter-"+getName());
			send(msg);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
	}

}
