package com.allynote.allyFx.skin;

import com.allynote.allyFx.behavior.AllyDesignCalendarViewBehavior;
import com.allynote.allyFx.control.AllyDesignCalendarView;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.*;
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

    private ComboBox<String> dayComboBox;
    private ComboBox<String> monthComboBox;
    private ComboBox<String> yearComboBox;

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

        hBox.getChildren().add(createComboBoxDatePane());

        hBox.getChildren().add(createDayMonthYearPane());

        hBox.getChildren().add(createGridSpinnerPane());

        getChildren().add(hBox);

    }

    protected HBox createComboBoxDatePane() {

        HBox dayMonthYearPane = new HBox();
        dayMonthYearPane.getStyleClass().add("combo-boxes-date-pane");

        // Day ComboBox

        dayComboBox = new ComboBox<>();
        dayComboBox.getStyleClass().add("day-combobox");

        // Month ComboBox

        monthComboBox = new ComboBox<>();
        monthComboBox.getStyleClass().add("month-combobox");

        // Year ComboBox

        yearComboBox = new ComboBox<>();
        yearComboBox.getStyleClass().add("year-combobox");

        dayMonthYearPane.getChildren().addAll(dayComboBox, monthComboBox, yearComboBox);

        return dayMonthYearPane;

    }

    protected VBox createDayMonthYearPane() {

        VBox dayMonthYearPane = new VBox();
        dayMonthYearPane.getStyleClass().add("day-month-year-pane");

        // Day spinner

        HBox daySpinner = new HBox();
        daySpinner.getStyleClass().add("day-month-spinner");

        dayLabel = new Label();
        dayLabel.getStyleClass().add("spinner-label");

        daySpinner.getChildren().add(dayLabel);
        dayMonthYearPane.getChildren().add(daySpinner);

        // Month spinner

        HBox monthSpinner = new HBox();
        monthSpinner.getStyleClass().add("day-month-spinner");

        monthLabel = new Label();
        monthLabel.getStyleClass().add("spinner-label");

        monthSpinner.getChildren().add(monthLabel);
        dayMonthYearPane.getChildren().addAll(monthSpinner);

        // Year spinner

        HBox yearSpinner = new HBox();
        yearSpinner.getStyleClass().add("year-spinner");

        yearLabel = new Label();
        yearLabel.getStyleClass().add("spinner-label");

        yearSpinner.getChildren().add(yearLabel);
        dayMonthYearPane.getChildren().addAll(yearSpinner);

        daySpinner.prefHeightProperty().bind(dayMonthYearPane.heightProperty());
        monthSpinner.prefHeightProperty().bind(dayMonthYearPane.heightProperty());
        yearSpinner.prefHeightProperty().bind(dayMonthYearPane.heightProperty());

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
        updateComboBoxDatePane();
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

    protected void updateComboBoxDatePane() {

        YearMonth yearMonth = displayedYearMonth.get();

        //format day
        LocalDate selectedValue = getSkinnable().getSelectedValue();
        if (selectedValue == null){
            selectedValue = yearMonth.atDay(1);
        }
        String str = formatDay(selectedValue);
        dayComboBox.getItems().add(str);
        double width = computeTextWidth(dayComboBox.getEditor().getFont(), str, 0);
        if (width > dayComboBox.getMinWidth()) {
            dayComboBox.setMinWidth(width * 5);
        }

        //format month
        str = formatMonth(yearMonth);
        monthComboBox.getItems().add(str);
        width = computeTextWidth(monthComboBox.getEditor().getFont(), str, 0);
        if (width > monthComboBox.getMinWidth()) {
            monthComboBox.setMinWidth(width * 2);
        }

        //format year
        str = formatYear(yearMonth);
        yearComboBox.getItems().add(str);
        width = computeTextWidth(yearComboBox.getEditor().getFont(), str, 0);
        if (width > yearComboBox.getMinWidth()) {
            yearComboBox.setMinWidth(width * 3);
        }

    }

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
