package com.dt002g.reviewapplication.frontend.util;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.ExecutorService;

public class ProgressWindow {
    private final Stage dialogStage;
    private final ObservableList<String> failedList = FXCollections.observableArrayList();

    public ProgressWindow(Stage owner, CSVHandler handler, ExecutorService executorService) {
        System.out.println("Intialise ProgressWindow");
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(owner);
        Button stopButton = new Button("Stop");
        Button closeButton = new Button("Close");
        final HBox hb4 = new HBox();

        stopButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                CSVHandler.getInstance().cancel = true;
                executorService.shutdown();
                hb4.getChildren().removeAll(stopButton);
                hb4.getChildren().addAll(closeButton);
            }
        });

        closeButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            }
        });

        final Label numberOfParsedRowLabel = new Label();
        numberOfParsedRowLabel.setText("Number of Parsed rows:");
        Label numberOfParsedRowAmount = new Label();
        numberOfParsedRowAmount.setText("0");
        handler.numberOfParsedRows.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(
                        () -> {
                            numberOfParsedRowAmount.setText("" + newValue.intValue());
                        }
                );
            }
        });
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.getChildren().addAll(numberOfParsedRowLabel, numberOfParsedRowAmount);
        GridPane.setConstraints(hb, 1, 1);

        final Label numberOfExportedRowLabel = new Label();
        numberOfExportedRowLabel.setText("Number of Exported rows:");
        Label numberOfExportedRowAmount = new Label();
        numberOfExportedRowAmount.setText("0");
        handler.numberOfExportedRows.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(
                        () -> {
                            numberOfExportedRowAmount.setText("" + newValue.intValue());
                        }
                );
            }
        });

        final HBox hb2 = new HBox();
        hb2.setSpacing(5);
        hb2.setAlignment(Pos.CENTER_LEFT);
        hb2.getChildren().addAll(numberOfExportedRowLabel, numberOfExportedRowAmount);
        GridPane.setConstraints(hb2, 1, 2);

        final Label numberOfFailedRowLabel = new Label();
        numberOfFailedRowLabel.setText("Number of Failed rows:");
        Label numberOfFailedRowAmount = new Label();
        numberOfFailedRowAmount.setText("0");
        handler.numberOfFailedRows.addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(
                        () -> {
                            numberOfFailedRowAmount.setText("" + newValue.intValue());
                        }
                );
            }
        });

        final HBox hb3 = new HBox();
        hb3.setSpacing(5);
        hb3.setAlignment(Pos.CENTER_LEFT);
        hb3.getChildren().addAll(numberOfFailedRowLabel, numberOfFailedRowAmount);
        GridPane.setConstraints(hb3, 1, 3);

        final HBox hb5 = new HBox();
        hb5.setSpacing(5);
        hb5.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(hb5, 1, 4);
        TableView<String> table = new TableView<>();
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(300);
        TableColumn<String, String> failedColumn = new TableColumn<>("Failed rows");
        failedColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        failedColumn.setPrefWidth(300);
        table.getColumns().addAll(failedColumn);
        table.setItems(failedList);
        handler.failedLineProp.addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                failedList.add(newValue);
            }
        });

        hb5.getChildren().addAll(table);

        hb4.setSpacing(5);
        hb4.setAlignment(Pos.CENTER);
        hb4.getChildren().addAll(stopButton);
        GridPane.setConstraints(hb4, 1, 5);
        grid.getChildren().addAll(hb, hb2, hb3,hb5, hb4);

        Scene scene = new Scene(grid, 300,300);
        dialogStage.setScene(scene);
    }

    public void activate()  {
        System.out.println("Show ProgressWindow");
        dialogStage.show();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }
}
