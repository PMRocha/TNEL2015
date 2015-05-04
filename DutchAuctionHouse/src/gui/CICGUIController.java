package gui;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;

import structures.AuctionsList;
import agents.CIC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.util.Duration;

public class CICGUIController {
	
	private static CIC agent;

	@FXML
	private ListView<String> clientAgentListView;
	private ListView<String> shopAgentListView;
	private ListView<String> auctionListView;

	private ObservableList<String> clientAgentListViewData = FXCollections.observableArrayList();
	private ObservableList<String> shopAgentListViewData = FXCollections.observableArrayList();
	private ObservableList<String> auctionListViewData = FXCollections.observableArrayList();

	public CICGUIController() {
		
		if(agent!=null){
			clientAgentListViewData.addAll((String[]) agent.getRegister().getClients().toArray());
			shopAgentListViewData.addAll((String[]) agent.getRegister().getShops().toArray());
			auctionListViewData.addAll(getCICAuctions(agent.getAuctions().getAuctions()));
		}
		
	}
	
	public static void setAgent(CIC agen) {
		agent = agen;
	}

	private String getCICAuctions(HashMap<String, HashMap<Integer, ArrayList<AID>>> auctions) {
		
		HashMap<Integer, ArrayList<AID>> temp;

		return null;
	}

	@FXML
	private void initialize() {
		if(agent!=null){
			clientAgentListView.setItems(clientAgentListViewData);
			shopAgentListView.setItems(shopAgentListViewData);
			auctionListView.setItems(auctionListViewData);}
	}
	
}