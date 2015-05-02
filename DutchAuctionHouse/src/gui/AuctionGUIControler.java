package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class AuctionGUIControler {
	@FXML
	private ListView<String> agentView;
	private ListView<String> messageView;
	private ListView<String> bidHistoryView;

	private ObservableList<String> listViewData = FXCollections.observableArrayList();

	public AuctionGUIControler() {
		
		listViewData.add("Lydia");
		listViewData.add("Anna");
		listViewData.add("Stefan");
		listViewData.add("Martin");
	}
	
	@FXML
	private void initialize() {
		
		// Init ListView and listen for selection changes
		agentView.setItems(listViewData);
		
		
		/*listView.getSelectionModel().selectedItemProperty().removeListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
					listView.setItems(listViewData);
			}
		});*/
	}
}