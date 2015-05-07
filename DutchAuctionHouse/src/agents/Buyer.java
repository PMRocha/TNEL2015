package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Buyer extends Agent{

		private static final long serialVersionUID = 1L;
		private AID seller; 
		
		class SellerBehaviour extends SimpleBehaviour {
			private static final long serialVersionUID = 1L;
		
			// construtor do behaviour
			public SellerBehaviour(Agent a) {
				super(a);
			}

			// action method
			public void action() {
			
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
				
				seller=(AID) args[2];
				System.out.println(seller.getLocalName());
				
			}  else {
				System.err.println("Parametros inválidos no buyer"+args.length);
				System.exit(1);
			}

			//adds client to service
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
