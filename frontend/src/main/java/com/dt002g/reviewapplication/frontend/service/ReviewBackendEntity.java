package com.dt002g.reviewapplication.frontend.service;

import com.dt002g.reviewapplication.frontend.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewBackendEntity {
	
	public String comment = "";
	public long id;
	public int rating;
	public double normalisedAverageSentenceScore;
	public double normalisedMedianSentenceScore;
	public ArrayList<SentenceBackendEntity> sentences = new ArrayList<>();

	
	public ReviewBackendEntity(){
	}
	
	public ReviewBackendEntity(int rating, long id, String comment, List<SentenceBackendEntity> sentences){
		this.id = id;
		this.comment = comment;
		this.rating = rating;
		this.sentences.addAll(sentences);
	}
	
	public ReviewBackendEntity(Review review){
		this.comment = review.getFreeText();
		this.id = review.getId();
		this.rating = review.getRating();
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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

	public ArrayList<SentenceBackendEntity> getSentences() {
		return sentences;
	}

	public void setSentences(ArrayList<SentenceBackendEntity> sentences) {
		this.sentences = sentences;
	}

	public double getNormalisedAverageSentenceScore() {
		return normalisedAverageSentenceScore;
	}

	public void setNormalisedAverageSentenceScore(double normalisedAverageSentenceScore) {
		this.normalisedAverageSentenceScore = normalisedAverageSentenceScore;
	}

	public double getNormalisedMedianSentenceScore() {
		return normalisedMedianSentenceScore;
	}

	public void setNormalisedMedianSentenceScore(double normalisedMedianSentenceScore) {
		this.normalisedMedianSentenceScore = normalisedMedianSentenceScore;
	}
}
