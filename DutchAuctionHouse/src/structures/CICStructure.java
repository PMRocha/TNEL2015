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
	
	//clients commands
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
	
	
}
