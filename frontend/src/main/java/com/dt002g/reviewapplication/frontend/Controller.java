package com.dt002g.reviewapplication.frontend;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.dt002g.reviewapplication.frontend.service.GetRatingStatsCallBack;
import com.dt002g.reviewapplication.frontend.service.GetReviewsCallBack;
import com.dt002g.reviewapplication.frontend.service.RatingBackendEntity;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendAPIService;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendEntity;
import com.dt002g.reviewapplication.frontend.util.SearchHandler;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Controller  implements Initializable, GetReviewsCallBack, GetRatingStatsCallBack {
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
	@FXML BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
	@FXML final HBox barChartBox = new HBox();
	@FXML final Pane barChartPane = new Pane();
	@FXML final ToggleGroup group = new ToggleGroup();
	
	ScrollBar reviewTableScrollBar;
	ChangeListener<Number> scrollListener;
	private String selection = "Get all";

	private final ObservableList<Review> reviewsInTable = FXCollections.observableArrayList();
	private ArrayList<Review> reviews = new ArrayList<>();
	private ArrayList<RatingStats> ratingsByComment = new ArrayList<>();
	
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
    
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		for(int i = 0; i < grid.getChildren().size(); i++) {
			try {
				if(grid.getChildren().get(i).getId().equals("barChart")){
					barChart = (BarChart<String, Number>) grid.getChildren().get(i);
					break;
				}
			}
			catch(NullPointerException e) {
				// continue to next
			}
					
		}
		barChart.setAnimated(false);
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
				reviewsInTable.clear();
				reviews.clear();
				if(reviewTableScrollBar != null && scrollListener != null) {
					reviewTableScrollBar.valueProperty().removeListener(scrollListener);
				}
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
		System.out.println("Selection: " + selection);
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
	            	if(selection.equals("Get all")) {
	            		ReviewBackendAPIService.getInstance().getTopReviewsLargerThanId(this, fetchId);
	            	}
	            	else if(selection.equals("Get by strings")) {
	            		SearchHandler.getInstance().getByStrings(this, this, searchField.getText(), fetchId);
	            	}
	            	else if(selection.equals("Get by rating")) {
	            		int rating = Integer.parseInt(searchField.getText());
	    				SearchHandler.getInstance().getByRating(this, rating, fetchId);
	            		
	            	}else if(selection.equals("Get by rating and string")){
	            		SearchHandler.getInstance().getByRatingAndStrings(this, 1, searchField.getText(), 0L);
	            	}
	            }
	        };
	        Platform.runLater(() -> {reviewTableScrollBar.valueProperty().addListener(scrollListener);});
		});
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
	
	@Override
	public void processGetMapCallBack(List<RatingBackendEntity> response) {
		
		ArrayList<RatingStats> tempRatings = new ArrayList<>();
		for(RatingBackendEntity rev: response) {
			ratingsByComment.add(new RatingStats(rev.getRating(), rev.getAmount()));
			tempRatings.add(new RatingStats(rev.getRating(), rev.getAmount()));
		}
		setBarChart(tempRatings);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setBarChart(ArrayList<RatingStats> ratingsByComment) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				try{
					barChart.getData().remove(0);
				}
				catch(IndexOutOfBoundsException e) {
					// do nothing
				}
				int rating1 = 0, rating2 = 0, rating3 = 0, rating4 = 0, rating5 = 0;
				
				
				
				System.out.println(ratingsByComment.size());
				xAxis.setLabel("Rating");
				yAxis.setLabel("Number of reviews");

				
				XYChart.Series series1 = new XYChart.Series<>();
				for(RatingStats rating : ratingsByComment) {
					if(rating.getRating() == 1) {
						rating1 = rating.getAmount();
					}
					else if(rating.getRating() == 2) {
						rating2 = rating.getAmount();
					}
					else if(rating.getRating() == 3) {
						rating3 = rating.getAmount();
					}
					else if(rating.getRating() == 4) {
						rating4 = rating.getAmount();
					}
					else if(rating.getRating() == 5) {
						rating5 = rating.getAmount();
					}
				}
				series1.getData().add(new XYChart.Data<>("1", rating1));
				series1.getData().add(new XYChart.Data<>("2", rating2));
				series1.getData().add(new XYChart.Data<>("3", rating3));
				series1.getData().add(new XYChart.Data<>("4", rating4));
				series1.getData().add(new XYChart.Data<>("5", rating5));
				series1.setName("Ratings");
		
				barChart.getData().add(series1);
				barChart.getData().get(0).getData().forEach((item) ->{
					item.getNode().setStyle("-fx-background-color: blue");
				});
			}
		});
	}

	@FXML protected void searchAction(ActionEvent event) {
		if(selection.equals("Get all")) {
			SearchHandler.getInstance().getTopReviewsLargerThanId(this, 0L);
			return;
		}
		
		if(searchField.getText().isEmpty() || searchField == null) {
			Alert alert = new Alert(AlertType.WARNING, "Empty search string");
			alert.show();
			return;
		}
		
		if(selection.equals("Get by strings")){	
			SearchHandler.getInstance().getByStrings(this,this, searchField.getText(), 0L);
		}
		else if(selection.equals("Get by rating")) {
			System.out.println("Get by rating");
			try {
				int rating = Integer.parseInt(searchField.getText());
				SearchHandler.getInstance().getByRating(this, rating, 0);
			}
			catch(NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING, "Could not parse integer");
				alert.show();
			}
		}
		else if(selection.equals("Get by rating and string")){
			System.out.println("Get by rating and string");
			try {
				// Här under ska den hämta från en annan sökruta
				//int rating = Integer.parseInt(INTEGERSÖKRUTA.getText());
				int temp = 1;
				//SearchHandler.getInstance().getByRatingAndString(this, temp, searchField.getText());
				SearchHandler.getInstance().getByRatingAndStrings(this, temp, searchField.getText(), 0L);
			}
			catch(NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING, "Could not parse integer");
				alert.show();
			}
		}
    }
}
