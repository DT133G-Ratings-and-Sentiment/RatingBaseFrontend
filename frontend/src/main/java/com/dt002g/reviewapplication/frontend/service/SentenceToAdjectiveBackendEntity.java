package com.dt002g.reviewapplication.frontend.service;

import java.util.ArrayList;
import java.util.List;

public class SentenceToAdjectiveBackendEntity {
    public double numberOfOccurence = 0;
    public ArrayList<AdjectiveBackendEntity> adjectives = new ArrayList<>();

    public SentenceToAdjectiveBackendEntity(){

    }
    public SentenceToAdjectiveBackendEntity(double  numberOfOccurence, List<AdjectiveBackendEntity> adjectives){
        this.numberOfOccurence = numberOfOccurence;
        this.adjectives = new ArrayList<>();
        this.adjectives.addAll(adjectives);
    }
    public double getNumberOfOccurence() {
        return numberOfOccurence;
    }

    public void setNumberOfOccurence(double numberOfOccurence) {
        this.numberOfOccurence = numberOfOccurence;
    }

    public ArrayList<AdjectiveBackendEntity> getAdjectives() {
        return adjectives;
    }

    public void setAdjectives(ArrayList<AdjectiveBackendEntity> adjectives) {
        this.adjectives = adjectives;
    }
}
