package structures;

import java.util.ArrayList;
import java.util.HashMap;

public class AuctionsList {

	//<Product,<Quant,[Seller]>>
	private HashMap<String,HashMap<Integer,ArrayList<String>>> auctions;
	
	public AuctionsList() {
		auctions= new HashMap<String,HashMap<Integer,ArrayList<String>>>();
	}

}
