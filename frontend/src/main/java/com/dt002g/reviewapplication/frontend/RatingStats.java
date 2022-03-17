package com.dt002g.reviewapplication.frontend;

public class RatingStats {
	private int rating;
	private int amount;
	
	RatingStats(int rating, int amount){
		this.rating = rating;
		this.amount = amount;
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
