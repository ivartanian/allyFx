package com.allynote.allyFx.skin;

import com.allynote.allyFx.behavior.AllyCalendarViewBehavior;
import com.allynote.allyFx.control.AllyAbstractCalendarView;
import com.allynote.allyFx.control.AllyCalendarView;
import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoUnit.*;


public abstract class AllyAbstractCalendarViewSkin extends BehaviorSkinBase<AllyCalendarView, AllyCalendarViewBehavior> {

    protected GridPane gridPane;
    protected final AllyCalendarView allyCalendarView;

    private int daysPerWeek;
    private List<DateCell> dayNameCells = new ArrayList<>();
    private List<DateCell> weekNumberCells = new ArrayList<>();
    private List<DateCell> dayCells = new ArrayList<>();
    private LocalDate[] dayCellDates;

    private DateCell lastFocusedDayCell = null;

    protected ObjectProperty<YearMonth> displayedYearMonth = new SimpleObjectProperty<>(this, "displayedYearMonth");

    public ObjectProperty<YearMonth> displayedYearMonthProperty() {
        return displayedYearMonth;
    }

    protected DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
    protected DateTimeFormatter monthFormatterSO = DateTimeFormatter.ofPattern("LLLL"); // Standalone month name
    protected DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("y");
    protected DateTimeFormatter yearWithEraFormatter = DateTimeFormatter.ofPattern("GGGGy"); // For Japanese. What to use for others??
    protected DateTimeFormatter weekNumberFormatter = DateTimeFormatter.ofPattern("w");
    protected DateTimeFormatter weekDayNameFormatter = DateTimeFormatter.ofPattern("ccc"); // Standalone day name
    protected DateTimeFormatter dayCellFormatter = DateTimeFormatter.ofPattern("d");

    public AllyAbstractCalendarViewSkin(final AllyCalendarView allyCalendarView) {

        super(allyCalendarView, new AllyCalendarViewBehavior(allyCalendarView));
        this.allyCalendarView = allyCalendarView;

        {
            LocalDate date = allyCalendarView.getSelectedValue();
            displayedYearMonth.set((date != null) ? YearMonth.from(date) : YearMonth.now());
        }

        initialize();

        createComponents();

        extraConditions();

        refresh();

    }

    private void initialize(){

        daysPerWeek = getDaysPerWeek();

        displayedYearMonth.addListener((observable, oldValue, newValue) -> {
            updateValues();
        });

        gridPane = new GridPane();
        gridPane.setFocusTraversable(true);
        gridPane.getStyleClass().add("calendar-grid");
        gridPane.setVgap(-1);
        gridPane.setHgap(-1);

        // get the weekday labels starting with the weekday that is the
        // first-day-of-the-week according to the locale in the
        // displayed LocalDate
        for (int i = 0; i < daysPerWeek; i++) {
            DateCell cell = getNewDateCell();
            cell.getStyleClass().add("day-name-cell");
            dayNameCells.add(cell);
        }

        // Week number column
        for (int i = 0; i < 6; i++) {
            DateCell cell = getNewDateCell();
            cell.getStyleClass().add("week-number-cell");
            weekNumberCells.add(cell);
        }

        createDayCells();
        updateGrid();
    }

    protected abstract void createComponents();

    protected abstract void extraConditions();

    protected abstract void refresh();

    protected int getDaysPerWeek() {
        ValueRange range = getPrimaryChronology().range(DAY_OF_WEEK);
        return (int) (range.getMaximum() - range.getMinimum() + 1);
    }

    protected void updateValues() {
        updateWeeknumberDateCells();
        updateDayCells();
        updateExtraPane();
    }

    protected void updateExtraPane(){

    }

    protected void updateWeeknumberDateCells() {
        if (getSkinnable().isShowWeekNumbers()) {
            final Locale locale = getLocale();
            final int maxWeeksPerMonth = 6; // TODO: Get this from chronology?

            LocalDate firstOfMonth = displayedYearMonth.get().atDay(1);
            for (int i = 0; i < maxWeeksPerMonth; i++) {
                LocalDate date = firstOfMonth.plus(i, WEEKS);
                // Use a formatter to ensure correct localization,
                // such as when Thai numerals are required.
                String cellText = weekNumberFormatter.withLocale(locale)
                        .withDecimalStyle(DecimalStyle.of(locale))
                        .format(date);
                weekNumberCells.get(i).setText(cellText);
            }
        }
    }

    /**
     * update values
     */
    protected void updateDayCells() {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        int firstOfMonthIdx = determineFirstOfMonthDayOfWeek();
        YearMonth curMonth = displayedYearMonth.get();

        // RT-31075: The following are now set in the try-catch block.
        YearMonth prevMonth = null;
        YearMonth nextMonth = null;
        int daysInCurMonth = -1;
        int daysInPrevMonth = -1;
        int daysInNextMonth = -1;

        for (int i = 0; i < 6 * daysPerWeek; i++) {
            DateCell dayCell = dayCells.get(i);
            dayCell.getStyleClass().setAll("cell", "date-cell", "day-cell");
            dayCell.setDisable(false);
            dayCell.setStyle(null);
            dayCell.setGraphic(null);
            dayCell.setTooltip(null);

            try {
                if (daysInCurMonth == -1) {
                    daysInCurMonth = curMonth.lengthOfMonth();
                }
                YearMonth month = curMonth;
                int day = i - firstOfMonthIdx + 1;
                //int index = firstOfMonthIdx + i - 1;
                if (i < firstOfMonthIdx) {
                    if (prevMonth == null) {
                        prevMonth = curMonth.minusMonths(1);
                        daysInPrevMonth = prevMonth.lengthOfMonth();
                    }
                    month = prevMonth;
                    day = i + daysInPrevMonth - firstOfMonthIdx + 1;
                    dayCell.getStyleClass().add("previous-month");
                } else if (i >= firstOfMonthIdx + daysInCurMonth) {
                    if (nextMonth == null) {
                        nextMonth = curMonth.plusMonths(1);
                        daysInNextMonth = nextMonth.lengthOfMonth();
                    }
                    month = nextMonth;
                    day = i - daysInCurMonth - firstOfMonthIdx + 1;
                    dayCell.getStyleClass().add("next-month");
                }
                LocalDate date = month.atDay(day);
                dayCellDates[i] = date;
                ChronoLocalDate cDate = chrono.date(date);

                dayCell.setDisable(false);

                if (isToday(date)) {
                    dayCell.getStyleClass().add("today");
                }

                if (date.equals(getSkinnable().getSelectedValue())) {
                    dayCell.getStyleClass().add("selected");
                }

                String cellText = dayCellFormatter.withLocale(locale)
                        .withChronology(chrono)
                        .withDecimalStyle(DecimalStyle.of(locale))
                        .format(cDate);
                dayCell.setText(cellText);

                dayCell.updateItem(date, false);
            } catch (DateTimeException ex) {
                // Date is out of range.
                // System.err.println(dayCellDate(dayCell) + " " + ex);
                dayCell.setText(" ");
                dayCell.setDisable(true);
            }
        }
    }

    /**
     * determine on which day of week idx the first of the months is
     */
    private int determineFirstOfMonthDayOfWeek() {
        // determine with which cell to start
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();
        int firstOfMonthIdx = displayedYearMonth.get().atDay(1).getDayOfWeek().getValue() - firstDayOfWeek;
        if (firstOfMonthIdx < 0) {
            firstOfMonthIdx += daysPerWeek;
        }
        return firstOfMonthIdx;
    }

    protected void createDayCells() {
        final EventHandler<MouseEvent> dayCellActionHandler = ev -> {
            if (ev.getButton() != MouseButton.PRIMARY) {
                return;
            }

            DateCell dayCell = (DateCell) ev.getSource();
            selectDayCell(dayCell);
            lastFocusedDayCell = dayCell;
        };

        getSkinnable().selectedValueProperty().addListener(this::onSelectedDateChanged);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < daysPerWeek; col++) {
                DateCell dayCell = createDayCell();
                dayCell.addEventHandler(MouseEvent.MOUSE_CLICKED, dayCellActionHandler);
                dayCells.add(dayCell);
            }
        }

        dayCellDates = new LocalDate[6 * daysPerWeek];
    }

    public void selectDayCell(DateCell dateCell) {
        getSkinnable().setSelectedValue(dayCellDate(dateCell));
    }

    private DateCell createDayCell() {
        DateCell cell = null;
        if (getSkinnable().getDayCellFactory() != null) {
            cell = getSkinnable().getDayCellFactory().call(getSkinnable());
        }
        if (cell == null) {
            cell = getNewDateCell();
        }

        return cell;
    }

    private DateCell getNewDateCell() {
        DateCell cell = new DateCell();
        cell.setMaxHeight(Double.MAX_VALUE);
        return cell;
    }

    protected LocalDate dayCellDate(DateCell dateCell) {
        assert (dayCellDates != null);
        return dayCellDates[dayCells.indexOf(dateCell)];
    }

    private boolean isToday(LocalDate localDate) {
        return (localDate.equals(LocalDate.now()));
    }

    void updateGrid() {
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();

        int nCols = daysPerWeek + (getSkinnable().isShowWeekNumbers() ? 1 : 0);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100); // Treated as weight
        for (int i = 0; i < nCols; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100); // Treated as height
        for (int i = 0; i < 7; i++) {
            gridPane.getRowConstraints().add(i, rowConstraints);
        }

        for (int i = 0; i < daysPerWeek; i++) {
            gridPane.add(dayNameCells.get(i), i + nCols - daysPerWeek, 0);  // col, row
        }

        // Week number column
        if (getSkinnable().isShowWeekNumbers()) {
            for (int i = 0; i < 6; i++) {
                gridPane.add(weekNumberCells.get(i), 0, i + 1);  // col, row
            }
        }

        // setup: 6 rows of daysPerWeek (which is the maximum number of cells required in the worst case layout)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < daysPerWeek; col++) {
                gridPane.add(dayCells.get(row * daysPerWeek + col), col + nCols - daysPerWeek, row + 1);
            }
        }
    }

    protected int getMonthsPerYear() {
        ValueRange range = getPrimaryChronology().range(MONTH_OF_YEAR);
        return (int) (range.getMaximum() - range.getMinimum() + 1);
    }

    protected double computeTextWidth(Font font, String text, double wrappingWidth) {
        Text layout = new Text(text != null ? text : "");
        layout.setWrappingWidth(wrappingWidth);
        layout.setFont(font);

        return layout.getLayoutBounds().getWidth();
    }

    protected void updateDayNameCells() {
        // first day of week, 1 = monday, 7 = sunday
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();

        // july 13th 2009 is a Monday, so a firstDayOfWeek=1 must come out of the 13th
        LocalDate date = LocalDate.of(2009, 7, 12 + firstDayOfWeek);
        for (int i = 0; i < daysPerWeek; i++) {
            String name = weekDayNameFormatter.withLocale(getLocale()).format(date.plus(i, DAYS));
            dayNameCells.get(i).setText(titleCaseWord(name));
        }
    }

    protected void forward(int offset, ChronoUnit unit, boolean focusDayCell) {
        YearMonth yearMonth = displayedYearMonth.get();
        DateCell dateCell = lastFocusedDayCell;
        if (dateCell == null || !dayCellDate(dateCell).getMonth().equals(yearMonth.getMonth())) {
            dateCell = findDayCellForDate(yearMonth.atDay(1));
        }
        goToDayCell(dateCell, offset, unit, focusDayCell);
    }

    protected DateCell findDayCellForDate(LocalDate date) {
        for (int i = 0; i < dayCellDates.length; i++) {
            if (date.equals(dayCellDates[i])) {
                return dayCells.get(i);
            }
        }
        return dayCells.get(dayCells.size() / 2 + 1);
    }

    protected void goToDayCell(DateCell dateCell, int offset, ChronoUnit unit, boolean focusDayCell) {
        goToDate(dayCellDate(dateCell).plus(offset, unit), focusDayCell);
    }

    private void goToDate(LocalDate date, boolean focusDayCell) {
        if (isValidDate(getSkinnable().getChronology(), date)) {
            displayedYearMonth.set(YearMonth.from(date));
            if (focusDayCell) {
                findDayCellForDate(date).requestFocus();
            }
        }
    }

    protected boolean isValidDate(Chronology chrono, LocalDate date) {
        try {
            if (date != null) {
                chrono.date(date);
            }
            return true;
        } catch (DateTimeException ex) {
            return false;
        }
    }

    protected boolean isValidDate(Chronology chrono, LocalDate date, int offset, ChronoUnit unit) {
        if (date != null) {
            try {
                return isValidDate(chrono, date.plus(offset, unit));
            } catch (DateTimeException ex) {
            }
        }
        return false;
    }

    protected String formatMonth(YearMonth yearMonth) {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        try {
            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));

            String str = monthFormatterSO.withLocale(getLocale())
                    .withChronology(chrono)
                    .format(cDate);
            if (Character.isDigit(str.charAt(0))) {
                // Fallback. The standalone format returned a number, so use standard format instead.
                str = monthFormatter.withLocale(getLocale())
                        .withChronology(chrono)
                        .format(cDate);
            }
            return titleCaseWord(str);
        } catch (DateTimeException ex) {
            // Date is out of range.
            return "";
        }
    }

    protected String formatYear(YearMonth yearMonth) {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        try {
            DateTimeFormatter formatter = yearFormatter;
            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));
            int era = cDate.getEra().getValue();
            int nEras = chrono.eras().size();

            /*if (cDate.get(YEAR) < 0) {
                formatter = yearForNegYearFormatter;
            } else */
            if ((nEras == 2 && era == 0) || nEras > 2) {
                formatter = yearWithEraFormatter;
            }

            // Fixme: Format Japanese era names with Japanese text.
            String str = formatter.withLocale(getLocale())
                    .withChronology(chrono)
                    .withDecimalStyle(DecimalStyle.of(getLocale()))
                    .format(cDate);

            return str;
        } catch (DateTimeException ex) {
            // Date is out of range.
            return "";
        }
    }

    protected Locale getLocale() {
        return Locale.getDefault(Locale.Category.FORMAT);
    }

    protected Chronology getPrimaryChronology() {
        return getSkinnable().getChronology();
    }

    // Ensures that month and day names are titlecased (capitalized).
    private String titleCaseWord(String str) {
        if (str.length() > 0) {
            int firstChar = str.codePointAt(0);
            if (!Character.isTitleCase(firstChar)) {
                str = new String(new int[]{Character.toTitleCase(firstChar)}, 0, 1) +
                        str.substring(Character.offsetByCodePoints(str, 0, 1));
            }
        }
        return str;
    }

    protected void onSelectedDateChanged(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
        if (newValue != null) {
            getSkinnable().setSelectedValue(newValue);
            goToDate(newValue, true);
            updateValues();
        }
    }

}
