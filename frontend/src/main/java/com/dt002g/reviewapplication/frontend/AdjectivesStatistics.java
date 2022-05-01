package com.dt002g.reviewapplication.frontend;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdjectivesStatistics {


    StringProperty adjective = new SimpleStringProperty("");
    ObjectProperty<Long> amount = new SimpleObjectProperty(0);
    ObjectProperty<Double> correlation = new SimpleObjectProperty<>(0.0);

    AdjectivesStatistics( String adjective, Long amount, Double correlation) {
        this.adjective.set(adjective);
        this.amount.set(amount);
        this.correlation.set(correlation);
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

    public Long getAmount() {
        return amount.get();
    }

    public ObjectProperty<Long> amountProperty() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount.set(amount);
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
