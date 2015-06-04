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
			
			
			AgentController CIC = cc.acceptNewAgent("CIC", new CIC());
			CIC.start();
			//product,quantity,price
			AgentController s1 = cc.createNewAgent("s1","agents.Shop", new Object[]{"pc",5,140});
			/*AgentController s2 = cc.createNewAgent("s2","agents.Shop", new Object[]{"pc",15,200});
			AgentController s3 = cc.createNewAgent("s3","agents.Shop", new Object[]{"pc",5,130});
			AgentController s4 = cc.createNewAgent("s4","agents.Shop", new Object[]{"pc",15,120});
			AgentController s5 = cc.createNewAgent("s5","agents.Shop", new Object[]{"pc",10,150});
			AgentController s6 = cc.createNewAgent("s6","agents.Shop", new Object[]{"pc",30,120});
			AgentController s7 = cc.createNewAgent("s7","agents.Shop", new Object[]{"pc",15,130});
			AgentController s8 = cc.createNewAgent("s8","agents.Shop", new Object[]{"pc",40,140});
			AgentController s9 = cc.createNewAgent("s9","agents.Shop", new Object[]{"pc",15,200});
			AgentController s10 = cc.createNewAgent("s10","agents.Shop", new Object[]{"pc",5,130});
			AgentController s11 = cc.createNewAgent("s11","agents.Shop", new Object[]{"pc",15,120});
			AgentController s12 = cc.createNewAgent("s12","agents.Shop", new Object[]{"pc",10,150});
			AgentController s13 = cc.createNewAgent("s13","agents.Shop", new Object[]{"pc",30,120});
			AgentController s14 = cc.createNewAgent("s14","agents.Shop", new Object[]{"pc",15,130});*/
			
			//product,quantity,money,valuegiven,algorithm(0-normal e 1-nash)
			AgentController c1 = cc.createNewAgent("c1","agents.Client", new Object[]{"pc",5,10000,150,1});
			AgentController c2 = cc.createNewAgent("c2","agents.Client", new Object[]{"pc",15,10000,150,2});
			AgentController c3 = cc.createNewAgent("c3","agents.Client", new Object[]{"pc",5,10000,50,0});
			/*AgentController c4 = cc.createNewAgent("c4","agents.Client", new Object[]{"pc",15,10000,150,1});
			AgentController c5 = cc.createNewAgent("c5","agents.Client", new Object[]{"pc",5,10000,150,1});
			AgentController c6 = cc.createNewAgent("c6","agents.Client", new Object[]{"pc",15,10000,145,2});
			AgentController c7 = cc.createNewAgent("c7","agents.Client", new Object[]{"pc",5,10000,155,2});
			AgentController c8 = cc.createNewAgent("c8","agents.Client", new Object[]{"pc",15,10000,150,2});
			AgentController c9 = cc.createNewAgent("c9","agents.Client", new Object[]{"pc",5,10000,130,2});
			AgentController c10 = cc.createNewAgent("c10","agents.Client", new Object[]{"pc",15,10000,150,2});*/
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s1.start();
			/*s2.start();
			s3.start();
			s4.start();
			s5.start();
			s6.start();
			s7.start();
			s8.start();
			s9.start();
			s10.start();
			s11.start();
			s12.start();
			s13.start();
			s14.start();*/
			
			c1.start();
			c2.start();
			c3.start();
			/*c4.start();
			c4.start();
			c5.start();
			c6.start();
			c7.start();
			c8.start();
			c9.start();
			c10.start();*/
			
			//s1.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
