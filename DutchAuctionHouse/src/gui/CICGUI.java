package gui;
 
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class CICGUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
    	
    	Parent root = FXMLLoader.load(getClass().getResource("CICGUI.fxml"));
        
        Scene scene = new Scene(root, 800, 800);
    
        primaryStage.setTitle("CIC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}