package com.dt002g.reviewapplication.frontend.util;

import com.dt002g.reviewapplication.frontend.service.ReviewBackendAPIService;
import javafx.concurrent.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SendCSVFileTask  extends Task<Void> {
    ArrayList<String> data;
    String fileName;

    public SendCSVFileTask(ArrayList<String> data, String fileName){
        this.data = data;
        this.fileName = fileName;
    }

    @Override
    protected Void call() throws Exception {
        File myObj = new File(fileName);
        try {
            if (myObj.createNewFile()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                for(String s: data) {
                    writer.write(s + "\n");

                }
                writer.close();

                ReviewBackendAPIService.getInstance().uploadCSVFile(myObj);
            }
        } catch (IOException | SecurityException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
