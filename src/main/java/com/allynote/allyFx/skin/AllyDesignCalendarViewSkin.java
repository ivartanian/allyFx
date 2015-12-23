package com.allynote.allyFx.skin;

import com.allynote.allyFx.behavior.AllyDesignCalendarViewBehavior;
import com.allynote.allyFx.control.AllyDesignCalendarView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.*;


public class AllyDesignCalendarViewSkin extends AllyAbstractCalendarViewSkin<AllyDesignCalendarView, AllyDesignCalendarViewBehavior> {

    private HBox hBox;

    private Button backSpinnerButton;
    private Button forwardSpinnerButton;

    private Label dayLabel;
    private Label monthLabel;
    private Label yearLabel;

    private Button backDayButton;
    private Button forwardDayButton;
    private Button backMonthButton;
    private Button forwardMonthButton;
    private Button backYearButton;
    private Button forwardYearButton;


    public AllyDesignCalendarViewSkin(final AllyDesignCalendarView allyCalendarView) {
        super(allyCalendarView, new AllyDesignCalendarViewBehavior(allyCalendarView));
    }

    @Override
    protected void extraConditions() {
        gridPane.prefWidthProperty().bind(hBox.widthProperty());
    }

    @Override
    protected void createComponents() {
        hBox = new HBox();
        hBox.getStyleClass().add("ally-designcalendar-view");

        hBox.getChildren().add(createDayMonthYearPane());

        hBox.getChildren().add(createGridSpinnerPane());

        getChildren().add(hBox);

    }

    protected BorderPane createDayMonthYearPane() {

        BorderPane dayMonthYearPane = new BorderPane();
        dayMonthYearPane.getStyleClass().add("day-month-year-pane");

        // Day spinner

        HBox daySpinner = new HBox();
        daySpinner.getStyleClass().add("spinner");

        backDayButton = new Button();
        backDayButton.getStyleClass().add("left-button");

        forwardDayButton = new Button();
        forwardDayButton.getStyleClass().add("right-button");

        StackPane leftDayArrow = new StackPane();
        leftDayArrow.getStyleClass().add("left-arrow");
        leftDayArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        backDayButton.setGraphic(leftDayArrow);

        StackPane rightDayArrow = new StackPane();
        rightDayArrow.getStyleClass().add("right-arrow");
        rightDayArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        forwardDayButton.setGraphic(rightDayArrow);


        backDayButton.setOnAction(t -> {
            forward(-1, DAYS, false);
        });

        dayLabel = new Label();
        dayLabel.getStyleClass().add("spinner-label");

        forwardDayButton.setOnAction(t -> {
            forward(1, DAYS, false);
        });

        daySpinner.getChildren().addAll(backDayButton, dayLabel, forwardDayButton);
//        dayMonthYearPane.getChildren().addAll(daySpinner);
        dayMonthYearPane.setTop(daySpinner);

        // Month spinner

        HBox monthSpinner = new HBox();
        monthSpinner.getStyleClass().add("spinner");

        backMonthButton = new Button();
        backMonthButton.getStyleClass().add("left-button");

        forwardMonthButton = new Button();
        forwardMonthButton.getStyleClass().add("right-button");

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

        monthLabel = new Label();
        monthLabel.getStyleClass().add("spinner-label");

        forwardMonthButton.setOnAction(t -> {
            forward(1, MONTHS, false);
        });

        monthSpinner.getChildren().addAll(backMonthButton, monthLabel, forwardMonthButton);
//        dayMonthYearPane.getChildren().addAll(monthSpinner);
        dayMonthYearPane.setCenter(monthSpinner);

        // Year spinner

        HBox yearSpinner = new HBox();
        yearSpinner.getStyleClass().add("spinner");

        backYearButton = new Button();
        backYearButton.getStyleClass().add("left-button");

        forwardYearButton = new Button();
        forwardYearButton.getStyleClass().add("right-button");

        StackPane leftYearArrow = new StackPane();
        leftYearArrow.getStyleClass().add("left-arrow");
        leftYearArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        backYearButton.setGraphic(leftYearArrow);

        StackPane rightYearArrow = new StackPane();
        rightYearArrow.getStyleClass().add("right-arrow");
        rightYearArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        forwardYearButton.setGraphic(rightYearArrow);


        backYearButton.setOnAction(t -> {
            forward(-1, YEARS, false);
        });

        yearLabel = new Label();
        yearLabel.getStyleClass().add("spinner-label");

        forwardYearButton.setOnAction(t -> {
            forward(1, YEARS, false);
        });

        yearSpinner.getChildren().addAll(backYearButton, yearLabel, forwardYearButton);
        yearSpinner.setFillHeight(false);
//        dayMonthYearPane.getChildren().addAll(yearSpinner);
        dayMonthYearPane.setBottom(yearSpinner);

        return dayMonthYearPane;

    }

    protected BorderPane createGridSpinnerPane() {

        BorderPane monthSpinnerPane = new BorderPane();
        monthSpinnerPane.getStyleClass().add("calendar-spinner-pane");

        // Month spinner

        backSpinnerButton = new Button();
        backSpinnerButton.getStyleClass().add("left-button");
        backSpinnerButton.prefHeightProperty().bind(monthSpinnerPane.heightProperty());

        forwardSpinnerButton = new Button();
        forwardSpinnerButton.getStyleClass().add("right-button");
        forwardSpinnerButton.prefHeightProperty().bind(monthSpinnerPane.heightProperty());

        StackPane leftMonthArrow = new StackPane();
        leftMonthArrow.getStyleClass().add("left-arrow");
        leftMonthArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        backSpinnerButton.setGraphic(leftMonthArrow);

        StackPane rightMonthArrow = new StackPane();
        rightMonthArrow.getStyleClass().add("right-arrow");
        rightMonthArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        forwardSpinnerButton.setGraphic(rightMonthArrow);


        backSpinnerButton.setOnAction(t -> {
            forward(-1, MONTHS, false);
        });


        forwardSpinnerButton.setOnAction(t -> {
            forward(1, MONTHS, false);
        });

        monthSpinnerPane.setLeft(backSpinnerButton);
        monthSpinnerPane.setCenter(gridPane);
        monthSpinnerPane.setRight(forwardSpinnerButton);


        return monthSpinnerPane;
    }

    protected void updateExtraPane(){
        updateMonthPane();
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


    protected void updateMonthPane() {

        YearMonth yearMonth = displayedYearMonth.get();

        //format day
        LocalDate selectedValue = getSkinnable().getSelectedValue();
        if (selectedValue == null){
            selectedValue = yearMonth.atDay(1);
        }
        String str = formatDay(selectedValue);
        dayLabel.setText(str);
        double width = computeTextWidth(dayLabel.getFont(), str, 0);
        if (width > dayLabel.getMinWidth()) {
            dayLabel.setMinWidth(width);
        }

        //format month
        str = formatMonth(yearMonth);
        monthLabel.setText(str);
        width = computeTextWidth(monthLabel.getFont(), str, 0);
        if (width > monthLabel.getMinWidth()) {
            monthLabel.setMinWidth(width);
        }

        //format year
        str = formatYear(yearMonth);
        yearLabel.setText(str);
        width = computeTextWidth(yearLabel.getFont(), str, 0);
        if (width > yearLabel.getMinWidth()) {
            yearLabel.setMinWidth(width);
        }

        Chronology chrono = getSkinnable().getChronology();
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        backDayButton.setDisable(!isValidDate(chrono, selectedValue, -1, DAYS));
        forwardDayButton.setDisable(!isValidDate(chrono, selectedValue, +1, DAYS));
        backMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, DAYS));
        forwardMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, MONTHS));
        backYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, YEARS));
        forwardYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, YEARS));

    }

    protected String formatDay(LocalDate localDate) {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        try {
            ChronoLocalDate cDate = chrono.date(localDate);

            String str = dayCellFormatter.withLocale(getLocale())
                    .withChronology(chrono)
                    .format(cDate);
//            if (Character.isDigit(str.charAt(0))) {
//                // Fallback. The standalone format returned a number, so use standard format instead.
//                str = monthFormatter.withLocale(getLocale())
//                        .withChronology(chrono)
//                        .format(cDate);
//            }
            return str;
        } catch (DateTimeException ex) {
            // Date is out of range.
            return "";
        }
    }


}
