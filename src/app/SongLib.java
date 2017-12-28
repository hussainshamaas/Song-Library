package app;
	
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.ListController;
/**
 * @author Shamaas Hussain
 *
 */

public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) 
	throws Exception {
		  ListController listController = new ListController("List.fxml");
	      primaryStage.setScene(listController.getScene());
	      primaryStage.sizeToScene();
	      primaryStage.setTitle("Song Library Project - Shamaas Hussain");
	      primaryStage.show(); 
	      primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

	          @Override
	          	public void handle(WindowEvent event) 
	          	{
	          		try {
						listController.saveItemsToFile();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	          	}
	  		});

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
