package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CICGUIController {
	@FXML
	private ListView<String> clientAgentListView;
	private ListView<String> shopAgentListView;
	private ListView<String> auctionListView;

	private ObservableList<String> clientAgentListViewData = FXCollections.observableArrayList();

	public CICGUIController() {
		
		clientAgentListViewData.add("Lydia");
		clientAgentListViewData.add("Anna");
		clientAgentListViewData.add("Stefan");
		clientAgentListViewData.add("Martin");
	}
	
	@FXML
	private void initialize() {
		
		// Init ListView and listen for selection changes
		clientAgentListView.setItems(clientAgentListViewData);
		
		
		/*listView.getSelectionModel().selectedItemProperty().removeListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
					listView.setItems(listViewData);
			}
		});*/
	}
}