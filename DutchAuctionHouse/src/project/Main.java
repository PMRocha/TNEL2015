package project;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import agents.CIC;



public class Main {
	public static void main(String arg[]){
		Runtime rt = Runtime.instance();
		
		Profile p = new ProfileImpl();
		// Create a new non-main container, connecting to the default
		// main container (i.e. on this host, port 1099)
		ContainerController cc = rt.createMainContainer(p);
		
		
		try {
			AgentController rma = cc.createNewAgent("rma",
					"jade.tools.rma.rma", null);

			rma.start();
			
			//AgentController CIC = cc.createNewAgent("CIC",
					//"agents.CIC", Object[]);
			
			
			
			AgentController CIC = cc.acceptNewAgent("CIC", new CIC());
			CIC.start();
			//product,quantity
			AgentController s1 = cc.createNewAgent("s1","agents.Shop", new Object[]{"pc",15,150});
			/*AgentController s2 = cc.createNewAgent("s2","agents.Shop", argument);
			s2.start();*/
			
			//product,quantity,money
			AgentController c1 = cc.createNewAgent("c1","agents.Client", new Object[]{"pc",5,1000});
			AgentController c2 = cc.createNewAgent("c2","agents.Client", new Object[]{"pc",15,1000});
			
			
			c2.start();
			c1.start();
			s1.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
