package com.dt002g.reviewapplication.frontend;

import com.dt002g.reviewapplication.frontend.service.ReviewBackendEntity;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Review {
	//IntegerProperty id = new SimpleIntegerProperty(0);
	StringProperty freeText = new SimpleStringProperty("");
	//IntegerProperty rating = new SimpleIntegerProperty(0);
	ObjectProperty<Long> id = new SimpleObjectProperty<>(0L);
	ObjectProperty<Integer> rating = new SimpleObjectProperty<>(0);
	
	
	public Review() {}
	
	public Review(Long id, Integer rating, String freeText) {
		this.id.set(id);
		this.rating.set(rating);
		this.freeText.set(freeText);
	}
	
	public Review(ReviewBackendEntity reviewBackendEntity) {
		this.id.set(reviewBackendEntity.getId());
		this.rating.set(reviewBackendEntity.getRating());
		this.freeText.set(reviewBackendEntity.getComment());
	}
	
	public void setId(Long value) {
		this.id.set(value);
	}
	public Long getId() {
		return this.id.get();
	}
	public ObjectProperty<Long> idProperty(){
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
	
	@Override
	public String toString(){
		return "Id: " +id.toString() + ", Freetext: " + this.getFreeText() + " Rating: " + this.getRating();
	}
}
