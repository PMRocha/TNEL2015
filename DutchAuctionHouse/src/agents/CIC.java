package agents;

import structures.CICStructure;
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
	private CICStructure cicStructure;


	class CICBehaviour extends SimpleBehaviour {

		private static final long serialVersionUID = 1L;

		// construtor do behaviour
		public CICBehaviour(Agent a) {
			super(a);
		}

		// m�todo action
		public void action() {
			ACLMessage msg = blockingReceive();
			ACLMessage reply = msg.createReply();

			String[] msgParts = msg.getContent().split("-");
			
		
			if(msgParts[0].equals("Enter"))
			{
				cicStructure.addClient(msg.getSender().getLocalName());
				System.out.println(cicStructure.getClients().toString());
			}

		}

		
		// m�todo done
		public boolean done() {
			return false;
		}
	}



	protected void setup() {

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		
		cicStructure=new CICStructure();
		
		//Object[] args = getArguments();

		/*if (args.length == 0) {
			System.out.println("teste");
		}  else {
			System.err.println("Parametros inv�lidos em CIC");
			System.exit(1);
		}*/

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
	}

}