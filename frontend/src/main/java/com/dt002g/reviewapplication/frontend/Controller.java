package com.dt002g.reviewapplication.frontend;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.dt002g.reviewapplication.frontend.service.GetReviewsCallBack;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendAPIService;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendEntity;
import com.dt002g.reviewapplication.frontend.util.SearchHandler;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	@FXML private TextField searchField;
	@FXML final CategoryAxis xAxis = new CategoryAxis();
	@FXML final NumberAxis yAxis = new NumberAxis();
	@FXML final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
	@FXML final HBox barChartBox = new HBox();
	@FXML final Pane barChartPane = new Pane();
	@FXML final ToggleGroup group = new ToggleGroup();
	
	ScrollBar reviewTableScrollBar;
	ChangeListener<Number> scrollListener;
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
    private TableColumn<Review, Long> idColumn;
    
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

		//  Radio button code

		getAllRadioButton.setToggleGroup(group);
		getByRatingRadioButton.setToggleGroup(group);
		getByStringsRadioButton.setToggleGroup(group);
		getByRatingAndStringsRadioButton.setToggleGroup(group);
		getAllRadioButton.setSelected(true);

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
				if(group.getSelectedToggle().getUserData() != null) {
					selection = group.getSelectedToggle().getUserData().toString();
				}
				if(selection.equals("Get all")) {
					searchField.setVisible(false);
					searchField.setManaged(false);
				}
				else {
					searchField.setVisible(true);
					searchField.setManaged(true);
				}
			}
		});
	}
	
	public void setReviews(List<Review> pReviews, int page) {
		long lastId;
		if(reviewsInTable.size() > 0) {
			lastId = reviewsInTable.get(reviewsInTable.size()-1).getId();
		}
		reviewsInTable.addAll(pReviews);
		long fetchId =  pReviews.get(pReviews.size()-1).getId();
		/*for(int i = (page*25); i < ((page*25) + 25); i++) {
			reviewsInTable.add(pReviews.get(i));
		}*/
		Platform.runLater(() -> {
			if(reviewTableScrollBar != null && scrollListener != null) {
				reviewTableScrollBar.valueProperty().removeListener(scrollListener);
			}
			reviewTableScrollBar = (ScrollBar) referenceTable.lookup(".scroll-bar:vertical");
			scrollListener = (observable, oldValue, newValue) -> {
	            if ((Double) newValue == 1.0) {
	            	ReviewBackendAPIService.getInstance().getTopReviewsLargerThanId(this, fetchId);
	            	/*Review temp = reviewsInTable.get(reviewsInTable.size()-15);
	                if(reviewsInTable.size() < reviews.size()) {
	                	setReviews(reviews, reviewsInTable.size()/25);
	                	referenceTable.scrollTo(temp);
	                }*/
	            }
	        };
	        Platform.runLater(() -> {reviewTableScrollBar.valueProperty().addListener(scrollListener);});
		});
		/*Platform.runLater(() -> {
        ScrollBar tvScrollBar = (ScrollBar) referenceTable.lookup(".scroll-bar:vertical");
        tvScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((Double) newValue == 1.0) {
            	ReviewBackendAPIService.getInstance().getTopReviewsLargerThanId(this, lastId);
            	Review temp = reviewsInTable.get(reviewsInTable.size()-15);
                if(reviewsInTable.size() < reviews.size()) {
                	setReviews(reviews, reviewsInTable.size()/25);
                	referenceTable.scrollTo(temp);
                }
            }
        });
		});*/
		Platform.runLater(() ->{
			if((reviewsInTable.size() - pReviews.size())> 0) {
				Review temp = reviewsInTable.get(reviewsInTable.size() - pReviews.size());
		        referenceTable.scrollTo(temp);
			}
		});
	}

	@Override
	public void processGetReviewsCallBack(List<ReviewBackendEntity> response) {
		ArrayList<Review> tempReviews = new ArrayList<>();
		for(ReviewBackendEntity rev: response) {
			reviews.add(new Review(rev));
			tempReviews.add(new Review(rev));
		}
		setReviews(tempReviews, 0);
		
	}

	@FXML protected void searchAction(ActionEvent event) {
		if(selection.equals("Get all")) {
			SearchHandler.getInstance().getAll(this);
			return;
		}
		
		if(searchField.getText().isEmpty() || searchField == null) {
			Alert alert = new Alert(AlertType.WARNING, "Empty search string");
			alert.show();
			return;
		}
		
		if(selection.equals("Get by strings")){	
			SearchHandler.getInstance().getByStrings(this, searchField.getText());
		}
		else if(selection.equals("Get by rating")) {
			try {
				int rating = Integer.parseInt(searchField.getText());
				SearchHandler.getInstance().getByRating(this, rating);
			}
			catch(NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING, "Could not parse integer");
				alert.show();
			}
		}
		else if(selection.equals("Get by rating and string")){
			try {
				// Här under ska den hämta från en annan sökruta
				//int rating = Integer.parseInt(INTEGERSÖKRUTA.getText());
				int temp = 1;
				SearchHandler.getInstance().getByRatingAndString(this, temp, searchField.getText());
			}
			catch(NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING, "Could not parse integer");
				alert.show();
			}
		}
		else if(selection.equals("Get by strings")){
			SearchHandler.getInstance().getByStrings(this, searchField.getText());
		}
    }
}
