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

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.util.Callback;

import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public abstract class AllyAbstractCalendarView<T extends Control> extends Control {

    private static final String DEFAULT_STYLE = "css/allydefault.css";

    /**
     * The default class name used for styling.
     */
    private static final String DEFAULT_STYLE_CLASS = "ally-calendar-view";

    public final BooleanProperty showWeekNumbersProperty() {
        if (showWeekNumbers == null) {
            String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
            boolean localizedDefault =
                    (!country.isEmpty() &&
                            ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country));
            showWeekNumbers = new StyleableBooleanProperty(localizedDefault) {
                @Override
                public CssMetaData<AllyAbstractCalendarView, Boolean> getCssMetaData() {
                    return StyleableProperties.SHOW_WEEK_NUMBERS;
                }

                @Override
                public Object getBean() {
                    return AllyAbstractCalendarView.this;
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

    private ObjectProperty<Callback<AllyAbstractCalendarView, T>> dayCellFactory;
    public final void setDayCellFactory(Callback<AllyAbstractCalendarView, T> value) {
        dayCellFactoryProperty().set(value);
    }
    public final Callback<AllyAbstractCalendarView, T> getDayCellFactory() {
        return (dayCellFactory != null) ? dayCellFactory.get() : null;
    }
    public final ObjectProperty<Callback<AllyAbstractCalendarView, T>> dayCellFactoryProperty() {
        if (dayCellFactory == null) {
            dayCellFactory = new SimpleObjectProperty<>(this, "dayCellFactory");
        }
        return dayCellFactory;
    }

    private ObjectProperty<Chronology> chronology = new SimpleObjectProperty<>(this, "chronology", null);
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
    public final ObjectProperty<Chronology> chronologyProperty() {
        return chronology;
    }
    public final void setChronology(Chronology value) {
        chronology.setValue(value);
    }


    public AllyAbstractCalendarView() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }


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

    private static class StyleableProperties {
        private static final String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
        private static final CssMetaData<AllyAbstractCalendarView, Boolean> SHOW_WEEK_NUMBERS =
                new CssMetaData<AllyAbstractCalendarView, Boolean>("-fx-show-week-numbers",
                        BooleanConverter.getInstance(),
                        (!country.isEmpty() &&
                                ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country))) {
                    @Override
                    public boolean isSettable(AllyAbstractCalendarView n) {
                        return n.showWeekNumbers == null || !n.showWeekNumbers.isBound();
                    }

                    @Override
                    public StyleableProperty<Boolean> getStyleableProperty(AllyAbstractCalendarView n) {
                        return (StyleableProperty<Boolean>) n.showWeekNumbersProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables,
                    SHOW_WEEK_NUMBERS
            );
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

}
