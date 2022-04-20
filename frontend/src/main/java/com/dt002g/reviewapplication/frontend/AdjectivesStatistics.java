package com.dt002g.reviewapplication.frontend;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdjectivesStatistics {

    StringProperty ratingSpan = new SimpleStringProperty("");
    StringProperty sentiment = new SimpleStringProperty("");
    StringProperty adjective = new SimpleStringProperty("");
    ObjectProperty<Integer> amount = new SimpleObjectProperty(0);

    AdjectivesStatistics(String ratingSpan, String sentiment, String adjective, int amount) {
        this.ratingSpan.set(ratingSpan);
        this.sentiment.set(sentiment);
        this.adjective.set(adjective);
        this.amount.set(amount);
    }

    public String getRatingSpan() {
        return ratingSpan.get();
    }

    public StringProperty ratingSpanProperty() {
        return ratingSpan;
    }

    public void setRatingSpan(String ratingSpan) {
        this.ratingSpan.set(ratingSpan);
    }

    public String getSentiment() {
        return sentiment.get();
    }

    public StringProperty sentimentProperty() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment.set(sentiment);
    }

    public String getAdjective() {
        return adjective.get();
    }

    public StringProperty adjectiveProperty() {
        return adjective;
    }

    public void setAdjective(String adjective) {
        this.adjective.set(adjective);
    }

    public Integer getAmount() {
        return amount.get();
    }

    public ObjectProperty<Integer> amountProperty() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount.set(amount);
    }
}
