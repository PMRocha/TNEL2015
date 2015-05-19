package structures;

import jade.core.AID;

import java.util.ArrayList;

public class RegisteredPeople {
	
	//clients
	private ArrayList<AID> clients;
	private ArrayList<AID> shops;
	
	public RegisteredPeople()
	{
		clients=new ArrayList<AID>();
		shops=new ArrayList<AID>();
	}
	
	//clients functions
	public void addClient(AID name)
	{
		clients.add(name);
	}
	
	public void removeClient(AID name)
	{
		clients.remove(name);
	}

	public ArrayList<AID> getClients() {
		return clients;
	}
	
	//shop functions
	public void addShop(AID name)
	{
		shops.add(name);
	}
		
	public void removeShop(AID name)
	{
		System.out.println("quantity:"+shops.size());
		shops.remove(name);
		System.out.println("quantity:"+shops.size());
	}

	public ArrayList<AID> getShops() {
		return shops;
	}
	
	
}
