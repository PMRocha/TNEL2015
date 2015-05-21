package structures;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;

public class AuctionsList {

	// <Product,<Quant,[Seller]>>
	private HashMap<String, HashMap<Integer, ArrayList<AID>>> auctions;

	public HashMap<String, HashMap<Integer, ArrayList<AID>>> getAuctions() {
		return auctions;
	}

	public AuctionsList() {
		auctions = new HashMap<String, HashMap<Integer, ArrayList<AID>>>();
	}

	public void addAuction(String product, int quantity, AID seller) {
		HashMap<Integer, ArrayList<AID>> auction = auctions.get(product);

		if (auction==null) {
			ArrayList<AID> sellers = new ArrayList<AID>();
			sellers.add(seller);
			auction = new HashMap<Integer, ArrayList<AID>>();
			auction.put(quantity, sellers);
			auctions.put(product, auction);
		} else {
			if (auction.get(quantity) == null) {
				ArrayList<AID> sellers = new ArrayList<AID>();
				sellers.add(seller);
				auction.put(quantity, sellers);
				auctions.put(product, auction);
			}

			else {
				ArrayList<AID> sellers = new ArrayList<AID>();
				sellers = auction.get(quantity);
				sellers.add(seller);
				auction.put(quantity, sellers);
				auctions.put(product, auction);
			}
		}
	}

	public void removeAuction(String product, AID seller) {

		HashMap<Integer, ArrayList<AID>> aux=auctions.get(product);

		for (int key : aux.keySet()) {
		    if(aux.get(key).contains(seller))
		    {
		    	ArrayList<AID> help=aux.get(key);
		    	help.remove(seller);
		    	if(help.size()>0)
		    	aux.put(key,help);
		    	else
		    	aux.remove(key);
		    	break;
		    }
		}
		
	}
	
	public HashMap<Integer, ArrayList<AID>> getAuctionsOfProduct(String product) {
		return auctions.get(product);
		
	}
}
