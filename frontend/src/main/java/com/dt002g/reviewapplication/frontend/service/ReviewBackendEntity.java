package com.dt002g.reviewapplication.frontend.service;

import com.dt002g.reviewapplication.frontend.Review;

public class ReviewBackendEntity {
	
	public String freeText = "";
	public int id;
	public int rating;
	
	public ReviewBackendEntity(){
		
	}
	
	public ReviewBackendEntity(String freeText, int id, int rating){
		this.freeText = freeText;
		this.id = id;
		this.rating = rating;
	}
	
	public ReviewBackendEntity(Review review){
		this.freeText = review.getFreeText();
		this.id = review.getId();
		this.rating = review.getRating();
	}
	
	public String getFreeText() {
		return freeText;
	}
	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
