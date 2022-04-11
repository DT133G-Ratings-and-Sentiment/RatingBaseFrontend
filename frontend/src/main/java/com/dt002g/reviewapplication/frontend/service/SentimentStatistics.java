package com.dt002g.reviewapplication.frontend.service;

import java.util.Map;

public class SentimentStatistics {
        private Map<Integer, Integer> sentimentMatrix;


       SentimentStatistics(Map<Integer, Integer> sentimentMatrix){
            this.sentimentMatrix = sentimentMatrix;
        }

        public Map<Integer, Integer> getSentimentMatrix() {
            return sentimentMatrix;
        }
        public void setRating(Map<Integer, Integer> sentimentMatrix) {
            this.sentimentMatrix = sentimentMatrix;
        }
}
