package project;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import agents.CIC;
import agents.Shop;


public class Main {
	public static void main(String arg[]){
		Runtime rt = Runtime.instance();
		
		Profile p = new ProfileImpl();
		// Create a new non-main container, connecting to the default
		// main container (i.e. on this host, port 1099)
		ContainerController cc = rt.createMainContainer(p);
		
		Object[] argument =new Object[0];
		
		try {
			AgentController rma = cc.createNewAgent("rma",
					"jade.tools.rma.rma", null);

			rma.start();
			
			//AgentController CIC = cc.createNewAgent("CIC",
					//"agents.CIC", Object[]);
			
			
			
			AgentController CIC = cc.acceptNewAgent("CIC", new CIC());
			CIC.start();
			
			AgentController s1 = cc.createNewAgent("s1","agents.Shop", argument);
			s1.start();
			
			AgentController s2 = cc.createNewAgent("s2","agents.Shop", argument);
			s2.start();
			
			AgentController c1 = cc.createNewAgent("c1","agents.Client", argument);
			c1.start();
			
			AgentController c2 = cc.createNewAgent("c2","agents.Client", argument);
			c2.start();

		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
