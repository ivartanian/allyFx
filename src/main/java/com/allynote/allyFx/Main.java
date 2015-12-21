package com.allynote.allyFx;/**
 * Created by super on 12/15/15.
 */

import com.allynote.allyFx.control.AllyCalendarView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        Pane pane = new Pane();

        AllyCalendarView fxMonthView = new AllyCalendarView();
        fxMonthView.setShowWeekNumbers(true);
        pane.getChildren().addAll(fxMonthView);

        DatePicker datePicker = new DatePicker();
        fxMonthView.selectedValueProperty().bindBidirectional(datePicker.valueProperty());
//        pane.getChildren().addAll(fxMonthView, datePicker);

        root.setTop(datePicker);
        root.setCenter(fxMonthView);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setScene(scene);


        primaryStage.show();
    }
}
