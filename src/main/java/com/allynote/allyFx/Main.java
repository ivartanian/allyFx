package com.allynote.allyFx;/**
 * Created by super on 12/15/15.
 */

import com.allynote.allyFx.control.FXCalendarView;
import com.allynote.allyFx.control.FXMonthView;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        Pane pane = new Pane();

        FXCalendarView fxMonthView = new FXCalendarView();
        fxMonthView.setSelectedDate(LocalDate.now());
        fxMonthView.setShowWeekNumbers(true);
        fxMonthView.setShowMonthYearPane(true);
        pane.getChildren().addAll(fxMonthView);

        DatePicker datePicker = new DatePicker();
        fxMonthView.selectedDateProperty().bindBidirectional(datePicker.valueProperty());
//        pane.getChildren().addAll(fxMonthView, datePicker);

        root.setTop(datePicker);
        root.setCenter(pane);

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setScene(scene);


        primaryStage.show();
    }
}
