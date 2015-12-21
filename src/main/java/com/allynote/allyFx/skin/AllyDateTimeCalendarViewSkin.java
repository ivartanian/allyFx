package com.allynote.allyFx.skin;

import com.allynote.allyFx.behavior.AllyCalendarViewBehavior;
import com.allynote.allyFx.behavior.AllyDateTimeCalendarViewBehavior;
import com.allynote.allyFx.control.AllyCalendarView;
import com.allynote.allyFx.control.AllyDateTimeCalendarView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.Chronology;

import static java.time.temporal.ChronoUnit.*;


public class AllyDateTimeCalendarViewSkin extends AllyAbstractCalendarViewSkin<AllyDateTimeCalendarView, AllyDateTimeCalendarViewBehavior> {

    private HBox hBox;

    private Button backMonthButton;
    private Button forwardMonthButton;

//    private Label monthLabel;

    public AllyDateTimeCalendarViewSkin(final AllyDateTimeCalendarView allyCalendarView) {
        super(allyCalendarView, new AllyDateTimeCalendarViewBehavior(allyCalendarView));
    }

    @Override
    protected void extraConditions() {
        gridPane.prefWidthProperty().bind(hBox.widthProperty());
    }

    @Override
    protected void createComponents() {
        hBox = new HBox();
        hBox.getStyleClass().add("ally-calendar-view");

        //TODO: create time vizualization

        hBox.getChildren().add(createMonthSpinnerPane());

        getChildren().add(hBox);

    }

    protected BorderPane createMonthSpinnerPane() {

        BorderPane monthSpinnerPane = new BorderPane();
        monthSpinnerPane.getStyleClass().add("month-spinner-pane");

        // Month spinner

        backMonthButton = new Button();
        backMonthButton.getStyleClass().add("left-button");
        backMonthButton.prefHeightProperty().bind(monthSpinnerPane.heightProperty());

        forwardMonthButton = new Button();
        forwardMonthButton.getStyleClass().add("right-button");
        forwardMonthButton.prefHeightProperty().bind(monthSpinnerPane.heightProperty());

        StackPane leftMonthArrow = new StackPane();
        leftMonthArrow.getStyleClass().add("left-arrow");
        leftMonthArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        backMonthButton.setGraphic(leftMonthArrow);

        StackPane rightMonthArrow = new StackPane();
        rightMonthArrow.getStyleClass().add("right-arrow");
        rightMonthArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        forwardMonthButton.setGraphic(rightMonthArrow);


        backMonthButton.setOnAction(t -> {
            forward(-1, MONTHS, false);
        });


        forwardMonthButton.setOnAction(t -> {
            forward(1, MONTHS, false);
        });

        monthSpinnerPane.setLeft(backMonthButton);
        monthSpinnerPane.setCenter(gridPane);
        monthSpinnerPane.setRight(forwardMonthButton);


        return monthSpinnerPane;
    }

    protected void updateExtraPane(){
//        updateMonthPane();
    }

    protected void refresh() {
        updateDayNameCells();
        updateValues();
//        updateMonthLabelWidth();
    }

//    private void updateMonthLabelWidth() {
//        if (monthLabel != null) {
//            int monthsPerYear = getMonthsPerYear();
//            double width = 0;
//            for (int i = 0; i < monthsPerYear; i++) {
//                YearMonth yearMonth = displayedYearMonth.get().withMonth(i + 1);
//                String name = monthFormatterSO.withLocale(getLocale()).format(yearMonth);
//                if (Character.isDigit(name.charAt(0))) {
//                    // Fallback. The standalone format returned a number, so use standard format instead.
//                    name = monthFormatter.withLocale(getLocale()).format(yearMonth);
//                }
//                width = Math.max(width, computeTextWidth(monthLabel.getFont(), name, 0));
//            }
//            monthLabel.setMinWidth(width);
//        }
//    }


//    protected void updateMonthPane() {
//        YearMonth yearMonth = displayedYearMonth.get();
//        String str = formatMonth(yearMonth);
//        monthLabel.setText(str);
//
//        Chronology chrono = getSkinnable().getChronology();
//        LocalDate firstDayOfMonth = yearMonth.atDay(1);
//        backMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, DAYS));
//        forwardMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, MONTHS));
//    }

}
