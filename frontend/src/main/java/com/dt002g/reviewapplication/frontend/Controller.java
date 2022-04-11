package com.dt002g.reviewapplication.frontend;

import com.dt002g.reviewapplication.frontend.service.*;
import com.dt002g.reviewapplication.frontend.util.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller  implements Initializable, GetReviewsCallBack, GetRatingStatsCallBack, GetNumberOfReviewsCallBack {
	@FXML private GridPane root;

	@FXML private RadioButton getAllRadioButton = new RadioButton();
	@FXML private RadioButton getByRatingRadioButton = new RadioButton();
	@FXML private RadioButton getByStringsRadioButton = new RadioButton();
	@FXML private RadioButton getByRatingAndStringsRadioButton = new RadioButton();
	@FXML private RadioButton getByStringsInclusiveRadioButton;
	@FXML final private ToggleGroup group = new ToggleGroup();
	
	@FXML private CheckBox checkBoxOneTen;
	@FXML private CheckBox keepChartCheckBox;

	@FXML private TabPane tabPane;
	@FXML private Tab tableDataTab;
	@FXML private Tab barChartTab;

	@FXML private Button searchButton;
	@FXML private TextField searchField;
	
	@FXML private BarChart<Number, Number> barChart;
    @FXML private CategoryAxis barChartYAxis;
    @FXML private NumberAxis barChartXAxis;

	private File csvFile;
	private ScrollBar reviewTableScrollBar;
	private ChangeListener<Number> scrollListener;
	private String selection = "Get all";
	private final ObservableList<Review> reviewsInTable = FXCollections.observableArrayList();
	private final ArrayList<Review> reviews = new ArrayList<>();
	private final ArrayList<RatingStats> ratingsByComment = new ArrayList<>();
	private final HashMap<Integer, Review> reviewMap = new HashMap<>();
	private static ExecutorService executorService = Executors.newFixedThreadPool(1);

	public HashMap<Integer, Review> getReviewMap(){
		return reviewMap;
	}

    @FXML
    private TableView<Review> referenceTable;
    
    @FXML
    private TableColumn<Review, Long> idColumn;
    
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    
    @FXML
    private TableColumn<Review, String> freeTextColumn;
    
    @FXML
    private Tab importTab;

    @FXML
    private ComboBox<String> selectRating;

    @FXML
    private ComboBox<String> selectFreeText;

    @FXML
    private Button chooseFileButton;

    @FXML
    private Button storeDataButton;
    

    @FXML
    private Spinner<Integer> ratingSpinner;

    @FXML
    private Spinner<Integer> minRating;

    @FXML
    private Spinner<Integer> maxRating;

	@FXML private RadioButton oneFiveScaleRadioButton = new RadioButton();

	@FXML private RadioButton oneThreeScaleRadioButton = new RadioButton();
	private final ToggleGroup sentimentGroup = new ToggleGroup();
	private String selectedAnalysisRadioButton = "Scale 1-5";

	@FXML private Tab sentimentAnalysisTab;

	@FXML private TableView<SentimentCorrelationStatistics> sentimentTableView;
	private final ObservableList<SentimentCorrelationStatistics> sentimentCorrelationStatisticsList = FXCollections.observableArrayList();
	@FXML private TableColumn<SentimentCorrelationStatistics, String> ratingSpanColumn;
	@FXML private TableColumn<SentimentCorrelationStatistics, String> sentimentColumn;
	@FXML private TableColumn<SentimentCorrelationStatistics, Integer> correlatingReviewsColumn;
	@FXML private TableColumn<SentimentCorrelationStatistics, Double> correlatingPercentColumn;
	@FXML private TableColumn<SentimentCorrelationStatistics, Integer> totalReviewsColumn;
    
    @FXML
    void chooseFileButtonClicked(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Resource File");
    	csvFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
    	if(csvFile == null || !(csvFile.getName().endsWith(".csv") || csvFile.getName().endsWith(".CSV"))) {
    		showInvalidCSVFileAlertDialog((Node)event.getSource());
    		csvFile = null;
    	}
    	else {
	    	ArrayList<String> headers = CSVHandler.getInstance().getHeades(csvFile);
	    	if(headers.size() < 2) {
	    		showInvalidCSVFileAlertDialog((Node)event.getSource());
	    	}
	    	else {
	    		selectRating.getItems().clear();
		    	selectRating.getItems().addAll(headers);
		    	selectFreeText.getItems().clear();
		    	selectFreeText.getItems().addAll(headers);
		    	
			    selectRating.setDisable(false);
			    selectFreeText.setDisable(false);
			    storeDataButton.setDisable(false);
			    minRating.setDisable(false);
			    maxRating.setDisable(false);
	    	}
    	}
    }
    
    @FXML 
    void storeDataButtonClicked(ActionEvent event){
    	if(selectRating.getValue() == null || selectRating.getValue().equals("") || selectFreeText.getValue() == null || selectFreeText.getValue().equals("") || minRating.getValue() == null || maxRating.getValue() == null || minRating.getValue() >= maxRating.getValue()) {
    		
    		if(selectRating.getValue() == null || selectRating.getValue().equals("") || selectFreeText.getValue() == null || selectFreeText.getValue().equals("") ) {
    			missingHeaderMappingsCSVFileAlertDialog((Node)event.getSource());
    		}
    		else {
    			missingRatingMappingsForCSVFileAlertDialog((Node)event.getSource());
    		}
    	}
    	else {
			ArrayList<String> neededHeaders = new ArrayList<>();
			neededHeaders.add(selectRating.getValue());
			neededHeaders.add(selectFreeText.getValue());
			//ArrayList<String> data = CSVHandler.getInstance().parseCSVFile(neededHeaders, csvFile, minRating.getValue(), maxRating.getValue());
			ProgressWindow prog = new ProgressWindow((Stage)((Node)event.getSource()).getScene().getWindow(), CSVHandler.getInstance(), executorService);
			prog.activate();
			//CSVHandler.getInstance().parseCSVFileAndSend(neededHeaders, csvFile, minRating.getValue(), maxRating.getValue());

			CSVHandlerTask task = new CSVHandlerTask(neededHeaders, csvFile, minRating.getValue(), maxRating.getValue());
			executorService = Executors.newFixedThreadPool(1);
			executorService.execute(task);

			/*if(data == null) {
				couldNotParseCSVFileAlertDialog((Node)event.getSource());
			}
			else {
		        File myObj = new File("localCsvFile.csv");
		        try {
					if (myObj.createNewFile()) {
						BufferedWriter writer = new BufferedWriter(new FileWriter("localCsvFile.csv"));
						for(String s: data) {
							 writer.write(s + "\n");

						}
						writer.close();

						ReviewBackendAPIService.getInstance().uploadCSVFile(myObj);
					}
				} catch (IOException | SecurityException e) {
					System.out.println(e.getMessage());
				}

			}*/
    	}
    	Platform.runLater(() -> {
			selectRating.setDisable(true);
			selectFreeText.setDisable(true);
			storeDataButton.setDisable(true);
			minRating.setDisable(true);
			maxRating.setDisable(true);
			});
	    csvFile = null;
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ratingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1));
		minRating.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
		maxRating.setValueFactory(
				new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 1));
	    selectRating.setDisable(true);
	    selectFreeText.setDisable(true);
	    storeDataButton.setDisable(true);
	    minRating.setDisable(true);
	    maxRating.setDisable(true);

		//  Reviews table
	    referenceTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
	    referenceTable.setId("tableStyle");
		referenceTable.setItems(reviewsInTable);
		idColumn.setCellValueFactory(rowData -> rowData.getValue().idProperty());
		ratingColumn.setCellValueFactory(rowData -> rowData.getValue().ratingProperty());
		freeTextColumn.setCellValueFactory(rowData -> rowData.getValue().freeTextProperty());

		//  Sentiment table
		sentimentTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		sentimentTableView.setItems(sentimentCorrelationStatisticsList);
		ratingSpanColumn.setCellValueFactory(rowData -> rowData.getValue().ratingSpan);
		sentimentColumn.setCellValueFactory(rowData -> rowData.getValue().sentiment);
		correlatingReviewsColumn.setCellValueFactory(rowData -> rowData.getValue().numberOfCorrelations);
		correlatingPercentColumn.setCellValueFactory(rowData -> rowData.getValue().correlationPercent);
		totalReviewsColumn.setCellValueFactory(rowData -> rowData.getValue().totalReviews);

		root.setOnKeyPressed(event -> {
			if(event.getCode().equals(KeyCode.ENTER)) {
				ActionEvent ae = new ActionEvent();
				searchAction(ae);
			}
		});

		//  Prevent permanent tabs to be closed
		tableDataTab.setClosable(false);
		barChartTab.setClosable(false);
		importTab.setClosable(false);
		tabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
		barChart.setAnimated(false);
		sentimentAnalysisTab.setClosable(false);

		//  Sentiment radio button code
		oneFiveScaleRadioButton.setToggleGroup(sentimentGroup);
		oneThreeScaleRadioButton.setToggleGroup(sentimentGroup);
		oneFiveScaleRadioButton.setSelected(true);

		//  Fixa switch sen när fler funktioner finns
		sentimentGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
			RadioButton tempButton = (RadioButton) sentimentGroup.getSelectedToggle();
			selectedAnalysisRadioButton = tempButton.getText();

			switch (selectedAnalysisRadioButton) {
				case "Scale 1-5":
					setCorrelationTableView();
					break;
				case "Scale 1-3":
					break;
			}
		});


		//  Radio button code
		getAllRadioButton.setToggleGroup(group);
		getByRatingRadioButton.setToggleGroup(group);
		getByStringsRadioButton.setToggleGroup(group);
		getByRatingAndStringsRadioButton.setToggleGroup(group);
		getByStringsInclusiveRadioButton.setToggleGroup(group);
		getAllRadioButton.setSelected(true);
		searchField.setVisible(false);
		searchField.setManaged(false);
		ratingSpinner.setVisible(false);
		ratingSpinner.setManaged(false);
		
		group.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
			reviewsInTable.clear();
			reviews.clear();

			if(reviewTableScrollBar != null && scrollListener != null) {
				reviewTableScrollBar.valueProperty().removeListener(scrollListener);
			}
			if(group.getSelectedToggle().getUserData() != null) {
				selection = group.getSelectedToggle().getUserData().toString();
			}

			switch (selection) {
				case "Get all":
					searchField.setVisible(false);
					searchField.setManaged(false);
					ratingSpinner.setVisible(false);
					ratingSpinner.setManaged(false);
					break;
				case "Get by rating":
					searchField.setVisible(false);
					searchField.setManaged(false);
					ratingSpinner.setVisible(true);
					ratingSpinner.setManaged(true);
					break;
				case "Get by strings":
				case "Get by including words":
					searchField.setVisible(true);
					searchField.setManaged(true);
					ratingSpinner.setVisible(false);
					ratingSpinner.setManaged(false);
					break;
				case "Get by rating and string":
					searchField.setVisible(true);
					searchField.setManaged(true);
					ratingSpinner.setVisible(true);
					ratingSpinner.setManaged(true);
					break;
			}
		});
	}

	public void processGetSentimentStatisticsCallback(List<?> response){
		//  Kod för callback
	}

	public void setCorrelationTableView(){
		// Test
		sentimentCorrelationStatisticsList.clear();
		SentimentCorrelationStatistics test = new SentimentCorrelationStatistics("81-100", "Very Positive", 0.36, 7612, 452100);
		SentimentCorrelationStatistics test2 = new SentimentCorrelationStatistics("61-80", "Positive", 0.71, 6712, 1100);
		SentimentCorrelationStatistics test3 = new SentimentCorrelationStatistics("41-60", "Neutral", 0.37, 6113, 12300);
		SentimentCorrelationStatistics test4 = new SentimentCorrelationStatistics("21-40", "Negative", 0.93, 875, 1000);
		SentimentCorrelationStatistics test5 = new SentimentCorrelationStatistics("1-20", "Very Negative", 0.45, 2165, 1000);
		sentimentCorrelationStatisticsList.add(test);
		sentimentCorrelationStatisticsList.add(test2);
		sentimentCorrelationStatisticsList.add(test3);
		sentimentCorrelationStatisticsList.add(test4);
		sentimentCorrelationStatisticsList.add(test5);


	}
	
	public void setReviews(List<Review> pReviews, int page) {
		Platform.runLater(() -> {


		if(pReviews.size() > 0) {
			long lastId;
			if(reviewsInTable.size() > 0) {
				lastId = reviewsInTable.get(reviewsInTable.size()-1).getId();
			}
			reviewsInTable.addAll(pReviews);
			Utility.customResize(referenceTable);
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
						switch (selection) {
							case "Get all":
								ReviewBackendAPIService.getInstance().getTopReviewsLargerThanId(this, fetchId);
								break;
							case "Get by strings":
								SearchHandler.getInstance().getByStrings(this, this, searchField.getText(), fetchId);
								break;
							case "Get by rating": {
								int rating = ratingSpinner.getValue();
								SearchHandler.getInstance().getByRating(this, rating, fetchId);
								break;
							}
							case "Get by rating and string": {
								int rating = ratingSpinner.getValue();
								SearchHandler.getInstance().getByRatingAndStrings(this, rating, searchField.getText(), fetchId);
								break;
							}
							case "Get by including words":
								SearchHandler.getInstance().getByStringsInclusive(this, searchField.getText(), fetchId);
								break;
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

	private void setBarChart(ArrayList<RatingStats> ratingsByComment, String searchString) {
		Platform.runLater(() -> {
			String legend = searchString.substring(0, 1).toUpperCase() + searchString.substring(1);
			barChartXAxis.setLabel("Number of Reviews");
			barChartYAxis.setLabel("Rating");
			int totalCount = 0;

			XYChart.Series series1 = new XYChart.Series<>();
			for(RatingStats rating : ratingsByComment) {
				totalCount += rating.getAmount();
				int ratingN = rating.getRating();

				if(checkBoxOneTen.isSelected()) {
					ratingN = Math.round(ratingN/10);

				}
				series1.getData().add(new XYChart.Data<>(String.valueOf(ratingN), rating.getAmount()));
			}

			series1.setName(legend + ", Total count: " + totalCount);
			barChart.getData().add(series1);
		});
	}
	
	private void setPieChart(ArrayList<RatingStats> ratingsByComment, String searchString) {
		Platform.runLater(() -> {
			String legend = searchString.substring(0, 1).toUpperCase() + searchString.substring(1);
			PieChartHolder pieChart = addNewTabWithPieChart(legend);
			int totalCount = 0;
			ArrayList<Data> pieChartData = new ArrayList<>();
			Map<Integer, Integer> ratingHolder = new HashMap<>();
			for(RatingStats rating : ratingsByComment) {
				totalCount += rating.getAmount();
				int ratingN = rating.getRating();
				if(checkBoxOneTen.isSelected()) {
					ratingN = Math.round(ratingN / 10);
				}
				try {
					ratingHolder.put(ratingN, ratingHolder.get(ratingN) + rating.getAmount());
				}
				catch(NullPointerException e){
					ratingHolder.put(ratingN, rating.getAmount());
				}
			}
			for(Map.Entry<Integer, Integer> entry : ratingHolder.entrySet()){
				pieChartData.add((new Data(entry.getKey() + ": " + entry.getValue(), entry.getValue())));
			}

			ObservableList<Data> pieChartDataObservable = FXCollections.observableArrayList(pieChartData);
			pieChart.getPieChart().setData(pieChartDataObservable);
			pieChart.getPieChart().setTitle(legend + ", Total count: "+ totalCount);
			pieChart.getPieChart().setLegendSide(Side.LEFT);
			pieChart.getLabel().setTextFill(Color.BLACK);
			pieChart.getLabel().setStyle("-fx-font-size: 24;");

		   for (final Data data : pieChart.getPieChart().getData()){
			   final double percent = Utility.round((data.getPieValue() / totalCount)*100, 2);
			   Node node = data.getNode();
			   final String showOnHoverString = "Rating " + data.getName().charAt(0) + ": " + percent + "%";

			   node.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
				   pieChart.getLabel().setTranslateX(e.getSceneX());
				   pieChart.getLabel().setTranslateY(e.getSceneY() - root.getHeight());
				   pieChart.getLabel().setText(showOnHoverString);
				   pieChart.getLabel().setVisible(true);
			   });
				node.addEventHandler(MouseEvent.MOUSE_EXITED, e -> pieChart.getLabel().setVisible(false));
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
		return new PieChartHolder(pieChart, label);
	}

	private void setBarChartByStringLabel(Map<String, Integer> results) {
		System.out.println("setBarChartByStringLabel");
		Platform.runLater(() -> {
			StringBuilder legend = new StringBuilder();
			barChartXAxis.setLabel("Number of Reviews");
			barChartYAxis.setLabel("Search words");

			XYChart.Series series1 = new XYChart.Series<>();

			for (Map.Entry<String, Integer> entry : results.entrySet()) {
				legend.append(entry.getKey()).append(" ");
				series1.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
			}

			series1.setName(legend.toString());
			barChart.getData().add(series1);
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

		switch (selection) {
			case "Get by strings":
				SearchHandler.getInstance().getByStrings(this, this, searchField.getText(), 0L);
				break;
			case "Get by rating":
				try {
					int rating = ratingSpinner.getValue();
					SearchHandler.getInstance().getByRating(this, rating, 0);
				} catch (NumberFormatException e) {
					Alert alert = new Alert(AlertType.WARNING, "Could not parse integer");
					alert.show();
				}
				break;
			case "Get by rating and string":
				try {
					int rating = ratingSpinner.getValue();
					SearchHandler.getInstance().getByRatingAndStrings(this, rating, searchField.getText(), 0L);
				} catch (NumberFormatException e) {
					Alert alert = new Alert(AlertType.WARNING, "Could not parse integer");
					alert.show();
				}
				break;
			case "Get by including words":
				SearchHandler.getInstance().getByStringsInclusive(this, searchField.getText(), 0L);
				SearchHandler.getInstance().getNumberOfReviewsByInclusiveStrings(this, this, searchField.getText());
				break;
		}
    }
	
	public void clearChartAndSearchBar(ActionEvent event) {
		Platform.runLater(() -> {
			searchField.clear();
			barChart.getData().clear();
		});
	}

	public void clearChart() {
		Platform.runLater(() -> barChart.getData().clear());
	}
	
	@Override
	public void processGetNumberOfReviewsCallBack(Integer response) {
		System.out.println("processGetNumberOfReviewsCallBack");
		Map<String, Integer> results = new HashMap<>();
		results.put(searchField.getText(), response);
		setBarChartByStringLabel(results);
	}
	
	private void showInvalidCSVFileAlertDialog(Node node) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(node.getScene().getWindow());
		alert.setTitle("Not a valid CSV file");
		alert.setHeaderText("Error: Cant fetch SCV header row");
		alert.setContentText("SCV file dont end with \".csv\" or dont have a valid header row. Check the SCV file for errors and also that the separator is a \",\"");
		alert.showAndWait();
	}
	
	private void missingHeaderMappingsCSVFileAlertDialog(Node node) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(node.getScene().getWindow());
		alert.setTitle("missing values mapping to header");
		alert.setHeaderText("There is values missing to the csv header mappings");
		alert.setContentText("Select CSV header values mapping to rating and freetext in the combo boxes.");
		alert.showAndWait();
	}
	
	private void missingRatingMappingsForCSVFileAlertDialog(Node node) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(node.getScene().getWindow());
		alert.setTitle("missing csvFile min and maxRating");
		alert.setHeaderText("There is values missing to the csv rating mappings for min and max value.");
		alert.setContentText("To be able to pars the CSV rating data to a common format, the csv rating scale need to be known. Please fill in the scales min and max value");
		alert.showAndWait();
	}
	
	private void couldNotParseCSVFileAlertDialog(Node node) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(node.getScene().getWindow());
		alert.setTitle("Could not parse CSV file");
		alert.setHeaderText("There was an error parsing the CSV file");
		alert.setContentText("The CSV file could not be parsed. Check the SCV file for errors and also that the separator is a \",\" ");
		alert.showAndWait();
	}
}
