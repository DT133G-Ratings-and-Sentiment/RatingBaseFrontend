package com.dt002g.reviewapplication.frontend.service;

import com.dt002g.reviewapplication.frontend.Review;

import java.util.List;

public class ReviewBackendEntity {
	
	public String freeText = "";
	public long id;
	public int rating;

	
	public ReviewBackendEntity(){
		
	}
	
	public ReviewBackendEntity(int rating, long id, String comment, List<SentenceBackendEntity> sentence){
		this.freeText = freeText;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
