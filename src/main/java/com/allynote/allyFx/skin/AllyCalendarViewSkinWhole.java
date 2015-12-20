//package com.allynote.allyFx.skin;
//
//import com.allynote.allyFx.behavior.AllyCalendarViewBehavior;
//import com.allynote.allyFx.control.AllyCalendarView;
//import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
//import javafx.beans.property.ObjectProperty;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.beans.value.ObservableValue;
//import javafx.event.EventHandler;
//import javafx.scene.control.Button;
//import javafx.scene.control.DateCell;
//import javafx.scene.control.Label;
//import javafx.scene.input.MouseButton;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.*;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//
//import java.time.DateTimeException;
//import java.time.LocalDate;
//import java.time.YearMonth;
//import java.time.chrono.ChronoLocalDate;
//import java.time.chrono.Chronology;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DecimalStyle;
//import java.time.temporal.ChronoUnit;
//import java.time.temporal.ValueRange;
//import java.time.temporal.WeekFields;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import static java.time.temporal.ChronoField.DAY_OF_WEEK;
//import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
//import static java.time.temporal.ChronoUnit.*;
//
//
//public class AllyCalendarViewSkinWhole extends BehaviorSkinBase<AllyCalendarView, AllyCalendarViewBehavior> {
//
//    private VBox vBox;
//
//    private Button backMonthButton;
//    private Button forwardMonthButton;
//    private Button backYearButton;
//    private Button forwardYearButton;
//    private Label monthLabel;
//    private Label yearLabel;
//    protected GridPane gridPane;
//
//    private int daysPerWeek;
//    private List<DateCell> dayNameCells = new ArrayList<>();
//    private List<DateCell> weekNumberCells = new ArrayList<>();
//    protected List<DateCell> dayCells = new ArrayList<>();
//    private LocalDate[] dayCellDates;
//    private DateCell lastFocusedDayCell = null;
//
//    private ObjectProperty<YearMonth> displayedYearMonth = new SimpleObjectProperty<>(this, "displayedYearMonth");
//
//    public ObjectProperty<YearMonth> displayedYearMonthProperty() {
//        return displayedYearMonth;
//    }
//
//    final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM");
//
//    final DateTimeFormatter monthFormatterSO = DateTimeFormatter.ofPattern("LLLL"); // Standalone month name
//
//    final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("y");
//
//    final DateTimeFormatter yearWithEraFormatter = DateTimeFormatter.ofPattern("GGGGy"); // For Japanese. What to use for others??
//
//    final DateTimeFormatter weekNumberFormatter = DateTimeFormatter.ofPattern("w");
//
//    final DateTimeFormatter weekDayNameFormatter = DateTimeFormatter.ofPattern("ccc"); // Standalone day name
//
//    final DateTimeFormatter dayCellFormatter = DateTimeFormatter.ofPattern("d");
//
//    public AllyCalendarViewSkinWhole(final AllyCalendarView allyCalendarView) {
//
//        super(allyCalendarView, new AllyCalendarViewBehavior(allyCalendarView));
//
//        vBox = new VBox();
//
//        vBox.getStyleClass().add("ally-calendar-view");
//
//        daysPerWeek = getDaysPerWeek();
//
//        {
//            LocalDate date = allyCalendarView.getSelectedValue();
//            displayedYearMonth.set((date != null) ? YearMonth.from(date) : YearMonth.now());
//        }
//
//        displayedYearMonth.addListener((observable, oldValue, newValue) -> {
//            updateValues();
//        });
//
//        if (allyCalendarView.isShowMonthYearPane()) {
//            vBox.getChildren().add(createMonthYearPane());
//        }
//
//        gridPane = new GridPane()
//        {
//			@Override
//			protected double computePrefWidth(double height) {
//				final double width = super.computePrefWidth(height);
//
//				// RT-30903: Make sure width snaps to pixel when divided by
//				// number of columns. GridPane doesn't do this with percentage
//				// width constraints. See GridPane.adjustColumnWidths().
//				final int nCols = daysPerWeek + (allyCalendarView.isShowWeekNumbers() ? 1 : 0);
//				final double snaphgap = snapSpace(getHgap());
//				final double left = snapSpace(getInsets().getLeft());
//				final double right = snapSpace(getInsets().getRight());
//				final double hgaps = snaphgap * (nCols - 1);
//				final double contentWidth = width - left - right - hgaps;
//				return ((snapSize(contentWidth / nCols)) * nCols) + left + right + hgaps;
//			}
//
////            @Override
////            protected double computePrefHeight(double width) {
////                final double height = super.computePrefHeight(width);
////
////                final int nRows = 6 + 1 + (allyCalendarView.isShowMonthYearPane() ? 1 : 0);
////                final double snapvgap = snapSpace(getVgap());
////                final double top = snapSpace(getInsets().getTop());
////                final double bottom = snapSpace(getInsets().getBottom());
////                final double vgaps = snapvgap * (nRows - 1);
////                final double contentHeight = height - top - bottom - vgaps;
////                return ((snapSize(contentHeight / nRows)) * nRows) + top + bottom + vgaps;
////
////            }
//
//            @Override
//			protected void layoutChildren() {
//				// Prevent AssertionError in GridPane
//				if (getWidth() > 0 && getHeight() > 0) {
//					super.layoutChildren();
//				}
//			}
//		};
//
//		gridPane.setFocusTraversable(true);
//        gridPane.getStyleClass().add("calendar-grid");
//        gridPane.setVgap(-1);
//        gridPane.setHgap(-1);
//        gridPane.prefHeightProperty().bind(vBox.heightProperty());
//
//
//        // get the weekday labels starting with the weekday that is the
//        // first-day-of-the-week according to the locale in the
//        // displayed LocalDate
//        for (int i = 0; i < daysPerWeek; i++) {
//            DateCell cell = getNewDateCell();
//            cell.getStyleClass().add("day-name-cell");
//            dayNameCells.add(cell);
//        }
//
//        // Week number column
//        for (int i = 0; i < 6; i++) {
//            DateCell cell = getNewDateCell();
//            cell.getStyleClass().add("week-number-cell");
//            weekNumberCells.add(cell);
//        }
//
//        createDayCells();
//        updateGrid();
//
//        vBox.getChildren().add(gridPane);
//        getChildren().add(vBox);
//
//        refresh();
//
//    }
//
//    private int getDaysPerWeek() {
//        ValueRange range = getPrimaryChronology().range(DAY_OF_WEEK);
//        return (int) (range.getMaximum() - range.getMinimum() + 1);
//    }
//
//    void updateValues() {
//        // Note: Preserve this order, as DatePickerHijrahContent needs
//        // updateDayCells before updateMonthYearPane().
//        updateWeeknumberDateCells();
//        updateDayCells();
//        if (getSkinnable().isShowMonthYearPane()) {
//            updateMonthYearPane();
//        }
//    }
//
//    void updateWeeknumberDateCells() {
//        if (getSkinnable().isShowWeekNumbers()) {
//            final Locale locale = getLocale();
//            final int maxWeeksPerMonth = 6; // TODO: Get this from chronology?
//
//            LocalDate firstOfMonth = displayedYearMonth.get().atDay(1);
//            for (int i = 0; i < maxWeeksPerMonth; i++) {
//                LocalDate date = firstOfMonth.plus(i, WEEKS);
//                // Use a formatter to ensure correct localization,
//                // such as when Thai numerals are required.
//                String cellText = weekNumberFormatter.withLocale(locale)
//                        .withDecimalStyle(DecimalStyle.of(locale))
//                        .format(date);
//                weekNumberCells.get(i).setText(cellText);
//            }
//        }
//    }
//
//    /**
//     * update values
//     */
//    void updateDayCells() {
//        Locale locale = getLocale();
//        Chronology chrono = getPrimaryChronology();
//        int firstOfMonthIdx = determineFirstOfMonthDayOfWeek();
//        YearMonth curMonth = displayedYearMonth.get();
//
//        // RT-31075: The following are now set in the try-catch block.
//        YearMonth prevMonth = null;
//        YearMonth nextMonth = null;
//        int daysInCurMonth = -1;
//        int daysInPrevMonth = -1;
//        int daysInNextMonth = -1;
//
//        for (int i = 0; i < 6 * daysPerWeek; i++) {
//            DateCell dayCell = dayCells.get(i);
//            dayCell.getStyleClass().setAll("cell", "date-cell", "day-cell");
//            dayCell.setDisable(false);
//            dayCell.setStyle(null);
//            dayCell.setGraphic(null);
//            dayCell.setTooltip(null);
//
//            try {
//                if (daysInCurMonth == -1) {
//                    daysInCurMonth = curMonth.lengthOfMonth();
//                }
//                YearMonth month = curMonth;
//                int day = i - firstOfMonthIdx + 1;
//                //int index = firstOfMonthIdx + i - 1;
//                if (i < firstOfMonthIdx) {
//                    if (prevMonth == null) {
//                        prevMonth = curMonth.minusMonths(1);
//                        daysInPrevMonth = prevMonth.lengthOfMonth();
//                    }
//                    month = prevMonth;
//                    day = i + daysInPrevMonth - firstOfMonthIdx + 1;
//                    dayCell.getStyleClass().add("previous-month");
//                } else if (i >= firstOfMonthIdx + daysInCurMonth) {
//                    if (nextMonth == null) {
//                        nextMonth = curMonth.plusMonths(1);
//                        daysInNextMonth = nextMonth.lengthOfMonth();
//                    }
//                    month = nextMonth;
//                    day = i - daysInCurMonth - firstOfMonthIdx + 1;
//                    dayCell.getStyleClass().add("next-month");
//                }
//                LocalDate date = month.atDay(day);
//                dayCellDates[i] = date;
//                ChronoLocalDate cDate = chrono.date(date);
//
//                dayCell.setDisable(false);
//
//                if (isToday(date)) {
//                    dayCell.getStyleClass().add("today");
//                }
//
//                if (date.equals(getSkinnable().getSelectedValue())) {
//                    dayCell.getStyleClass().add("selected");
//                }
//
//                String cellText = dayCellFormatter.withLocale(locale)
//                        .withChronology(chrono)
//                        .withDecimalStyle(DecimalStyle.of(locale))
//                        .format(cDate);
//                dayCell.setText(cellText);
//
//                dayCell.updateItem(date, false);
//            } catch (DateTimeException ex) {
//                // Date is out of range.
//                // System.err.println(dayCellDate(dayCell) + " " + ex);
//                dayCell.setText(" ");
//                dayCell.setDisable(true);
//            }
//        }
//    }
//
//    /**
//     * determine on which day of week idx the first of the months is
//     */
//    private int determineFirstOfMonthDayOfWeek() {
//        // determine with which cell to start
//        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();
//        int firstOfMonthIdx = displayedYearMonth.get().atDay(1).getDayOfWeek().getValue() - firstDayOfWeek;
//        if (firstOfMonthIdx < 0) {
//            firstOfMonthIdx += daysPerWeek;
//        }
//        return firstOfMonthIdx;
//    }
//
//    protected void createDayCells() {
//        final EventHandler<MouseEvent> dayCellActionHandler = ev -> {
//            if (ev.getButton() != MouseButton.PRIMARY) {
//                return;
//            }
//
//            DateCell dayCell = (DateCell) ev.getSource();
//            selectDayCell(dayCell);
//            lastFocusedDayCell = dayCell;
//        };
//
//        getSkinnable().selectedValueProperty().addListener(this::onSelectedDateChanged);
//
//        for (int row = 0; row < 6; row++) {
//            for (int col = 0; col < daysPerWeek; col++) {
//                DateCell dayCell = createDayCell();
//                dayCell.addEventHandler(MouseEvent.MOUSE_CLICKED, dayCellActionHandler);
//                dayCells.add(dayCell);
//            }
//        }
//
//        dayCellDates = new LocalDate[6 * daysPerWeek];
//    }
//
//    // public for behavior class
//    public void selectDayCell(DateCell dateCell) {
//        getSkinnable().setSelectedValue(dayCellDate(dateCell));
//    }
//
//    private DateCell createDayCell() {
//        DateCell cell = null;
//        if (getSkinnable().getDayCellFactory() != null) {
//            cell = getSkinnable().getDayCellFactory().call(getSkinnable());
//        }
//        if (cell == null) {
//            cell = getNewDateCell();
//        }
//
//        return cell;
//    }
//
//    private DateCell getNewDateCell() {
//        DateCell cell = new DateCell();
//        cell.setMaxHeight(Double.MAX_VALUE);
//        return cell;
//    }
//
//    protected LocalDate dayCellDate(DateCell dateCell) {
//        assert (dayCellDates != null);
//        return dayCellDates[dayCells.indexOf(dateCell)];
//    }
//
//    private boolean isToday(LocalDate localDate) {
//        return (localDate.equals(LocalDate.now()));
//    }
//
//    void updateGrid() {
//        gridPane.getColumnConstraints().clear();
//        gridPane.getRowConstraints().clear();
//        gridPane.getChildren().clear();
//
//        int nCols = daysPerWeek + (getSkinnable().isShowWeekNumbers() ? 1 : 0);
//
//        ColumnConstraints columnConstraints = new ColumnConstraints();
//        columnConstraints.setPercentWidth(100); // Treated as weight
//        for (int i = 0; i < nCols; i++) {
//            gridPane.getColumnConstraints().add(columnConstraints);
//        }
//
//        RowConstraints rowConstraints = new RowConstraints();
//        rowConstraints.setPercentHeight(100); // Treated as height
//        for (int i = 0; i < 7; i++) {
//            gridPane.getRowConstraints().add(i, rowConstraints);
//        }
//
//        for (int i = 0; i < daysPerWeek; i++) {
//            gridPane.add(dayNameCells.get(i), i + nCols - daysPerWeek, 0);  // col, row
//        }
//
//        // Week number column
//        if (getSkinnable().isShowWeekNumbers()) {
//            for (int i = 0; i < 6; i++) {
//                gridPane.add(weekNumberCells.get(i), 0, i + 1);  // col, row
//            }
//        }
//
//        // setup: 6 rows of daysPerWeek (which is the maximum number of cells required in the worst case layout)
//        for (int row = 0; row < 6; row++) {
//            for (int col = 0; col < daysPerWeek; col++) {
//                gridPane.add(dayCells.get(row * daysPerWeek + col), col + nCols - daysPerWeek, row + 1);
//            }
//        }
//    }
//
//    void refresh() {
//        updateDayNameCells();
//        updateValues();
//        updateMonthLabelWidth();
//    }
//
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
//
//    private int getMonthsPerYear() {
//        ValueRange range = getPrimaryChronology().range(MONTH_OF_YEAR);
//        return (int) (range.getMaximum() - range.getMinimum() + 1);
//    }
//
//    public double computeTextWidth(Font font, String text, double wrappingWidth) {
//        Text layout = new Text(text != null ? text : "");
//        layout.setWrappingWidth(wrappingWidth);
//        layout.setFont(font);
//
//        return layout.getLayoutBounds().getWidth();
//    }
//
//    void updateDayNameCells() {
//        // first day of week, 1 = monday, 7 = sunday
//        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();
//
//        // july 13th 2009 is a Monday, so a firstDayOfWeek=1 must come out of the 13th
//        LocalDate date = LocalDate.of(2009, 7, 12 + firstDayOfWeek);
//        for (int i = 0; i < daysPerWeek; i++) {
//            String name = weekDayNameFormatter.withLocale(getLocale()).format(date.plus(i, DAYS));
//            dayNameCells.get(i).setText(titleCaseWord(name));
//        }
//    }
//
//    protected BorderPane createMonthYearPane() {
//        BorderPane monthYearPane = new BorderPane();
//        monthYearPane.getStyleClass().add("month-year-pane");
//
//        // Month spinner
//
//        HBox monthSpinner = new HBox();
//        monthSpinner.getStyleClass().add("spinner");
//
//        backMonthButton = new Button();
//        backMonthButton.getStyleClass().add("left-button");
//
//        forwardMonthButton = new Button();
//        forwardMonthButton.getStyleClass().add("right-button");
//
//        StackPane leftMonthArrow = new StackPane();
//        leftMonthArrow.getStyleClass().add("left-arrow");
//        leftMonthArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        backMonthButton.setGraphic(leftMonthArrow);
//
//        StackPane rightMonthArrow = new StackPane();
//        rightMonthArrow.getStyleClass().add("right-arrow");
//        rightMonthArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        forwardMonthButton.setGraphic(rightMonthArrow);
//
//
//        backMonthButton.setOnAction(t -> {
//            forward(-1, MONTHS, false);
//        });
//
//        monthLabel = new Label();
//        monthLabel.getStyleClass().add("spinner-label");
//
//        forwardMonthButton.setOnAction(t -> {
//            forward(1, MONTHS, false);
//        });
//
//        monthSpinner.getChildren().addAll(backMonthButton, monthLabel, forwardMonthButton);
//        monthYearPane.setLeft(monthSpinner);
//
//        // Year spinner
//
//        HBox yearSpinner = new HBox();
//        yearSpinner.getStyleClass().add("spinner");
//
//        backYearButton = new Button();
//        backYearButton.getStyleClass().add("left-button");
//
//        forwardYearButton = new Button();
//        forwardYearButton.getStyleClass().add("right-button");
//
//        StackPane leftYearArrow = new StackPane();
//        leftYearArrow.getStyleClass().add("left-arrow");
//        leftYearArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        backYearButton.setGraphic(leftYearArrow);
//
//        StackPane rightYearArrow = new StackPane();
//        rightYearArrow.getStyleClass().add("right-arrow");
//        rightYearArrow.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        forwardYearButton.setGraphic(rightYearArrow);
//
//
//        backYearButton.setOnAction(t -> {
//            forward(-1, YEARS, false);
//        });
//
//        yearLabel = new Label();
//        yearLabel.getStyleClass().add("spinner-label");
//
//        forwardYearButton.setOnAction(t -> {
//            forward(1, YEARS, false);
//        });
//
//        yearSpinner.getChildren().addAll(backYearButton, yearLabel, forwardYearButton);
//        yearSpinner.setFillHeight(false);
//        monthYearPane.setRight(yearSpinner);
//
//        return monthYearPane;
//    }
//
//    protected void forward(int offset, ChronoUnit unit, boolean focusDayCell) {
//        YearMonth yearMonth = displayedYearMonth.get();
//        DateCell dateCell = lastFocusedDayCell;
//        if (dateCell == null || !dayCellDate(dateCell).getMonth().equals(yearMonth.getMonth())) {
//            dateCell = findDayCellForDate(yearMonth.atDay(1));
//        }
//        goToDayCell(dateCell, offset, unit, focusDayCell);
//    }
//
//    private DateCell findDayCellForDate(LocalDate date) {
//        for (int i = 0; i < dayCellDates.length; i++) {
//            if (date.equals(dayCellDates[i])) {
//                return dayCells.get(i);
//            }
//        }
//        return dayCells.get(dayCells.size() / 2 + 1);
//    }
//
//    // public for behavior class
//    public void goToDayCell(DateCell dateCell, int offset, ChronoUnit unit, boolean focusDayCell) {
//        goToDate(dayCellDate(dateCell).plus(offset, unit), focusDayCell);
//    }
//
//    // public for behavior class
//    public void goToDate(LocalDate date, boolean focusDayCell) {
//        if (isValidDate(getSkinnable().getChronology(), date)) {
//            displayedYearMonth.set(YearMonth.from(date));
//            if (focusDayCell) {
//                findDayCellForDate(date).requestFocus();
//            }
//        }
//    }
//
//    protected boolean isValidDate(Chronology chrono, LocalDate date) {
//        try {
//            if (date != null) {
//                chrono.date(date);
//            }
//            return true;
//        } catch (DateTimeException ex) {
//            return false;
//        }
//    }
//
//    protected boolean isValidDate(Chronology chrono, LocalDate date, int offset, ChronoUnit unit) {
//        if (date != null) {
//            try {
//                return isValidDate(chrono, date.plus(offset, unit));
//            } catch (DateTimeException ex) {
//            }
//        }
//        return false;
//    }
//
//    protected void updateMonthYearPane() {
//        YearMonth yearMonth = displayedYearMonth.get();
//        String str = formatMonth(yearMonth);
//        monthLabel.setText(str);
//
//        str = formatYear(yearMonth);
//        yearLabel.setText(str);
//        double width = computeTextWidth(yearLabel.getFont(), str, 0);
//        if (width > yearLabel.getMinWidth()) {
//            yearLabel.setMinWidth(width);
//        }
//
//        Chronology chrono = getSkinnable().getChronology();
//        LocalDate firstDayOfMonth = yearMonth.atDay(1);
//        backMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, DAYS));
//        forwardMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, MONTHS));
//        backYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, YEARS));
//        forwardYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, YEARS));
//    }
//
//    private String formatMonth(YearMonth yearMonth) {
//        Locale locale = getLocale();
//        Chronology chrono = getPrimaryChronology();
//        try {
//            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));
//
//            String str = monthFormatterSO.withLocale(getLocale())
//                    .withChronology(chrono)
//                    .format(cDate);
//            if (Character.isDigit(str.charAt(0))) {
//                // Fallback. The standalone format returned a number, so use standard format instead.
//                str = monthFormatter.withLocale(getLocale())
//                        .withChronology(chrono)
//                        .format(cDate);
//            }
//            return titleCaseWord(str);
//        } catch (DateTimeException ex) {
//            // Date is out of range.
//            return "";
//        }
//    }
//
//    private String formatYear(YearMonth yearMonth) {
//        Locale locale = getLocale();
//        Chronology chrono = getPrimaryChronology();
//        try {
//            DateTimeFormatter formatter = yearFormatter;
//            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));
//            int era = cDate.getEra().getValue();
//            int nEras = chrono.eras().size();
//
//            /*if (cDate.get(YEAR) < 0) {
//                formatter = yearForNegYearFormatter;
//            } else */
//            if ((nEras == 2 && era == 0) || nEras > 2) {
//                formatter = yearWithEraFormatter;
//            }
//
//            // Fixme: Format Japanese era names with Japanese text.
//            String str = formatter.withLocale(getLocale())
//                    .withChronology(chrono)
//                    .withDecimalStyle(DecimalStyle.of(getLocale()))
//                    .format(cDate);
//
//            return str;
//        } catch (DateTimeException ex) {
//            // Date is out of range.
//            return "";
//        }
//    }
//
//    protected Locale getLocale() {
//        return Locale.getDefault(Locale.Category.FORMAT);
//    }
//
//    protected Chronology getPrimaryChronology() {
//        return getSkinnable().getChronology();
//    }
//
//    // Ensures that month and day names are titlecased (capitalized).
//    private String titleCaseWord(String str) {
//        if (str.length() > 0) {
//            int firstChar = str.codePointAt(0);
//            if (!Character.isTitleCase(firstChar)) {
//                str = new String(new int[]{Character.toTitleCase(firstChar)}, 0, 1) +
//                        str.substring(Character.offsetByCodePoints(str, 0, 1));
//            }
//        }
//        return str;
//    }
//
//    private void onSelectedDateChanged(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
//        if (newValue != null) {
//            getSkinnable().setSelectedValue(newValue);
//            goToDate(newValue, true);
//            updateValues();
//        }
//
//    }
//
//}
