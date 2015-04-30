package structures;

import java.util.ArrayList;

public class CICStructure {
	
	//clients
	private ArrayList<String> clients;
	private ArrayList<String> shops;
	
	public CICStructure()
	{
		clients=new ArrayList<String>();
		shops=new ArrayList<String>();
	}
	
	//clients functions
	public void addClient(String name)
	{
		clients.add(name);
	}
	
	public void removeClient(String name)
	{
		clients.remove(name);
	}

	public ArrayList<String> getClients() {
		return clients;
	}
	
	//shop functions
	public void addShop(String name)
	{
		shops.add(name);
	}
		
	public void removeShop(String name)
	{
		shops.remove(name);
	}

	public ArrayList<String> getShops() {
		return shops;
	}
	
	
}
