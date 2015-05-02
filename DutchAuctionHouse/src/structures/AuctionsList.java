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

	public HashMap<Integer, ArrayList<AID>> getAuctionsOfProduct(String product) {
		return auctions.get(product);
		
	}
}
