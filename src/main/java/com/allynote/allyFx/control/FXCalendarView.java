/*
 * Copyright 2015 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.allynote.allyFx.control;

import com.allynote.allyFx.skin.FXCalendarViewSkin;
import com.allynote.allyFx.skin.FXMonthViewSkin;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * The {@link FXCalendarView} is a simple calendar like control that displays a
 * single month, together with controls at the top to change what month is
 * displayed.
 * <p>
 * It can be used as a simple date picker.
 *
 * @author Robert Zenz
 */
public class FXCalendarView extends Control {
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Class members
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * The path to the default style sheet.
     */
    public static final String DEFAULT_STYLE = "/css/fxmonthview.css";

    /**
     * The default class name used for styling.
     */
    public static final String DEFAULT_STYLE_CLASS = "monthview";

    /**
     * The property for the selected date.
     */
    private ObjectProperty<LocalDate> selectedDate;

    /**
     * The property for the currently visible month.
     */
//    private ObjectProperty<LocalDate> visibleMonth;

    private BooleanProperty showMonthYearPane;

    public final void setShowMonthYearPane(boolean value) {
        showMonthYearPane.setValue(value);
    }

    public final boolean isShowMonthYearPane() {
        return showMonthYearPane.getValue();
    }

    public final BooleanProperty showWeekNumbersProperty() {
        if (showWeekNumbers == null) {
            String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
            boolean localizedDefault =
                    (!country.isEmpty() &&
                            ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country));
            showWeekNumbers = new StyleableBooleanProperty(localizedDefault) {
                @Override
                public CssMetaData<FXCalendarView, Boolean> getCssMetaData() {
                    return StyleableProperties.SHOW_WEEK_NUMBERS;
                }

                @Override
                public Object getBean() {
                    return FXCalendarView.this;
                }

                @Override
                public String getName() {
                    return "showWeekNumbers";
                }
            };
        }
        return showWeekNumbers;
    }

    private BooleanProperty showWeekNumbers;

    public final void setShowWeekNumbers(boolean value) {
        showWeekNumbersProperty().setValue(value);
    }

    public final boolean isShowWeekNumbers() {
        return showWeekNumbersProperty().getValue();
    }

    private ObjectProperty<Callback<FXCalendarView, DateCell>> dayCellFactory;
    public final void setDayCellFactory(Callback<FXCalendarView, DateCell> value) {
        dayCellFactoryProperty().set(value);
    }
    public final Callback<FXCalendarView, DateCell> getDayCellFactory() {
        return (dayCellFactory != null) ? dayCellFactory.get() : null;
    }
    public final ObjectProperty<Callback<FXCalendarView, DateCell>> dayCellFactoryProperty() {
        if (dayCellFactory == null) {
            dayCellFactory = new SimpleObjectProperty<Callback<FXCalendarView, DateCell>>(this, "dayCellFactory");
        }
        return dayCellFactory;
    }

    public final ObjectProperty<Chronology> chronologyProperty() {
        return chronology;
    }
    private ObjectProperty<Chronology> chronology =
            new SimpleObjectProperty<Chronology>(this, "chronology", null);
    public final Chronology getChronology() {
        Chronology chrono = chronology.get();
        if (chrono == null) {
            try {
                chrono = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
            } catch (Exception ex) {
                System.err.println(ex);
            }
            if (chrono == null) {
                chrono = IsoChronology.INSTANCE;
            }
            //System.err.println(chrono);
        }
        return chrono;
    }
    public final void setChronology(Chronology value) {
        chronology.setValue(value);
    }

    public final ObjectProperty<StringConverter<LocalDate>> converterProperty() { return converter; }
    private ObjectProperty<StringConverter<LocalDate>> converter = new SimpleObjectProperty<StringConverter<LocalDate>>(this, "converter", null);
    public final void setConverter(StringConverter<LocalDate> value) { converterProperty().set(value); }
    public final StringConverter<LocalDate> getConverter() {
        StringConverter<LocalDate> converter = converterProperty().get();
        if (converter != null) {
            return converter;
        } else {
            return defaultConverter;
        }
    }

    // Create a symmetric (format/parse) converter with the default locale.
    private StringConverter<LocalDate> defaultConverter =
            new LocalDateStringConverter(FormatStyle.SHORT, null, getChronology());


    private static class StyleableProperties {
        private static final String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
        private static final CssMetaData<FXCalendarView, Boolean> SHOW_WEEK_NUMBERS =
                new CssMetaData<FXCalendarView, Boolean>("-fx-show-week-numbers",
                        BooleanConverter.getInstance(),
                        (!country.isEmpty() &&
                                ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country))) {
                    @Override
                    public boolean isSettable(FXCalendarView n) {
                        return n.showWeekNumbers == null || !n.showWeekNumbers.isBound();
                    }

                    @Override
                    public StyleableProperty<Boolean> getStyleableProperty(FXCalendarView n) {
                        return (StyleableProperty<Boolean>) (WritableValue<Boolean>) n.showWeekNumbersProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<CssMetaData<? extends Styleable, ?>>(Control.getClassCssMetaData());
            Collections.addAll(styleables,
                    SHOW_WEEK_NUMBERS
            );
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Initialization
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Creates a new instance of {@link FXCalendarView}.
     */
    public FXCalendarView() {

        getStyleClass().add(DEFAULT_STYLE_CLASS);

        selectedDate = new SimpleObjectProperty<>();

        showMonthYearPane = new SimpleBooleanProperty(true);

//        visibleMonth = new SimpleObjectProperty<>(LocalDate.now());

    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overwritten methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserAgentStylesheet() {
        return DEFAULT_STYLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new FXCalendarViewSkin(this);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // User-defined methods
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Gets the currently selected {@link LocalDate}.
     *
     * @return the currently selected {@link LocalDate}. {@code null} if there
     * is none selected.
     */
    public LocalDate getSelectedDate() {
        return selectedDate.get();
    }

    /**
     * Gets the currently visible month.
     *
     * @return the currently visible month.
     */
//    public LocalDate getVisibleMonth() {
//        return visibleMonth.get();
//    }

    /**
     * Gets the property for the currently selected {@link LocalDate}.
     *
     * @return the property for the currently selected {@link LocalDate}.
     */
    public ObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate;
    }

    /**
     * Sets the currently selected {@link LocalDate}.
     *
     * @param pSelectedDate the currently selected {@link LocalDate}.
     *                      {@code null} if there is none selected.
     */
    public void setSelectedDate(LocalDate pSelectedDate) {
        selectedDate.set(pSelectedDate);
    }

    /**
     * Sets the currently visible month.
     *
     * @param pVisibleMonth the currently visible month.
     */
//    public void setVisibleMonth(LocalDate pVisibleMonth) {
//        visibleMonth.set(pVisibleMonth);
//    }
//
//    /**
//     * Gets the property for the currently visible month.
//     *
//     * @return the property for the currently visible month.
//     */
//    public ObjectProperty<LocalDate> visibleMonthProperty() {
//        return visibleMonth;
//    }

}    // FXMonthView
