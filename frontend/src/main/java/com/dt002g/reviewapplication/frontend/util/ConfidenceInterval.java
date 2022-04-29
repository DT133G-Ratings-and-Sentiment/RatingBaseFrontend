package com.dt002g.reviewapplication.frontend.util;

public class ConfidenceInterval {
    public double sampleMean;
    public double width;
    public double min;
    public double max;
    double zValue;
    double standardDeviation;
    int sampleSize;

    public ConfidenceInterval(double sampleMean, double halfWidth, double zValue, double standardDeviation, int sampleSize){
        this.sampleMean = sampleMean;
        this.width = halfWidth *2;
        this.min = sampleMean -halfWidth;
        this.max = sampleMean +halfWidth;
        this.zValue = zValue;
        this.standardDeviation = standardDeviation;
        this.sampleSize = sampleSize;
    }
}
