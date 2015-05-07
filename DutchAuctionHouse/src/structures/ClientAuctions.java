package structures;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ClientAuctions {

	// quantidade ,vendedores
	private HashMap<Integer, ArrayList<AID>> existingAuctions;
	private HashMap<Integer, ArrayList<AID>> participatingAuctions;
	

	public ClientAuctions() {

		existingAuctions = new HashMap<Integer, ArrayList<AID>>();
		participatingAuctions = new HashMap<Integer, ArrayList<AID>>();
	}

	public void parseStringAuction(String content) {
		existingAuctions = new HashMap<Integer, ArrayList<AID>>();
		content = content.substring(1, content.length()-2);
		
		String[] help;
		ArrayList<AID> sellers = new ArrayList<AID>();

		String[] processing = content.split("=");

		for (int i = 0; i < processing.length; i += 2) {

			help = processing[i + 1].split(",");

			for (int j = 0; j < help.length; j++) {
				sellers.add(new AID(help[j], true));
			}

			existingAuctions.put(Integer.parseInt(processing[i]), sellers);
			sellers = new ArrayList<AID>();
		}
		
	}
	
	public ArrayList<AID> getAuctionsWithoutBuyer()
	{
		ArrayList<AID>existingSellers=new ArrayList<AID>();
		ArrayList<AID>participatingSellers=new ArrayList<AID>();;
		
		for(Entry<Integer, ArrayList<AID>> auction : existingAuctions.entrySet()) {
			existingSellers.addAll(auction.getValue());
		}
		
		for(Entry<Integer, ArrayList<AID>> auction : participatingAuctions.entrySet()) {
			participatingSellers.addAll(auction.getValue());
		}
		existingSellers.removeAll(participatingSellers);
		
		return existingSellers;
		
	}

}
