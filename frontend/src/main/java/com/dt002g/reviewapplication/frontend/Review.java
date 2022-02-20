package com.dt002g.reviewapplication.frontend;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.dt002g.reviewapplication.frontend.service.ReviewBackendEntity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Review {
	//IntegerProperty id = new SimpleIntegerProperty(0);
	StringProperty freeText = new SimpleStringProperty("");
	//IntegerProperty rating = new SimpleIntegerProperty(0);
	ObjectProperty<Integer> id = new SimpleObjectProperty<>(0);
	ObjectProperty<Integer> rating = new SimpleObjectProperty<>(0);
	
	
	public Review() {}
	
	public Review(Integer id, Integer rating, String freeText) {
		this.id.set(id);
		this.rating.set(rating);
		this.freeText.set(freeText);
	}
	
	public Review(ReviewBackendEntity reviewBackendEntity) {
		this.id.set(reviewBackendEntity.getId());
		this.rating.set(reviewBackendEntity.getRating());
		this.freeText.set(reviewBackendEntity.getFreeText());
	}
	
	public void setId(Integer value) {
		this.id.set(value);
	}
	
	public Integer getId() {
		return this.id.get();
	}
	
	public ObjectProperty<Integer> idProperty(){
		return id;
	}
	

	public StringProperty freeTextProperty() {
		return freeText;
	}
	public String getFreeText() {
		return freeText.get();
	}
	public void setFreeText(String freeText) {
		this.freeText.set(freeText);
	}
	public void setRating(Integer value) {
		this.rating.set(value);
	}
	
	public Integer getRating() {
		return this.rating.get();
	}
	
	public ObjectProperty<Integer> ratingProperty(){
		return rating;
	}
	
	
}
