package com.allynote.allyFx.skin;

import com.allynote.allyFx.behavior.AllyCalendarViewBehavior;
import com.allynote.allyFx.control.AllyCalendarView;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.Chronology;

import static java.time.temporal.ChronoUnit.*;


public class AllyCalendarViewSkin extends AllyAbstractCalendarViewSkin<AllyCalendarView, AllyCalendarViewBehavior> {

    private VBox vBox;

    private Button backMonthButton;
    private Button forwardMonthButton;
    private Button backYearButton;
    private Button forwardYearButton;
    private Label monthLabel;
    private Label yearLabel;

    public AllyCalendarViewSkin(final AllyCalendarView allyCalendarView) {
        super(allyCalendarView, new AllyCalendarViewBehavior(allyCalendarView));
    }

    @Override
    protected void extraConditions() {
        gridPane.prefHeightProperty().bind(vBox.heightProperty());
    }

    @Override
    protected void createComponents() {
        vBox = new VBox();
        vBox.getStyleClass().add("ally-calendar-view");
        if (control.isShowMonthYearPane()) {
            vBox.getChildren().add(createMonthYearPane());
        }
        vBox.getChildren().add(gridPane);
        getChildren().add(vBox);

    }

    protected void updateExtraPane(){
        if (getSkinnable().isShowMonthYearPane()) {
            updateMonthYearPane();
        }
    }

    protected void refresh() {
        updateDayNameCells();
        updateValues();
        updateMonthLabelWidth();
    }

    private void updateMonthLabelWidth() {
        if (monthLabel != null) {
            int monthsPerYear = getMonthsPerYear();
            double width = 0;
            for (int i = 0; i < monthsPerYear; i++) {
                YearMonth yearMonth = displayedYearMonth.get().withMonth(i + 1);
                String name = monthFormatterSO.withLocale(getLocale()).format(yearMonth);
                if (Character.isDigit(name.charAt(0))) {
                    // Fallback. The standalone format returned a number, so use standard format instead.
                    name = monthFormatter.withLocale(getLocale()).format(yearMonth);
                }
                width = Math.max(width, computeTextWidth(monthLabel.getFont(), name, 0));
            }
            monthLabel.setMinWidth(width);
        }
    }

    protected BorderPane createMonthYearPane() {
        BorderPane monthYearPane = new BorderPane();
        monthYearPane.getStyleClass().add("month-year-pane");

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
        monthYearPane.setLeft(monthSpinner);

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
        monthYearPane.setRight(yearSpinner);

        return monthYearPane;
    }

    protected void updateMonthYearPane() {
        YearMonth yearMonth = displayedYearMonth.get();
        String str = formatMonth(yearMonth);
        monthLabel.setText(str);

        str = formatYear(yearMonth);
        yearLabel.setText(str);
        double width = computeTextWidth(yearLabel.getFont(), str, 0);
        if (width > yearLabel.getMinWidth()) {
            yearLabel.setMinWidth(width);
        }

        Chronology chrono = getSkinnable().getChronology();
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        backMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, DAYS));
        forwardMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, MONTHS));
        backYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, YEARS));
        forwardYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, YEARS));
    }

}
