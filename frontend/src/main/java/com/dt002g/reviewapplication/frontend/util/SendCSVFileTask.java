package com.dt002g.reviewapplication.frontend.util;

import com.dt002g.reviewapplication.frontend.service.ReviewBackendAPIService;
import com.dt002g.reviewapplication.frontend.service.UploadCSVFileCallBack;
import javafx.concurrent.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SendCSVFileTask  extends Task<Void> {
    ArrayList<String> data;
    String fileName;
    UploadCSVFileCallBack uploadCSVFileCallBack;

    public SendCSVFileTask(ArrayList<String> data, String fileName, UploadCSVFileCallBack uploadCSVFileCallBack){
        this.data = data;
        this.fileName = fileName;
        this.uploadCSVFileCallBack = uploadCSVFileCallBack;
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

                ReviewBackendAPIService.getInstance().uploadCSVFile(myObj, uploadCSVFileCallBack, data.size());
            }
        } catch (IOException | SecurityException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
