package com.dt002g.reviewapplication.frontend.service;

import java.util.ArrayList;
import java.util.List;

public class ReviewsByAdjective {
    public String adjective;
    public List<ReviewBackendEntity> reviews= new ArrayList<>();

    public ReviewsByAdjective(){};

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
}
