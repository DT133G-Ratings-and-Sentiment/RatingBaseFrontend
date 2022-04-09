package com.dt002g.reviewapplication.frontend.util;

import javafx.beans.binding.NumberBinding;
import javafx.concurrent.Task;

import java.io.File;
import java.util.ArrayList;

public class CSVHandlerTask extends Task<Void> {
    ArrayList<String> neededHeaders;
    File file;
    int minRating;
    int maxRating;

    public CSVHandlerTask(ArrayList<String> neededHeaders, File file, int minRating, int maxRating){
        this.neededHeaders = neededHeaders;
        this.file = file;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    @Override
    protected Void call() throws Exception {
        CSVHandler.getInstance().parseCSVFileAndSend(neededHeaders, file, minRating, maxRating);
        return null;
    }

}
