package com.dt002g.reviewapplication.frontend;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.dt002g.reviewapplication.frontend.service.GetNumberOfReviewsCallBack;
import com.dt002g.reviewapplication.frontend.service.GetRatingStatsCallBack;
import com.dt002g.reviewapplication.frontend.service.GetReviewsCallBack;
import com.dt002g.reviewapplication.frontend.service.RatingBackendEntity;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendAPIService;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendEntity;
import com.dt002g.reviewapplication.frontend.util.PieChartHolder;
import com.dt002g.reviewapplication.frontend.util.SearchHandler;
import com.dt002g.reviewapplication.frontend.util.Utility;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Controller  implements Initializable, GetReviewsCallBack, GetRatingStatsCallBack, GetNumberOfReviewsCallBack {
	@FXML private GridPane root;
	
	@FXML private HBox radioHBox = new HBox();
	@FXML private RadioButton getAllRadioButton = new RadioButton();
	@FXML private RadioButton getByStringRadioButton = new RadioButton();
	@FXML private RadioButton getByRatingRadioButton = new RadioButton();
	@FXML private RadioButton getByStringsRadioButton = new RadioButton();
	@FXML private RadioButton getByRatingAndStringsRadioButton = new RadioButton();
	@FXML private RadioButton getByStringsInclusiveRadioButton;
	@FXML final private ToggleGroup group = new ToggleGroup();
	
	@FXML private MenuButton ratingDropDown;
	@FXML private MenuItem rating1;
	@FXML private MenuItem rating2;
	@FXML private MenuItem rating3;
	@FXML private MenuItem rating4;
	@FXML private MenuItem rating5;
	
	@FXML private CheckBox keepChartCheckBox;

	@FXML private TabPane tabPane;
	@FXML private Tab tableDataTab;
	@FXML private Tab barChartTab;
	
	@FXML private Button searchButton;
	@FXML private TextField searchField;
	
	@FXML private BarChart<Number, Number> barChart;
    @FXML private CategoryAxis barChartYAxis;
    @FXML private NumberAxis barChartXAxis;
	@FXML final private HBox barChartBox = new HBox();
	@FXML final private Pane barChartPane = new Pane();
	
	
	private ScrollBar reviewTableScrollBar;
	private ChangeListener<Number> scrollListener;
	private String selection = "Get all";
	private final ObservableList<Review> reviewsInTable = FXCollections.observableArrayList();
	private ArrayList<Review> reviews = new ArrayList<>();
	private ArrayList<RatingStats> ratingsByComment = new ArrayList<>();
	private final HashMap<Integer, Review> revieweMap = new HashMap<>();

	public HashMap<Integer, Review> getReviewMap(){
		return revieweMap;
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

		root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				System.out.println(event.getCode());
				if(event.getCode().equals(KeyCode.ENTER)) {
					ActionEvent ae = new ActionEvent();
					searchAction(ae);
				}
			}
		});
		
		tableDataTab.setClosable(false);
		barChartTab.setClosable(false);
		tabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
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
		getByStringsInclusiveRadioButton.setToggleGroup(group);
		getAllRadioButton.setSelected(true);
		searchField.setVisible(false);
		searchField.setManaged(false);
		ratingDropDown.setVisible(false);
		ratingDropDown.setManaged(false);

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
					ratingDropDown.setVisible(false);
					ratingDropDown.setManaged(false);
				}
				else if(selection.equals("Get by rating")) {
					searchField.setVisible(false);
					searchField.setManaged(false);
					ratingDropDown.setVisible(true);
					ratingDropDown.setManaged(true);
					ratingDropDown.setText("Rating");
				}
				else if(selection.equals("Get by strings")){
					searchField.setVisible(true);
					searchField.setManaged(true);
					ratingDropDown.setVisible(false);
					ratingDropDown.setManaged(false);
				}
				else if(selection.equals("Get by rating and string")) {
					searchField.setVisible(true);
					searchField.setManaged(true);
					ratingDropDown.setVisible(true);
					ratingDropDown.setManaged(true);
					ratingDropDown.setText("Rating");
				}else if(selection.equals("Get by including words")) {
					searchField.setVisible(true);
					searchField.setManaged(true);
					ratingDropDown.setVisible(false);
					ratingDropDown.setManaged(false);
				}			
			}
		});
	}
	
	public void setReviews(List<Review> pReviews, int page) {
		if(pReviews.size() > 0) {
			long lastId;
			if(reviewsInTable.size() > 0) {
				lastId = reviewsInTable.get(reviewsInTable.size()-1).getId();
			}
			reviewsInTable.addAll(pReviews);
			long fetchId = pReviews.get(pReviews.size()-1).getId();
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
		            		int rating = Integer.parseInt(ratingDropDown.getText());
		    				SearchHandler.getInstance().getByRating(this, rating, fetchId);         		
		            	}else if(selection.equals("Get by rating and string")){
		            		int rating = Integer.parseInt(ratingDropDown.getText());
		            		SearchHandler.getInstance().getByRatingAndStrings(this, rating, searchField.getText(), fetchId);
		            	}else if(selection.equals("Get by including words")) {
		            		SearchHandler.getInstance().getByStringsInclusive(this, searchField.getText(), fetchId);
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
	public void processGetMapCallBack(List<RatingBackendEntity> response, String searchString) {
		System.out.println("processGetMapCallBack");
		ArrayList<RatingStats> tempRatings = new ArrayList<>();
		for(RatingBackendEntity rev: response) {
			ratingsByComment.add(new RatingStats(rev.getRating(), rev.getAmount()));
			tempRatings.add(new RatingStats(rev.getRating(), rev.getAmount()));
		}
		if(!selection.equals("Get by including words")) {
			setBarChart(tempRatings, searchString);
		}
		setPieChart(tempRatings, searchString);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setBarChart(ArrayList<RatingStats> ratingsByComment, String searchString) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String legend = searchString.substring(0, 1).toUpperCase() + searchString.substring(1);	
				barChartXAxis.setLabel("Number of Reviews");
				barChartYAxis.setLabel("Rating");
				int totalCount = 0;

				XYChart.Series series1 = new XYChart.Series<>();
				for(RatingStats rating : ratingsByComment) {
					totalCount += rating.getAmount();
					series1.getData().add(new XYChart.Data<>(String.valueOf(rating.getRating()), rating.getAmount()));
				}
			
				series1.setName(legend + ", Total count: " + totalCount);
		
				barChart.getData().add(series1);
				
				//  Ändra färger om vi vill sen
				/*barChart.getData().get(0).getData().forEach((item) ->{
					item.getNode().setStyle("-fx-background-color: blue");
				});
				*/
			}
		});
	}
	
	private void setPieChart(ArrayList<RatingStats> ratingsByComment, String searchString) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String legend = searchString.substring(0, 1).toUpperCase() + searchString.substring(1);	
				PieChartHolder pieChart = addNewTabWithPieChart(legend);
				int totalCount = 0;
				ArrayList<PieChart.Data> pieChartData = new ArrayList<>();
				
				for(RatingStats rating : ratingsByComment) {
					totalCount += rating.getAmount();
					pieChartData.add((new PieChart.Data(rating.getRating() + ": " + rating.getAmount(), rating.getAmount())));
				}
				ObservableList<PieChart.Data> pieChartDataObservable = FXCollections.observableArrayList(pieChartData); 
				pieChart.getPieChart().setData(pieChartDataObservable);
				pieChart.getPieChart().setTitle(legend + ", Total count: "+ totalCount);
				pieChart.getPieChart().setLegendSide(Side.LEFT);
				pieChart.getLabel().setTextFill(Color.BLACK);
				pieChart.getLabel().setStyle("-fx-font-size: 24;");
				
		       for (final Data data : pieChart.getPieChart().getData()){
		    	   final double percent = Utility.round((data.getPieValue() / totalCount)*100, 2);
		           Node node = data.getNode();
		           final String showOnHoverString = "Rating " + data.getName().substring(0, 1) + ": " + String.valueOf(percent) + "%";

		           node.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>(){
						@Override
						public void handle(MouseEvent e){
							pieChart.getLabel().setTranslateX(e.getSceneX());
							pieChart.getLabel().setTranslateY(e.getSceneY()- root.getHeight());
							pieChart.getLabel().setText(showOnHoverString);
							pieChart.getLabel().setVisible(true);
						}
		            });
		            node.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>(){
		                @Override
		                public void handle(MouseEvent e)
		                {
		                	pieChart.getLabel().setVisible(false);
		                }
		            });
		        }
			}
		});
	}
		
	private PieChartHolder addNewTabWithPieChart(String searchString) {
		FlowPane pane = new FlowPane();
		pane.prefWidth(tabPane.getWidth());
		pane.prefHeight(tabPane.getHeight());
		pane.setMinWidth(tabPane.getWidth());
		pane.setMaxWidth(tabPane.getWidth());
		pane.setMaxHeight(PieChart.USE_COMPUTED_SIZE);
		pane.setMinHeight(PieChart.USE_COMPUTED_SIZE);
		PieChart pieChart = new PieChart();
		pieChart.prefWidth(tabPane.getWidth());
		pieChart.prefHeight(tabPane.getHeight());
		
		Label label = new Label("");
		pane.getChildren().addAll(pieChart, label);
		Tab tab = new Tab(searchString + " Pie Chart", pane);
		tab.setClosable(true);
		pieChart.setMinWidth(tabPane.getWidth());
		pieChart.setMaxWidth(tabPane.getWidth());
		pieChart.setMaxHeight(tabPane.getHeight()-100);
		pieChart.setMinHeight(tabPane.getHeight()-100);
		tabPane.getTabs().add(tab);
		PieChartHolder pieChartHolder = new PieChartHolder(pieChart, label);
		return pieChartHolder;
	
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setBarChartByStringLabel(Map<String, Integer> results) {
		System.out.println("setBarChartByStringLabel");
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				String legend = "";
				barChartXAxis.setLabel("Number of Reviews");
				barChartYAxis.setLabel("Search words");

				XYChart.Series series1 = new XYChart.Series<>();

				for (Map.Entry<String, Integer> entry : results.entrySet()) {
					legend += entry.getKey() + " ";
					series1.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
				}

				series1.setName(legend);
				barChart.getData().add(series1);
				
				//  Ändra färger om vi vill sen
				/*barChart.getData().get(0).getData().forEach((item) ->{
					item.getNode().setStyle("-fx-background-color: blue");
				});
				*/
			}
		});
	}
	
	@FXML protected void searchAction(ActionEvent event) {
		reviewsInTable.clear();
		reviews.clear();
		if(!keepChartCheckBox.isSelected()) {
			clearChart();
		}
		if(selection.equals("Get all")) {
			SearchHandler.getInstance().getTopReviewsLargerThanId(this, 0L);		
			return;
		}
		
		if(searchField.getText().isEmpty() && !selection.equals("Get by rating")) {
			Alert alert = new Alert(AlertType.WARNING, "Empty search string");
			alert.show();
			return;
		}
		
		if(selection.equals("Get by strings")){	
			SearchHandler.getInstance().getByStrings(this,this, searchField.getText(), 0L);
		}
		else if(selection.equals("Get by rating")) {
			try {
				int rating = Integer.parseInt(ratingDropDown.getText());
				SearchHandler.getInstance().getByRating(this, rating, 0);
			}
			catch(NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING, "Could not parse integer");
				alert.show();
			}
		}
		else if(selection.equals("Get by rating and string")){
			try {
				int rating = Integer.parseInt(ratingDropDown.getText());
				SearchHandler.getInstance().getByRatingAndStrings(this, rating, searchField.getText(), 0L);
			}
			catch(NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING, "Could not parse integer");
				alert.show();
			}
		}
		else if(selection.equals("Get by including words")){	
			SearchHandler.getInstance().getByStringsInclusive(this, searchField.getText(), 0L);
			SearchHandler.getInstance().getNumberOfReviewsByInclusiveStrings(this, this, searchField.getText());
		}
    }
	
	public void clearChartAndSearchBar(ActionEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				searchField.clear();
				barChart.getData().clear();
			}
		});
	}
	public void clearChart() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				barChart.getData().clear();
			}
		});
	}
	
	public void ratingDropDownChange(ActionEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				   final MenuItem source = (MenuItem) event.getSource();
				   ratingDropDown.setText(source.getText());
			}
		});
	}

	@Override
	public void processGetNumberOfReviewsCallBack(Integer response) {
		System.out.println("processGetNumberOfReviewsCallBack");
		Map<String, Integer> results = new HashMap<>();
		results.put(searchField.getText(), response);
		setBarChartByStringLabel(results);
	}
}
