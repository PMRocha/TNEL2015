package project;

import agents.CIC;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.event.ActionEvent;


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

		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
