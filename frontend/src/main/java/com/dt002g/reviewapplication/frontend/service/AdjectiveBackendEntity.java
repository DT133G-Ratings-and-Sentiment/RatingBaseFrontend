package com.dt002g.reviewapplication.frontend.service;

public class AdjectiveBackendEntity {
    public int id = 0;
    public String Word = "";

    public AdjectiveBackendEntity(){}

    public AdjectiveBackendEntity(int id, String word){
        this.id = id;
        this.Word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }
}
