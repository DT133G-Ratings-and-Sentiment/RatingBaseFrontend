package com.dt002g.reviewapplication.frontend.service;

import java.util.ArrayList;

public class AdjectiveReviewAmountAppearence {
    public String adjective;
    public int numberOfAppearancesPerReview;
    public ArrayList<SentimentStatisticsBackendEntity> reviewRatingByScoreMatrix;

    public AdjectiveReviewAmountAppearence(){}

    public AdjectiveReviewAmountAppearence(String adjective, int numberOfAppearancesPerReview, ArrayList<SentimentStatisticsBackendEntity> reviewRatingByScoreMatrix){
        this.adjective = adjective;
        this.numberOfAppearancesPerReview = numberOfAppearancesPerReview;
        reviewRatingByScoreMatrix = new ArrayList<>();
        this.reviewRatingByScoreMatrix.addAll(reviewRatingByScoreMatrix);
    }

    public String getAdjective() {
        return this.adjective;
    }

    public void setAdjective(String adjective) {
        this.adjective = adjective;
    }

    public int getNumberOfAppearancesPerReview() {
        return this.numberOfAppearancesPerReview;
    }

    public void setNumberOfAppearancesPerReview(int numberOfAppearancesPerReview) {
        this.numberOfAppearancesPerReview = numberOfAppearancesPerReview;
    }

    public ArrayList<SentimentStatisticsBackendEntity> getReviewRatingByScoreMatrix() {
        return reviewRatingByScoreMatrix;
    }

    public void setReviewRatingByScoreMatrix(ArrayList<SentimentStatisticsBackendEntity> sentimentStatisticsBackendEntityMatrix) {
        this.reviewRatingByScoreMatrix= sentimentStatisticsBackendEntityMatrix;
    }
}