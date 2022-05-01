package com.dt002g.reviewapplication.frontend;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SentimentCorrelationStatistics {
    ObjectProperty<Double> correlationCoefficient = new SimpleObjectProperty(0);
    ObjectProperty<Double> standardDeviation = new SimpleObjectProperty(0);
    ObjectProperty<Double> confidenceInterval = new SimpleObjectProperty(0);


    SentimentCorrelationStatistics(double correlationCoefficient, double standardDeviation, double confidenceInterval) {
    this.correlationCoefficient.set(correlationCoefficient);
    this.standardDeviation.set(standardDeviation);
    this.confidenceInterval.set(confidenceInterval);
    }

    public Double getCorrelationCoefficient() {
        return correlationCoefficient.get();
    }

    public ObjectProperty<Double> correlationCoefficientProperty() {
        return correlationCoefficient;
    }

    public void setCorrelationCoefficient(Double correlationCoefficient) {
        this.correlationCoefficient.set(correlationCoefficient);
    }

    public Double getStandardDeviation() {
        return standardDeviation.get();
    }

    public ObjectProperty<Double> standardDeviationProperty() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation.set(standardDeviation);
    }

    public Double getConfidenceInterval() {
        return confidenceInterval.get();
    }

    public ObjectProperty<Double> confidenceIntervalProperty() {
        return confidenceInterval;
    }

    public void setConfidenceInterval(Double confidenceInterval) {
        this.confidenceInterval.set(confidenceInterval);
    }

    @Override
    public String toString() {
        return "SentimentCorrelationStatistics{" +
                "correlationCoefficient=" + correlationCoefficient +
                ", standardDeviation=" + standardDeviation +
                ", confidenceInterval=" + confidenceInterval +
                '}';
    }
}

