package com.dt002g.reviewapplication.frontend;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.dt002g.reviewapplication.frontend.service.GetReviewsCallBack;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendEntity;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class Controller  implements Initializable, GetReviewsCallBack {

	private final ObservableList<Review> reviews = FXCollections.observableArrayList();
	
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
		referenceTable.setItems(reviews);
		idColumn.setCellValueFactory(rowData -> rowData.getValue().idProperty());
		ratingColumn.setCellValueFactory(rowData -> rowData.getValue().ratingProperty());
		freeTextColumn.setCellValueFactory(rowData -> rowData.getValue().freeTextProperty());
		
		reviews.addAll(new Review(1, 5, "A great product"),
				new Review(2, 2, "This sucks"),
				new Review(3, 4, "I like how it works, but expensive"));
	}
	
	public void setReviews(List<Review> pReviews) {
		reviews.addAll(pReviews);
	}

	@Override
	public void processGetReviewsCallBack(List<ReviewBackendEntity> response) {
		ArrayList<Review> reviews = new ArrayList<>();
		for(ReviewBackendEntity rev: response) {
			reviews.add(new Review(rev));
		}
		setReviews(reviews);
		
	}
}
