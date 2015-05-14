package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class AuctionGUIController {
	@FXML
	private ListView<String> agentView;
	private ListView<String> messageView;
	private ListView<String> bidHistoryView;

	private ObservableList<String> agentViewData = FXCollections.observableArrayList();
	private ObservableList<String> messageViewData = FXCollections.observableArrayList();
	private ObservableList<String> bidHistoryViewData = FXCollections.observableArrayList();
	
	@FXML
	private void initialize() {		
		
		agentView.getSelectionModel().selectedItemProperty().removeListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
					agentViewData.remove(oldValue);
					agentViewData.add(newValue);
					agentView.setItems(agentViewData);
			}
		});
		
		messageView.getSelectionModel().selectedItemProperty().removeListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				messageViewData.remove(oldValue);
				messageViewData.add(newValue);
				messageView.setItems(messageViewData);
			}
		});
		
		bidHistoryView.getSelectionModel().selectedItemProperty().removeListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable,String oldValue, String newValue) {
				bidHistoryViewData.remove(oldValue);
				bidHistoryViewData.add(newValue);
				bidHistoryView.setItems(bidHistoryViewData);
			}
		});
	}
}