package com.dt002g.reviewapplication.frontend.service;

import java.util.ArrayList;
import java.util.List;

public class ReviewsByAdjective {
    public String adjective;
    public List<ReviewBackendEntity> reviews= new ArrayList<>();
    public double correlationCoefficient = 0.0;
    public double standardDeviation = 0.0;
    public double moreThanOneCorrelation;
    public long moreThanOneAmount;
    public ReviewsByAdjective(){}

    public ReviewsByAdjective(String adjective, List<ReviewBackendEntity> reviews) {
        this.adjective = adjective;
        this.reviews.addAll(reviews);
    }

    public String getAdjective() {
        return adjective;
    }
    public void setAdjective(String adjective) {
        this.adjective = adjective;
    }
    public List<ReviewBackendEntity> getReviews() {
        return reviews;
    }
    public void setReviews(List<ReviewBackendEntity> reviews) {
        this.reviews = reviews;
    }

    public double getCorrelationCoefficient() {
        return correlationCoefficient;
    }

    public void setCorrelationCoefficient(double correlationCoefficient) {
        this.correlationCoefficient = correlationCoefficient;
    }
}
