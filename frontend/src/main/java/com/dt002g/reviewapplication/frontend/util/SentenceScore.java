package com.dt002g.reviewapplication.frontend.util;

public class SentenceScore {
	private String sentence = "";
	private SentimentScore sentimentScore;
	
	public SentenceScore(String sentence, SentimentScore sentimentScore) {
		this.sentence = sentence;
		this.sentimentScore = sentimentScore;
	}
	
	public String getSentence() {
		return sentence;
	}
	
	public double getVeryPositive() {
		return sentimentScore.getVeryPositive();
	}
	
	public double getPositive() {
		return sentimentScore.getPositive();
	}
	
	public double getNeutral() {
		return sentimentScore.getNeutral();
	}
	
	public double getNegative() {
		return sentimentScore.getNegative();
	}

	public double getVeryNegative() {
		return sentimentScore.getVeryNegative();
	}
		
	public String getSentimentType() {
		return sentimentScore.getSentimentType();
	}

}
