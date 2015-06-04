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
		content = content.substring(1, content.length() - 1);

		String[] help;
		ArrayList<AID> sellers = new ArrayList<AID>();

		String[] processing = content.split("],");
		String[] separation;

		if (processing.length > 0) {
			if (!processing[0].equals("")) {

				for (int i = 0; i < processing.length; i++) {
					separation = processing[i].split("=");
					help = separation[1].substring(1).replaceAll("]", "")
							.split(",");

					for (int j = 0; j < help.length; j += 2) {
						// extracts localname
						String localname = help[j].substring(
								help[j].indexOf(":name") + 6,
								help[j].indexOf("@"));
						sellers.add(new AID(localname, AID.ISLOCALNAME));
					}

					existingAuctions.put(
							Integer.parseInt(separation[0].trim()), sellers);
					sellers = new ArrayList<AID>();
				}
			}
		}

	}

	public ArrayList<AID> getAuctionsWithoutBuyer() {
		ArrayList<AID> existingSellers = new ArrayList<AID>();
		ArrayList<AID> participatingSellers = new ArrayList<AID>();

		for (Entry<Integer, ArrayList<AID>> auction : participatingAuctions
				.entrySet()) {
			participatingSellers.addAll(auction.getValue());
		}
		for (Entry<Integer, ArrayList<AID>> auction : existingAuctions
				.entrySet()) {
			existingSellers.addAll(auction.getValue());
			participatingAuctions.put(auction.getKey(), auction.getValue());
		}
		existingSellers.removeAll(participatingSellers);

		// corrects number of auctions
		participatingAuctions = existingAuctions;

		return existingSellers;

	}

}
