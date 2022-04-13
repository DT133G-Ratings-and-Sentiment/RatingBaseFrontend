package com.dt002g.reviewapplication.frontend.service;

import java.util.List;


public class SentenceBackendEntity {

    Long id;
    String text;
    double veryPositive;
    double positive;
    double neutral;
    double negative;
    double veryNegative;
    int score;

    public SentenceBackendEntity() {

    }

    public SentenceBackendEntity(String text, List<Double> grades, int score) {
        this.text = text;
        this.score = score;
        if(grades.size() == 5) {
            this.veryPositive = grades.get(0);
            this.positive = grades.get(1);
            this.neutral = grades.get(2);
            this.negative = grades.get(3);
            this.veryNegative = grades.get(4);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getVeryPositive() {
        return veryPositive;
    }

    public void setVeryPositive(double veryPositive) {
        this.veryPositive = veryPositive;
    }

    public double getPositive() {
        return positive;
    }

    public void setPositive(double positive) {
        this.positive = positive;
    }

    public double getNeutral() {
        return neutral;
    }

    public void setNeutral(double neutral) {
        this.neutral = neutral;
    }

    public double getNegative() {
        return negative;
    }

    public void setNegative(double negative) {
        this.negative = negative;
    }

    public double getVeryNegative() {
        return veryNegative;
    }

    public void setVeryNegative(double veryNegative) {
        this.veryNegative = veryNegative;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
