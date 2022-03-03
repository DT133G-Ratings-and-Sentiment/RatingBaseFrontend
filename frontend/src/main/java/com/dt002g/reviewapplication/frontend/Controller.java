package com.dt002g.reviewapplication.frontend;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.dt002g.reviewapplication.frontend.service.GetReviewsCallBack;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendEntity;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;

public class Controller  implements Initializable, GetReviewsCallBack {
	@FXML private RadioButton getAllRadioButton = new RadioButton();
	@FXML private RadioButton getByStringRadioButton = new RadioButton();
	@FXML private RadioButton getByRatingRadioButton = new RadioButton();
	@FXML private RadioButton getByStringsRadioButton = new RadioButton();
	@FXML private RadioButton getByRatingAndStringsRadioButton = new RadioButton();
	@FXML private HBox radioHBox = new HBox();
	
	@FXML private GridPane grid = new GridPane();
	@FXML private Text actiontarget;
	@FXML private TextField searchField;
	@FXML final CategoryAxis xAxis = new CategoryAxis();
	@FXML final NumberAxis yAxis = new NumberAxis();
	@FXML final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
	@FXML final HBox barChartBox = new HBox();
	@FXML final Pane barChartPane = new Pane();
	@FXML final ToggleGroup group = new ToggleGroup();
	private String selection = "Get all";

	private final ObservableList<Review> reviewsInTable = FXCollections.observableArrayList();
	private ArrayList<Review> reviews = new ArrayList<>();
	
	private final HashMap<Integer, Review> referenceMap = new HashMap<>();

	public HashMap<Integer, Review> getReferenceMap(){
		return referenceMap;
	}
	

	
	
    @FXML
    private TableView<Review> referenceTable;
    
    @FXML
    private TableColumn<Review, Integer> idColumn;
    
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    
    @FXML
    private TableColumn<Review, String> freeTextColumn;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		referenceTable.setItems(reviewsInTable);
		idColumn.setCellValueFactory(rowData -> rowData.getValue().idProperty());
		ratingColumn.setCellValueFactory(rowData -> rowData.getValue().ratingProperty());
		freeTextColumn.setCellValueFactory(rowData -> rowData.getValue().freeTextProperty());
		searchField.setOnKeyPressed(this::setActionTarget);

		
		//  Radio button code
		getAllRadioButton.setToggleGroup(group);
		getByStringRadioButton.setToggleGroup(group);
		getByRatingRadioButton.setToggleGroup(group);
		getByStringsRadioButton.setToggleGroup(group);
		getByRatingAndStringsRadioButton.setToggleGroup(group);

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
				if(group.getSelectedToggle().getUserData() != null) {
					selection = group.getSelectedToggle().getUserData().toString();
				}
				if(selection.equals("Get all")) {
					System.out.println("Get all");
					searchField.setVisible(false);
					searchField.setManaged(false);
				}
				else if(selection.equals("Get by string")) {
					System.out.println("Get by string");
					searchField.setVisible(true);
					searchField.setManaged(true);
				}
				else if(selection.equals("Get by rating")){
					// add field for integer
					System.out.println("Get by rating");
				}
				else if(selection.equals("Get by strings")) {
					// add 10 searchfields
					System.out.println("Get by strings");
				}
				else if(selection.equals("Get by rating and string")) {
					// add two fields, one for integer and one for string
					System.out.println("Get by rating and sttring");
				}
						
			}
		});
	
	}
	
	public void setReviews(List<Review> pReviews, int page) {
		for(int i = (page*25); i < ((page*25) + 25); i++) {
			reviewsInTable.add(pReviews.get(i));
		}
		Platform.runLater(() -> {
        ScrollBar tvScrollBar = (ScrollBar) referenceTable.lookup(".scroll-bar:vertical");
        tvScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((Double) newValue == 1.0) {
                Review temp = reviewsInTable.get(reviewsInTable.size()-15);
                if(reviewsInTable.size() < reviews.size()) {
                	setReviews(reviews, reviewsInTable.size()/25);
                	referenceTable.scrollTo(temp);
                }
            }
        });
		});
	}

	@Override
	public void processGetReviewsCallBack(List<ReviewBackendEntity> response) {
		reviews.clear();
		reviewsInTable.clear();
		reviews = new ArrayList<>();
		for(ReviewBackendEntity rev: response) {
			reviews.add(new Review(rev));
		}
		setReviews(reviews, 0);
		
	}
	
	
	
	  @FXML protected void temporarySearch(ActionEvent event) {
		if(!grid.getChildren().get(0).isVisible()) {
			grid.getChildren().get(0).setVisible(true);
			grid.getChildren().get(0).setManaged(true);
		}
		else {
			grid.getChildren().get(0).setVisible(false);
			grid.getChildren().get(0).setManaged(false);
		}
		
			System.out.println(group.getSelectedToggle().toString());
		
	    
	    	/*reviews.addAll(new Review(1, 5, actiontarget.getText()),
					new Review(2, 2, "This sucks"),
					new Review(3, 4, "I like how it works, but expensive"));*/
	    }
	    
	  @FXML protected void onRadioButtonChange(Event event) {
		  System.out.println("HERE");
	  }
	  
	    @FXML protected void setActionTarget(KeyEvent keyEvent) {
	    	System.out.println(keyEvent.getCharacter());
	    	actiontarget.setText(searchField.getText()+keyEvent.getText());
	    }
	    

}
