package com.dt002g.reviewapplication.frontend;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdjectiveAmountStatistics {
    StringProperty adjective = new SimpleStringProperty("");
    ObjectProperty<Integer> numberOfAppearencesInReviews = new SimpleObjectProperty(0);
    ObjectProperty<Integer> amountOfReviews = new SimpleObjectProperty(0);
    ObjectProperty<Double> correlation = new SimpleObjectProperty<>(0.0);


    /*public Double getPercent() {
        return percent.get();
    }

    public ObjectProperty<Double> percentProperty() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent.set(percent);
    }

    public ObjectProperty<Double> percent = new SimpleObjectProperty<>(0.0);*/

    public AdjectiveAmountStatistics( String adjective, Integer numberOfAppearencesInReviews, Integer amountOfReviews, Double correlation) {
        this.adjective.set(adjective);
        this.amountOfReviews.set(amountOfReviews);
        this.correlation.set(correlation);
        this.numberOfAppearencesInReviews.set(numberOfAppearencesInReviews);
        //this.percent.set(percent);
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

    public Integer getNumberOfAppearencesInReviews() {
        return numberOfAppearencesInReviews.get();
    }

    public ObjectProperty<Integer> numberOfAppearencesInReviewsProperty() {
        return numberOfAppearencesInReviews;
    }

    public void setNumberOfAppearencesInReviews(Integer numberOfAppearencesInReviews) {
        this.numberOfAppearencesInReviews.set(numberOfAppearencesInReviews);
    }


    public Integer getAmountOfReviews() {
        return amountOfReviews.get();
    }

    public ObjectProperty<Integer> amountOfReviewsProperty() {
        return amountOfReviews;
    }

    public void setAmountOfReviews(Integer amount) {
        this.amountOfReviews.set(amount);
    }

    public Double getCorrelation() {
        return correlation.get();
    }

    public ObjectProperty<Double> correlationProperty() {
        return correlation;
    }

    public void setCorrelation(Double correlation) {
        this.correlation.set(correlation);
    }
}
