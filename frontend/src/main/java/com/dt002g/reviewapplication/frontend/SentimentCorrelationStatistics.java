package com.dt002g.reviewapplication.frontend;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SentimentCorrelationStatistics {
    StringProperty ratingSpan = new SimpleStringProperty("");
    StringProperty sentiment = new SimpleStringProperty("");
    ObjectProperty<Double> correlationPercent = new SimpleObjectProperty(0);
    ObjectProperty<Integer> numberOfCorrelations = new SimpleObjectProperty(0);
    ObjectProperty<Integer> totalReviews = new SimpleObjectProperty(0);

    SentimentCorrelationStatistics(String ratingSpan, String sentiment, double correlationPercent, int numberOfCorrelations, int totalReviews) {
        this.ratingSpan.set(ratingSpan);
        this.sentiment.set(sentiment);
        this.correlationPercent.set(correlationPercent);
        this.numberOfCorrelations.set(numberOfCorrelations);
        this.totalReviews.set(totalReviews);
    }

    public void setRatingSpan(String ratingSpan){
        this.ratingSpan.set(ratingSpan);
    }
    public void setSentiment(String sentiment){
        this.sentiment.set(sentiment);
    }
    public void setCorrelationPercent(Double correlationPercent){
        this.correlationPercent.set(correlationPercent);
    }
    public void setNumberOfCorrelations(Integer numberOfCorrelations){
        this.numberOfCorrelations.set(numberOfCorrelations);
    }
    public void setTotalReviews(Integer totalReviews){
        this.totalReviews.set(totalReviews);
    }
    public String getRatingSpan(){
        return this.ratingSpan.get();
    }
    public String getSentiment(){
        return this.sentiment.get();
    }
    public Double getCorrelationPercent(){
        return this.correlationPercent.get();
    }
    public Integer getNumberOfCorrelations(){
        return this.numberOfCorrelations.get();
    }
    public Integer getTotalReviews(){
        return this.totalReviews.get();
    }

    @Override
    public String toString(){
        return "Rating: " + this.getRatingSpan() + ", Sentiment: " + this.getSentiment() + ", Correlation %: "
                + this.getCorrelationPercent() + ", Correlation number: " + this.getNumberOfCorrelations() + ", Total: " + this.getTotalReviews();
    }

}

