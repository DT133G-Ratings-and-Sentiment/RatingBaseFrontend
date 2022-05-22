package com.dt002g.reviewapplication.frontend.util;

public class StatisticResult {
    public double correlationCofficient;
    public double ratingExpectedValue;
    public double sentimentScoreExpectedValue;
    public double deviationRating;
    public double deviationSentimentScore;
    public double covarriance;
    public int ratingScaleStart;
    public int ratingScaleNumbers;
    public int getScoreScaleStart;
    public int scoreScaleNumbers;
    public double[] ratingPercentage;
    public double[] sentimentScorePercentage;
    public double[][] ratingSentimentScorePercentage;
    public double[][] ratingSentimentScoreAmount;
    public int[] ratingAmount;
    public int[] sentimentScoreAmount;
    public int totalRatings = 0;
    public double CV = 0;
    public double confidenceIntervalRating;
    public double confidenceIntervalSentiment;
    public double getRatingStandardDeviation(){
        return Math.sqrt(deviationRating);
    }
    public double getSentimentScoreStandardDeviation(){
        return Math.sqrt(deviationSentimentScore);
    };
}
