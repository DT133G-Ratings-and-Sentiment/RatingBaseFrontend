package com.dt002g.reviewapplication.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.dt002g.reviewapplication.frontend.App;
import com.dt002g.reviewapplication.frontend.Controller;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendAPIService;

/**
 * JavaFX App
 */
public class App extends Application {

	 private static Scene scene;
	    private Controller controller;

	    @Override
	    public void start(Stage stage) throws IOException {
	    	
			FXMLLoader loader = new FXMLLoader(getClass().getResource("search.fxml"));
			
			GridPane grid = loader.load();
			
			controller = loader.getController();
			
	        Scene scene = new Scene(grid, 600, 400);
	        stage.setScene(scene);
	        stage.setTitle("Review Search");
	        stage.setAlwaysOnTop(false);
	        stage.setResizable(false);
	        readReviews();
	        
	        stage.show();
	    }

	    static void setRoot(String fxml) throws IOException {
	        scene.setRoot(loadFXML(fxml));
	    }

	    private static Parent loadFXML(String fxml) throws IOException {
	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
	        return fxmlLoader.load();
	    }

	    public static void main(String[] args) {
	        launch();
	    }
	    
	    public void readReviews() {
	    	ReviewBackendAPIService.getInstance().getReviews(controller);
	    }


}