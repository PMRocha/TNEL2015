package gui;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import agents.CIC;

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
			if(agent.getAuctions().getAuctions()!=null)	
				auctionListViewData.addAll(getCICAuctions(agent.getAuctions().getAuctions()));
		}
		
	}
	
	public static void setAgent(CIC agen) {
		agent = agen;
	}

	private String[] getCICAuctions(HashMap<String, HashMap<Integer, ArrayList<AID>>> auctions) {

		ArrayList<String> result = null;
		
		for (HashMap<Integer, ArrayList<AID>> temp : auctions.values()) {
		    for(ArrayList<AID> list : temp.values()){
		    	for(int i=0;i<list.size();i++){
		    		result.add(list.get(i).toString());
		    	}
		    }
		}
		
		return (String[]) result.toArray();
	}

	@FXML
	private void initialize() {
		
		clientAgentListView.getSelectionModel().selectedItemProperty().removeListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
					clientAgentListViewData.remove(oldValue);
					clientAgentListViewData.add(newValue);
					clientAgentListView.setItems(clientAgentListViewData);
			}
		});
		
		shopAgentListView.getSelectionModel().selectedItemProperty().removeListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
					shopAgentListViewData.remove(oldValue);
					shopAgentListViewData.add(newValue);
					shopAgentListView.setItems(shopAgentListViewData);
			}
		});
		
		auctionListView.getSelectionModel().selectedItemProperty().removeListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
					auctionListViewData.remove(oldValue);
					auctionListViewData.add(newValue);
					auctionListView.setItems(auctionListViewData);
			}
		});
		
		if(agent!=null){
			clientAgentListView.setItems(clientAgentListViewData);
			shopAgentListView.setItems(shopAgentListViewData);
			auctionListView.setItems(auctionListViewData);}
	}
	
}