package com.dt002g.reviewapplication.frontend.service;

import com.dt002g.reviewapplication.frontend.RatingStats;

public class RatingBackendEntity {
	public int amount;
	public int rating;
	
	public RatingBackendEntity(){
		
	}
	
	public RatingBackendEntity(int rating, int amount){
		this.rating = rating;
		this.amount = amount;
	}
	
	public RatingBackendEntity(RatingStats ratingStats){
		this.amount = ratingStats.getAmount();
		this.rating = ratingStats.getRating();
	}
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
