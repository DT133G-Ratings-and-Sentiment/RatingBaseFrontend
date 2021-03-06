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
import javafx.scene.input.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller  implements Initializable, GetReviewsCallBack, GetRatingStatsCallBack, GetNumberOfReviewsCallBack, GetSentimentStatisticsCallBack, GetNumberOfReviewsByRatingAndAverageScoreTotalCallback, GetNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSameCallBack, GetAllReviewsWithAdjectiveMatrixCallBack, GetNumberOfReviewsWithAMountOfSentencesMatrixCallBack, GetMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack{
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


	private String selectedAnalyseFormRadioButton = "Mean";
	private String selectedSentimentOrAdjective = "Sentiment";
	private String selectedGraph = "Table";

	@FXML RadioButton meanRadioButton;
	@FXML RadioButton averageRadioButton;
	private final ToggleGroup analysisFormGroup = new ToggleGroup();
	@FXML private Tab sentimentAnalysisTab;

	@FXML private RadioButton sentimentButton;
	@FXML private RadioButton adjectivesButton;
	@FXML private RadioButton adjectiveFrequencyInReviewButton;
	@FXML private RadioButton sentenceButton;
	private final ToggleGroup sentimentAdjectivesGroup = new ToggleGroup();

	@FXML private RadioButton scatterChartButton;
	@FXML private RadioButton tableButton;
	private final ToggleGroup visualsGroup = new ToggleGroup();

	@FXML private TableView<SentimentCorrelationStatistics> sentimentTableView;
	private final ObservableList<SentimentCorrelationStatistics> sentimentCorrelationStatisticsList = FXCollections.observableArrayList();
	@FXML private TableColumn<SentimentCorrelationStatistics, String> ratingIntervalColumn;
	@FXML private TableColumn<SentimentCorrelationStatistics, Double> correlationCoefficientColumn;
	@FXML private TableColumn<SentimentCorrelationStatistics, Double> standardDeviationColumn;
	//@FXML private TableColumn<SentimentCorrelationStatistics, Double> confidenceIntervalColumn;

	@FXML private Button sentimentSearchButton;
	@FXML private ProgressIndicator progressIndicator;

	@FXML private TableView<AdjectiveAmountStatistics> adjectiveNumberOfTimesInReviewTableView;
	private final ObservableList<AdjectiveAmountStatistics> adjectiveNumberOfTimesInReviewList = FXCollections.observableArrayList();
	@FXML private TableColumn<AdjectiveAmountStatistics, String> adjectiveWordColumn;
	@FXML private TableColumn<AdjectiveAmountStatistics, Integer> adjectiveNumberOfAppearencesColumn;
	@FXML private TableColumn<AdjectiveAmountStatistics, Integer> adjectiveAmountOfReviewsColumn;
	@FXML private TableColumn<AdjectiveAmountStatistics, Double> adjectiveNumberOfApperancesCorrelationColumn;


	@FXML private TableView<AdjectivesStatistics> adjectiveTableView;
	private final ObservableList<AdjectivesStatistics> adjectiveStatisticsList = FXCollections.observableArrayList();
	private final ObservableList<AdjectivesStatistics> temporaryHolderAdjectiveStatisticsList = FXCollections.observableArrayList();
	@FXML private TableColumn<AdjectivesStatistics, String> adjectiveColumn;
	@FXML private TableColumn<AdjectivesStatistics, Long> adjectiveAmountColumn;
	@FXML private TableColumn<AdjectivesStatistics, Double> adjectiveCorrelationColumn;
	@FXML private TableColumn<AdjectivesStatistics, Double> percentOfAllColumn;
	@FXML private TableColumn<AdjectivesStatistics, Double> adjectiveStandardDeviation;
	@FXML private TableColumn<AdjectivesStatistics, Double> adjectiveMoreThanOneSentenceCorrelation;
	@FXML private TableColumn<AdjectivesStatistics, Long> adjectivesFrequencyMoreThanOneSentence;

	@FXML private TableView<AmountOfRatingsWithAMountOfScentences> sentenceTableView;
	private final ObservableList<AmountOfRatingsWithAMountOfScentences> scentenceStatisticsList = FXCollections.observableArrayList();
	@FXML private TableColumn<AmountOfRatingsWithAMountOfScentences, Long> amountOfSentencesColumn;
	@FXML private TableColumn<AmountOfRatingsWithAMountOfScentences, Long> amountOfReviewsColumn;
	@FXML private TableColumn<AmountOfRatingsWithAMountOfScentences, Double> amountOfReviewsPercent;
	@FXML private Button showAllAdjectivesButton;
	@FXML private Button showPositiveCorrelationAdjectivesButton;
	@FXML private Button showNegativeCorrelationAdjectivesButton;
	@FXML private Button customSortButton;
	@FXML private Label customFilteringVariables;
	@FXML private Spinner<Double> customMin;
	@FXML private Spinner<Double> customMax;
	@FXML private Label minLabel;
	@FXML private Label maxLabel;

	@FXML private AreaChart<Number, Number> areaChartMean;
	@FXML final NumberAxis areaXAxis = new NumberAxis(-10, 105, 0.5);
	@FXML final NumberAxis areaYAxis = new NumberAxis(-10, 105, 0.5);
	@FXML private ScatterChart<Number, Number> scatterChartMedian;
	@FXML private ScatterChart<Number, Number> scatterChartMean;
	@FXML private Label coefficientLabel;
	@FXML final NumberAxis xAxis = new NumberAxis(-10, 105, 0.5);
	@FXML final NumberAxis yAxis = new NumberAxis(-10, 105, 0.5);
	@FXML final NumberAxis xAxisScatter = new NumberAxis(-10, 105, 0.5);
	@FXML final NumberAxis yAxisScatter = new NumberAxis(-10, 105, 0.5);

	final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);


    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		adjectiveTableView.setOnKeyPressed(event -> {
			if (keyCodeCopy.match(event)) {
				copySelectionToClipboard(adjectiveTableView);
			}
		});

		//  Upload csv
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
		correlationCoefficientColumn.setCellValueFactory(rowData -> rowData.getValue().correlationCoefficient);
		standardDeviationColumn.setCellValueFactory(rowData -> rowData.getValue().standardDeviation);
		ratingIntervalColumn.setCellValueFactory(rowData -> rowData.getValue().ratingInterval);

		adjectiveNumberOfTimesInReviewTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		adjectiveNumberOfTimesInReviewTableView.setItems(adjectiveNumberOfTimesInReviewList);
		adjectiveWordColumn.setCellValueFactory(rowData -> rowData.getValue().adjective);
		adjectiveNumberOfAppearencesColumn.setCellValueFactory(rowData -> rowData.getValue().numberOfAppearencesInReviews);
		adjectiveAmountOfReviewsColumn.setCellValueFactory(rowData -> rowData.getValue().amountOfReviews);
		adjectiveNumberOfApperancesCorrelationColumn.setCellValueFactory(rowData -> rowData.getValue().correlation);

		adjectiveTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		adjectiveTableView.setItems(adjectiveStatisticsList);
		adjectiveColumn.setCellValueFactory(rowData -> rowData.getValue().adjective);
		adjectiveAmountColumn.setCellValueFactory(rowData-> rowData.getValue().amount);
		adjectiveCorrelationColumn.setCellValueFactory(rowData -> rowData.getValue().correlation);
		percentOfAllColumn.setCellValueFactory(rowData -> rowData.getValue().percent);
		adjectiveStandardDeviation.setCellValueFactory(rowData -> rowData.getValue().standardDeviation);
		adjectiveMoreThanOneSentenceCorrelation.setCellValueFactory(rowData -> rowData.getValue().correlationMoreThanOneSentence);
		adjectivesFrequencyMoreThanOneSentence.setCellValueFactory(rowData -> rowData.getValue().amountMoreThanOneSentence);

		sentenceTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		sentenceTableView.setItems(scentenceStatisticsList);
		amountOfSentencesColumn.setCellValueFactory(rowData -> rowData.getValue().amountOfSentencesProperty());
		amountOfReviewsColumn.setCellValueFactory(rowData-> rowData.getValue().amountOfRatingsProperty());
		amountOfReviewsPercent.setCellValueFactory(rowData -> rowData.getValue().percentProperty());

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
		meanRadioButton.setToggleGroup(analysisFormGroup);
		averageRadioButton.setToggleGroup(analysisFormGroup);
		meanRadioButton.setSelected(true);
		sentimentButton.setToggleGroup(sentimentAdjectivesGroup);
		sentimentButton.setSelected(true);
		adjectivesButton.setToggleGroup(sentimentAdjectivesGroup);
		adjectiveFrequencyInReviewButton.setToggleGroup(sentimentAdjectivesGroup);
		sentenceButton.setToggleGroup(sentimentAdjectivesGroup);

		tableButton.setToggleGroup(visualsGroup);
		scatterChartButton.setToggleGroup(visualsGroup);
		tableButton.setSelected(true);


		analysisFormGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
			RadioButton tempButton = (RadioButton) analysisFormGroup.getSelectedToggle();
			selectedAnalyseFormRadioButton = tempButton.getText();
		});
		sentimentAdjectivesGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
			RadioButton tempButton = (RadioButton) sentimentAdjectivesGroup.getSelectedToggle();
			selectedSentimentOrAdjective = tempButton.getText();
			if(selectedSentimentOrAdjective.equals("Sentence") ){
				tableButton.setVisible(false);
				scatterChartButton.setVisible(false);
				averageRadioButton.setVisible(false);
				meanRadioButton.setVisible(false);
			}
			else if(selectedSentimentOrAdjective.equals("AdjectiveFrequencyInReview")){
				tableButton.setVisible(false);
				scatterChartButton.setVisible(false);
				averageRadioButton.setVisible(false);
				meanRadioButton.setVisible(false);
			}
			else if(!selectedSentimentOrAdjective.equals("Sentiment")){
				tableButton.setVisible(false);
				scatterChartButton.setVisible(false);
				averageRadioButton.setVisible(true);
				meanRadioButton.setVisible(true);
			}
			else{
				tableButton.setVisible(true);
				scatterChartButton.setVisible(true);
				averageRadioButton.setVisible(true);
				meanRadioButton.setVisible(true);
			}
		});
		visualsGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) ->{
			RadioButton tempButton = (RadioButton) visualsGroup.getSelectedToggle();
			selectedGraph = tempButton.getText();

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

		// Progress indicator for analysis
		progressIndicator.setVisible(false);
		progressIndicator.setManaged(false);

		//  Show buttons disabled until usable
		setAllVisibleFalse();
		customMax.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-0.99, 1.0, 0, 0.01));
		customMin.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-1.00, 0.99, 0, 0.01));


		/*StatisticsCalculator.getCorrelationCoefficient(null);
		//GetNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSameCallBack callback = this;
		//ReviewBackendAPIService.getInstance().getNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSame(callback);
		GetAllReviewsWithAdjectiveMatrixCallBack callback = this;
		ReviewBackendAPIService.getInstance().getAllReviewsWithAdjectiveMatrix(callback);*/
	}

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
			ArrayList<String> headers = CSVHandler.getInstance().getHeaders(csvFile);
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

	@FXML protected void showAllAdjectives(ActionEvent event){
		for(Iterator<AdjectivesStatistics> itr = temporaryHolderAdjectiveStatisticsList.iterator(); itr.hasNext();){
			AdjectivesStatistics as = itr.next();
				if (!adjectiveStatisticsList.contains(as)) {
					adjectiveStatisticsList.add(as);
				}
				itr.remove();
			}
		temporaryHolderAdjectiveStatisticsList.sort(Comparator.comparingDouble(AdjectivesStatistics::getCorrelation));
	}
	@FXML protected void showPositiveAdjectives(ActionEvent event){
		Platform.runLater(() -> {
		showAllAdjectives(event);
			for(Iterator<AdjectivesStatistics> itr = adjectiveStatisticsList.iterator(); itr.hasNext();){
				AdjectivesStatistics as = itr.next();
				if(!(as.correlation.get() == 1.0)){
					temporaryHolderAdjectiveStatisticsList.add(as);
					itr.remove();
				}
			}
		});
		adjectiveStatisticsList.sort(Comparator.comparingDouble(AdjectivesStatistics::getAmount));
	}
	@FXML protected void showNegativeAdjectives(ActionEvent event){
		Platform.runLater(() -> {
			showAllAdjectives(event);
			for(Iterator<AdjectivesStatistics> itr = adjectiveStatisticsList.iterator(); itr.hasNext();){
			AdjectivesStatistics as = itr.next();
			if(!(as.correlation.get() == -1.0)){
				temporaryHolderAdjectiveStatisticsList.add(as);
				itr.remove();
			}
		}
		});
		adjectiveStatisticsList.sort(Comparator.comparingDouble(AdjectivesStatistics::getAmount));
	}

	@FXML protected void showCustomFilterAdjectives(ActionEvent event){
		Platform.runLater(() -> {
			showAllAdjectives(event);
			for(Iterator<AdjectivesStatistics> itr = adjectiveStatisticsList.iterator(); itr.hasNext();){
				AdjectivesStatistics as = itr.next();
				if(!(as.correlation.get() <= customMax.getValue() && as.correlation.get() >= customMin.getValue())){
					temporaryHolderAdjectiveStatisticsList.add(as);
					itr.remove();
				}
			}
		});
		adjectiveStatisticsList.sort(Comparator.comparingDouble(AdjectivesStatistics::getAmount));
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

	@Override
	public void processGetSentimentStatisticsCallBack(List<SentimentStatisticsBackendEntity> response) {
		sentimentCorrelationStatisticsList.clear();
		adjectiveTableView.setVisible(false);
		adjectiveTableView.setManaged(false);
		sentimentTableView.setVisible(true);
		sentimentTableView.setManaged(true);
		//analyseAndSaveAnalysedSentimentToTable(response);
	}

	private void setAllVisibleFalse(){
		areaChartMean.setManaged(false);
		areaChartMean.setVisible(false);
		scatterChartMedian.setManaged(false);
		scatterChartMedian.setVisible(false);
		scatterChartMean.setManaged(false);
		scatterChartMean.setVisible(false);
		coefficientLabel.setVisible(false);
		adjectiveTableView.setVisible(false);
		adjectiveTableView.setManaged(false);
		sentimentTableView.setVisible(false);
		sentimentTableView.setManaged(false);
		showAllAdjectivesButton.setVisible(false);
		showAllAdjectivesButton.setManaged(false);
		//adjectiveFrequencyInReviewButton.setVisible(false);
		//adjectiveFrequencyInReviewButton.setManaged(false);
		showNegativeCorrelationAdjectivesButton.setVisible(false);
		showNegativeCorrelationAdjectivesButton.setManaged(false);
		showPositiveCorrelationAdjectivesButton.setVisible(false);
		showPositiveCorrelationAdjectivesButton.setManaged(false);
		customSortButton.setVisible(false);
		customSortButton.setManaged(false);
		customFilteringVariables.setVisible(false);
		customFilteringVariables.setManaged(false);
		customMax.setVisible(false);
		customMax.setManaged(false);
		customMin.setVisible(false);
		customMin.setManaged(false);
		minLabel.setVisible(false);
		minLabel.setManaged(false);
		maxLabel.setVisible(false);
		maxLabel.setManaged(false);
		sentenceTableView.setVisible(false);
		sentimentTableView.setManaged(false);

	}
	public void startSentimentAnalysis(ActionEvent event) {
		setAllVisibleFalse();
		sentimentSearchButton.setDisable(true);
		progressIndicator.setManaged(true);
		progressIndicator.setVisible(true);
		progressIndicator.setProgress(-1d);
		switch (selectedSentimentOrAdjective) {
			case "Sentence":
				ReviewBackendAPIService.getInstance().getNumberOfReviewsWithAMountOfSentencesMatrix(this);
				break;
			case "Sentiment":

				if (selectedAnalyseFormRadioButton.equals("Mean")) {
					ReviewBackendAPIService.getInstance().getNumberOfReviewsByRatingAndAverageScoreTotal(this);
				} else {
					ReviewBackendAPIService.getInstance().getNumberOfReviewsByRatingAndMedianScoreTotalMatrix(this);
				}
				break;
			case "AdjectiveFrequencyInReview":
				//ReviewBackendAPIService.getInstance().getAllReviewsWithAdjective()
				//ReviewBackendAPIService.getInstance().getListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews(this);
				ReviewBackendAPIService.getInstance().getMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviews(this);
				break;
			default:
				ReviewBackendAPIService.getInstance().getAllReviewsWithAdjectiveMatrix(this);
				break;
		}

	}

	@Override
	public void processGetNumberOfReviewsByRatingAndAverageScoreCallBack(List<SentimentStatisticsBackendEntity> response) {
	Platform.runLater(() ->{
		if(selectedGraph.equals("Table")){
			sentimentCorrelationStatisticsList.clear();
			sentimentTableView.setVisible(true);
			sentimentTableView.setManaged(true);
			StatisticResult statisticResult = StatisticsCalculator.getCorrelationCoefficient(response);
			SentimentCorrelationStatistics sentimentCorrelationStatistics = new SentimentCorrelationStatistics(statisticResult.correlationCofficient, statisticResult.getRatingStandardDeviation(), 0, "0-100");
			sentimentCorrelationStatisticsList.add(sentimentCorrelationStatistics);

			List<List<SentimentStatisticsBackendEntity>> lists = new ArrayList<>();
			for(int i = 0; i <= 5; i++){
				lists.add(new ArrayList<SentimentStatisticsBackendEntity>());
			}
			for(SentimentStatisticsBackendEntity s: response){
				if(s.rating >= 50) {
					lists.get(1).add(s);
				}
				else if(s.rating <= 50){
					lists.get(0).add(s);
				}
				else{
					lists.get(1).add(s);
					lists.get(0).add(s);
				}
				if(s.rating >74 && s.rating < 101){
					lists.get(2).add(s);
				}
				if(s.rating >-1 && s.rating < 26){
					lists.get(3).add(s);
				}
				if(s.rating >24 && s.rating < 76){
					lists.get(4).add(s);
				}
				if(s.rating == 100 || s.rating == 0){
					lists.get(5).add(s);
				}
			}
			int index = 40;
			for(int i = 0; i < lists.size(); i++){
				String ratingInterval = "";
				if(i == 0){
					ratingInterval = "0-50";
				}
				else if(i == 1){
					ratingInterval = "50-100";
				}
				else if(i == 2){
					ratingInterval = "75-100";
				}
				else if(i == 3){
					ratingInterval = "0-25";
				}
				else if(i == 4){
					ratingInterval = "25-75";
				}
				else if(i == 5){
					ratingInterval = "0 or 100";
				}
				StatisticResult tempStatisticResult = StatisticsCalculator.getCorrelationCoefficient(lists.get(i));
				SentimentCorrelationStatistics tempSentimentCorrelationStatistics = new SentimentCorrelationStatistics(tempStatisticResult.correlationCofficient, tempStatisticResult.getRatingStandardDeviation(), 0, ratingInterval);
				sentimentCorrelationStatisticsList.add(tempSentimentCorrelationStatistics);
			}

		}
		else{

			if(selectedAnalyseFormRadioButton.equals("Median")) {
				scatterChartMedian.getData().clear();
				scatterChartMedian.setVisible(true);
				scatterChartMedian.setManaged(true);
				coefficientLabel.setVisible(false);



			}
			else{
				/*
				scatterChartMean.getData().clear();
				scatterChartMean.setVisible(true);
				scatterChartMean.setManaged(true);

				 */
				areaChartMean.getData().clear();
				areaChartMean.setVisible(true);
				areaChartMean.setManaged(true);
			}

			double coefficient = StatisticsCalculator.getCorrelationCoefficient(response).correlationCofficient;
			coefficient = Utility.round(coefficient, 2);

			coefficientLabel.setText("r = " + coefficient);
			coefficientLabel.setVisible(true);
			xAxisScatter.setLabel("Rating");
			yAxisScatter.setLabel("Sentiment");

			XYChart.Series lowest = new XYChart.Series();
			XYChart.Series lower = new XYChart.Series();
			XYChart.Series middle = new XYChart.Series();
			XYChart.Series higher = new XYChart.Series();
			XYChart.Series highest = new XYChart.Series();
			lowest.setName("Lowest amount");
			lower.setName("Lower amount");
			middle.setName("Middle amount");
			higher.setName("Higher amount");
			highest.setName("Highest amount");

			long totalAmount = 0;
			for(SentimentStatisticsBackendEntity sentimentStatisticsBackendEntity: response){
				totalAmount += sentimentStatisticsBackendEntity.getAmount();
			}

			for (SentimentStatisticsBackendEntity sentimentStatisticsBackendEntity : response) {
				if(sentimentStatisticsBackendEntity.getAmount() == 0){
					continue;
				}
				double size = calculateSize(sentimentStatisticsBackendEntity.getAmount(), totalAmount);

				if(size >= 9){
					highest.getData().add(new XYChart.Data(sentimentStatisticsBackendEntity.getRating(), sentimentStatisticsBackendEntity.getMinScore()));
				}
				else if(size >= 6){
					higher.getData().add(new XYChart.Data(sentimentStatisticsBackendEntity.getRating(), sentimentStatisticsBackendEntity.getMinScore()));
				}
				else if(size >= 4){
					middle.getData().add(new XYChart.Data(sentimentStatisticsBackendEntity.getRating(), sentimentStatisticsBackendEntity.getMinScore()));
				}
				else if(size >= 2){
					lower.getData().add(new XYChart.Data(sentimentStatisticsBackendEntity.getRating(), sentimentStatisticsBackendEntity.getMinScore()));
				}
				else{
					lowest.getData().add(new XYChart.Data(sentimentStatisticsBackendEntity.getRating(), sentimentStatisticsBackendEntity.getMinScore()));
				}


			}

		if(selectedAnalyseFormRadioButton.equals("Median")) {
			scatterChartMedian.getData().addAll(lowest, lower, middle, higher, highest);
		}
		else{
			scatterChartMean.getData().addAll(lowest, lower, middle, higher, highest);
		}
		}
		progressIndicator.setManaged(false);
		progressIndicator.setVisible(false);
		sentimentSearchButton.setDisable(false);
	});


	}


	private double calculateSize(long amount, long totalAmount){

		double percent = (double)amount/(double)totalAmount;

		if(((percent*100)) > 10){
			return 10;
		}
		if(((percent*100)) < 1){
			return 1;
		}
		return Math.ceil(percent*100);
	}

	@Override
	public void processGetNumberOfTimesAdjectiveOccureWhenRatingAndScoreIsTheSameCallBack(List<Pair<String, Long>> response) {
		response.sort((o1, o2) -> o2.second.compareTo(o1.second));
		adjectiveTableView.setVisible(true);
		adjectiveTableView.setManaged(true);
		for(Pair<String, Long> p: response){
			AdjectivesStatistics as = new AdjectivesStatistics(p.first, p.second, 0.0, 0.0, 0.0, 0.0, 0L);
			adjectiveStatisticsList.add(as);
		}
		progressIndicator.setManaged(false);
		progressIndicator.setVisible(false);
		sentimentSearchButton.setDisable(false);
	}

	@Override
	public void processGetAllReviewsWithAdjectiveMatrixCallBack(List<ReviewsByAdjective> response) {
		adjectiveTableView.setVisible(true);
		adjectiveTableView.setManaged(true);
		double negativeTotal = 0;
		double positiveTotal = 0;
		List<SentimentStatisticsBackendEntity> moreThanOneSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> moreThanTwoSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> moreThanThreeSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> moreThanFourSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> moreThanFiveSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> moreThanSixSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> moreThanSevenSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> moreThanEightSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> moreThanNineSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> moreThanTenSentences = new ArrayList<>();
		List<SentimentStatisticsBackendEntity> goodMoreThanOneSentence = new ArrayList<>();

			for (ReviewsByAdjective rba : response) {

				List<SentimentStatisticsBackendEntity> sentimentStatisticsBackendEntityList = new ArrayList<>();
				List<SentimentStatisticsBackendEntity> moreThanOneList = new ArrayList<>();
				Long amountMoreThanOne = 0L;
				for (ReviewBackendEntity rbe : rba.getReviews()) {
					SentimentStatisticsBackendEntity sentimentStatisticsBackendEntity;
					SentimentStatisticsBackendEntity moreThanOneSentenceForAdjective = null;


					if (selectedAnalyseFormRadioButton.equals("Mean")) {

						sentimentStatisticsBackendEntity = new SentimentStatisticsBackendEntity(rbe.getRating(), rbe.normalisedAverageSentenceScore, rbe.normalisedAverageSentenceScore, 1);
						if(rbe.sentences.size() > 1) {
							amountMoreThanOne++;
							moreThanOneSentenceForAdjective = new SentimentStatisticsBackendEntity(rbe.getRating(), rbe.normalisedAverageSentenceScore, rbe.normalisedAverageSentenceScore, 1);
						}
					}
					else{
						sentimentStatisticsBackendEntity = new SentimentStatisticsBackendEntity(rbe.getRating(), rbe.normalisedMedianSentenceScore, rbe.normalisedMedianSentenceScore, 1);
						if(rbe.sentences.size() > 1) {
							amountMoreThanOne++;
							moreThanOneSentenceForAdjective = new SentimentStatisticsBackendEntity(rbe.getRating(), rbe.normalisedMedianSentenceScore, rbe.normalisedMedianSentenceScore, 1);
						}
					}
					sentimentStatisticsBackendEntityList.add(sentimentStatisticsBackendEntity);
					if(moreThanOneSentenceForAdjective != null) {
						moreThanOneList.add(moreThanOneSentenceForAdjective);
					}

					if(rbe.sentences.size() > 1){

						moreThanOneSentences.add(sentimentStatisticsBackendEntity);
					}
					if(rbe.sentences.size() > 2){
						moreThanTwoSentences.add(sentimentStatisticsBackendEntity);
					}
					if(rbe.sentences.size() > 3){
						moreThanThreeSentences.add(sentimentStatisticsBackendEntity);
					}
					if(rbe.sentences.size() > 4){
						moreThanFourSentences.add(sentimentStatisticsBackendEntity);
					}
					if(rbe.sentences.size() > 5){
						moreThanFiveSentences.add(sentimentStatisticsBackendEntity);
					}
					if(rbe.sentences.size() > 6){
						moreThanSixSentences.add(sentimentStatisticsBackendEntity);
					}
					if(rbe.sentences.size() > 7){
						moreThanSevenSentences.add(sentimentStatisticsBackendEntity);
					}
					if(rbe.sentences.size() > 8){
						moreThanEightSentences.add(sentimentStatisticsBackendEntity);
					}
					if(rbe.sentences.size() > 9){
						moreThanNineSentences.add(sentimentStatisticsBackendEntity);
					}
					if(rbe.sentences.size() > 10){
						moreThanTenSentences.add(sentimentStatisticsBackendEntity);
					}

				}

				//  H??r ??r statistics result
				StatisticResult sr = StatisticsCalculator.getCorrelationCoefficient(sentimentStatisticsBackendEntityList);
				StatisticResult srMoreThanOne = StatisticsCalculator.getCorrelationCoefficient(moreThanOneList);
				if(sr.correlationCofficient > 0){
					positiveTotal += rba.getReviews().size();
				}
				else{
					negativeTotal += rba.getReviews().size();
				}
				//  H??r s??tts resultatet in
				if(Double.isNaN(sr.correlationCofficient)){
				}
				rba.setCorrelationCoefficient(sr.correlationCofficient);
				rba.standardDeviation = sr.getRatingStandardDeviation();
				rba.moreThanOneCorrelation = srMoreThanOne.correlationCofficient;
				rba.moreThanOneAmount = amountMoreThanOne;
			}
			response.sort(Comparator.comparingDouble(ReviewsByAdjective::getCorrelationCoefficient));

			System.out.println("Neg: " + negativeTotal);
			System.out.println("Pos: " + positiveTotal);
			for(ReviewsByAdjective rba : response){
				//  Om correlation ??r st??rre ??n 1.0, mindre ??n -1.0, eller NaN s?? hoppa ??ver
				if(rba.getCorrelationCoefficient() > 1.0 || rba.getCorrelationCoefficient() < -1.0 || Double.isNaN(rba.getCorrelationCoefficient())){
					if(rba.adjective.equals("variable")){
						System.out.println(rba.getCorrelationCoefficient());
					}
					continue;
				}
				AdjectivesStatistics adjectivesStatistics;
				if(rba.getCorrelationCoefficient() > 0){
					adjectivesStatistics = new AdjectivesStatistics(rba.getAdjective(), (long) rba.getReviews().size(), rba.getCorrelationCoefficient(), Utility.round(((rba.getReviews().size()/positiveTotal) * 100), 5), rba.standardDeviation, rba.moreThanOneCorrelation, rba.moreThanOneAmount);
				}
				else{
					adjectivesStatistics = new AdjectivesStatistics(rba.getAdjective(), (long) rba.getReviews().size(), rba.getCorrelationCoefficient(), Utility.round(((rba.getReviews().size()/negativeTotal) * 100), 5),rba.standardDeviation, rba.moreThanOneCorrelation, rba.moreThanOneAmount);
				}

				adjectiveStatisticsList.add(adjectivesStatistics);
			}

			System.out.println("Size more than one: " + moreThanOneSentences.size());
			StatisticResult moreThanOneSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanOneSentences);
			System.out.println("Coeffiecient more than one: " + moreThanOneSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanOneSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanOneSentencesStatistics.getSentimentScoreStandardDeviation());

			System.out.println("Size more than two: " + moreThanTwoSentences.size());
			StatisticResult moreThanTwoSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanTwoSentences);
			System.out.println("Coeffiecient more than two: " + moreThanTwoSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanTwoSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanTwoSentencesStatistics.getSentimentScoreStandardDeviation());

			System.out.println("Size more than three: " + moreThanThreeSentences.size());
			StatisticResult moreThanThreeSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanThreeSentences);
			System.out.println("Coeffiecient more than Three: " + moreThanThreeSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanThreeSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanThreeSentencesStatistics.getSentimentScoreStandardDeviation());

		System.out.println("Size more than Four: " + moreThanFourSentences.size());
		StatisticResult moreThanFourSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanFourSentences);
		System.out.println("Coeffiecient more than Four: " + moreThanFourSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanFourSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanFourSentencesStatistics.getSentimentScoreStandardDeviation());

		System.out.println("Size more than Five: " + moreThanFiveSentences.size());
		StatisticResult moreThanFiveSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanFiveSentences);
		System.out.println("Coeffiecient more than Five: " + moreThanFiveSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanFiveSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanFiveSentencesStatistics.getSentimentScoreStandardDeviation());

		System.out.println("Size more than Six: " + moreThanSixSentences.size());
		StatisticResult moreThanSixSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanSixSentences);
		System.out.println("Coeffiecient more than Six: " + moreThanSixSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanSixSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanSixSentencesStatistics.getSentimentScoreStandardDeviation());

		System.out.println("Size more than Seven: " + moreThanSevenSentences.size());
		StatisticResult moreThanSevenSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanSevenSentences);
		System.out.println("Coeffiecient more than Seven: " + moreThanSevenSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanSevenSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanSevenSentencesStatistics.getSentimentScoreStandardDeviation());

		System.out.println("Size more than Eight: " + moreThanEightSentences.size());
		StatisticResult moreThanEightSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanEightSentences);
		System.out.println("Coeffiecient more than Eight: " + moreThanEightSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanEightSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanEightSentencesStatistics.getSentimentScoreStandardDeviation());

		System.out.println("Size more than Nine: " + moreThanNineSentences.size());
		StatisticResult moreThanNineSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanNineSentences);
		System.out.println("Coeffiecient more than Nine: " + moreThanNineSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanNineSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanNineSentencesStatistics.getSentimentScoreStandardDeviation());

		System.out.println("Size more than Ten: " + moreThanTenSentences.size());
		StatisticResult moreThanTenSentencesStatistics = StatisticsCalculator.getCorrelationCoefficient(moreThanTenSentences);
		System.out.println("Coeffiecient more than Ten: " + moreThanTenSentencesStatistics.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanTenSentencesStatistics.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanTenSentencesStatistics.getSentimentScoreStandardDeviation());

		StatisticResult moreThanOneSentencesStatisticsGood = StatisticsCalculator.getCorrelationCoefficient(goodMoreThanOneSentence);
		System.out.println("Good more than one sentence: " + goodMoreThanOneSentence.size());
		System.out.println("Coeffiecient more than Ten: " + moreThanOneSentencesStatisticsGood.correlationCofficient);
		System.out.println("Rating SD more than one: " + moreThanOneSentencesStatisticsGood.getRatingStandardDeviation());
		System.out.println("Sentiment SD more than one: " + moreThanOneSentencesStatisticsGood.getSentimentScoreStandardDeviation());


		showAllAdjectivesButton.setVisible(true);
		showAllAdjectivesButton.setManaged(true);
		//adjectiveFrequencyInReviewButton.setVisible(true);
		//adjectiveFrequencyInReviewButton.setManaged(true);
		showNegativeCorrelationAdjectivesButton.setVisible(true);
		showNegativeCorrelationAdjectivesButton.setManaged(true);
		showPositiveCorrelationAdjectivesButton.setVisible(true);
		showPositiveCorrelationAdjectivesButton.setManaged(true);
		customSortButton.setVisible(true);
		customSortButton.setManaged(true);
		customFilteringVariables.setVisible(true);
		customFilteringVariables.setManaged(true);
		customMax.setVisible(true);
		customMax.setManaged(true);
		customMin.setVisible(true);
		customMin.setManaged(true);
		minLabel.setVisible(true);
		minLabel.setManaged(true);
		maxLabel.setVisible(true);
		maxLabel.setManaged(true);
		progressIndicator.setManaged(false);
		progressIndicator.setVisible(false);
		sentimentSearchButton.setDisable(false);

		}

	@Override
	public void processGetNumberOfReviewsWithAMountOfSentencesMatrixCallBack(List<AmountOfRatingsWithAMountOfScentences> response) {
		sentenceTableView.setVisible(true);
		sentenceTableView.setManaged(true);
		double total = 0;
		for( AmountOfRatingsWithAMountOfScentences amount : response){
			total += amount.getAmountOfRatings();
		}
		for( AmountOfRatingsWithAMountOfScentences amount : response){
			amount.setPercent(Utility.round((amount.getAmountOfRatings() / total)*100, 2));
		}

		scentenceStatisticsList.addAll(response);
		progressIndicator.setManaged(false);
		progressIndicator.setVisible(false);
		sentimentSearchButton.setDisable(false);
	}
	@SuppressWarnings("rawtypes")
	public void copySelectionToClipboard(final TableView<?> table) {
		final Set<Integer> rows = new TreeSet<>();
		for (final TablePosition tablePosition : table.getSelectionModel().getSelectedCells()) {
			rows.add(tablePosition.getRow());
		}
		final StringBuilder strb = new StringBuilder();
		boolean firstRow = true;
		for (final Integer row : rows) {
			if (!firstRow) {
				strb.append('\n');
			}
			firstRow = false;
			boolean firstCol = true;
			for (final TableColumn<?, ?> column : table.getColumns()) {
				if (!firstCol) {
					strb.append('\t');
				}
				firstCol = false;
				final Object cellData = column.getCellData(row);
				strb.append(cellData == null ? "" : cellData.toString());
			}
		}
		final ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(strb.toString());
		Clipboard.getSystemClipboard().setContent(clipboardContent);
	}

	/*@Override
	public void processGetListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack(List<Pair<String, Long>> listOfAdjectivesAndTimesItAppearsInReviews) {
		adjectiveNumberOfTimesInReviewTableView.setVisible(true);
		adjectiveNumberOfTimesInReviewTableView.setManaged(true);
		listOfAdjectivesAndTimesItAppearsInReviews.sort((a1,a2)-> a2.second.compareTo(a1.second));
		for(int i = 0; i < 10; i++) {
			ReviewBackendAPIService.getInstance().getAllReviewsWithAdjective(listOfAdjectivesAndTimesItAppearsInReviews.get(i).first, this);
		}
	}*/

	@Override
	public void processGetMatrixWithListOfAdjectiveWordAndTotalNumberOfTimesItAppearsInAllReviewsCallBack(List<AdjectiveReviewAmountAppearence> result) {
		adjectiveNumberOfTimesInReviewTableView.setVisible(true);
		adjectiveNumberOfTimesInReviewTableView.setManaged(true);
		for(AdjectiveReviewAmountAppearence a: result){
			StatisticResult stats = StatisticsCalculator.getCorrelationCoefficient(a.getReviewRatingByScoreMatrix());
			adjectiveNumberOfTimesInReviewList.add(new AdjectiveAmountStatistics(a.getAdjective(), a.getNumberOfAppearancesPerReview(), stats.totalRatings, stats.correlationCofficient));
		}
		progressIndicator.setManaged(false);
		progressIndicator.setVisible(false);
	}
}

