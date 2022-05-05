package com.dt002g.reviewapplication.frontend.service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class AmountOfRatingsWithAMountOfScentences {
    ObjectProperty<Long> amountOfSentences = new SimpleObjectProperty(0);
    ObjectProperty<Long> amountOfRatings = new SimpleObjectProperty(0);

    public AmountOfRatingsWithAMountOfScentences(Long amountOfSentences, Long amountOfRatings){
        this.amountOfSentences.set(amountOfSentences);
        this.amountOfRatings.set(amountOfRatings);

    }

    public Long getAmountOfSentences() {
        return amountOfSentences.get();
    }

    public ObjectProperty<Long> amountOfSentencesProperty() {
        return amountOfSentences;
    }

    public void setAmountOfSentences(Long amount) {
        this.amountOfSentences.set(amount);
    }

    public Long getAmountOfRatings() {
        return amountOfRatings.get();
    }

    public ObjectProperty<Long> amountOfRatingsProperty() {
        return amountOfRatings;
    }

    public void setAmountOfRatings(Long amount) {
        this.amountOfRatings.set(amount);
    }
}
