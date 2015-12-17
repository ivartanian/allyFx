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

import com.allynote.allyFx.skin.AllyCalendarViewSkin;
import com.allynote.allyFx.skin.FXCalendarViewSkin;
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
import javafx.scene.control.Skin;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class AllyCalendarView extends AllyAbstractCalendarView<DateCell> {

    private ObjectProperty<LocalDate> selectedDate;

    public AllyCalendarView() {
        this(LocalDate.now());
    }

    public AllyCalendarView(LocalDate selectedDate) {
        this.selectedDate = new SimpleObjectProperty<>(selectedDate);
    }

    public AllyCalendarView(LocalDate selectedDate, Boolean showMonthYearPane) {
        this.selectedDate = new SimpleObjectProperty<>(selectedDate);
        this.showMonthYearPane = new SimpleBooleanProperty(showMonthYearPane);
    }

    public LocalDate getSelectedDate() {
        return selectedDate.get();
    }

    public ObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate pSelectedDate) {
        selectedDate.set(pSelectedDate);
    }

    private BooleanProperty showMonthYearPane;

    public final void setShowMonthYearPane(boolean value) {
        showMonthYearPane.setValue(value);
    }

    public final boolean isShowMonthYearPane() {
        return showMonthYearPane.getValue();
    }

    private StringConverter<LocalDate> defaultConverter = null;
    public final ObjectProperty<StringConverter<LocalDate>> converterProperty() { return converter; }
    private ObjectProperty<StringConverter<LocalDate>> converter = new SimpleObjectProperty<>(this, "converter", null);
    public final void setConverter(StringConverter<LocalDate> value) { converterProperty().set(value); }
    public final StringConverter<LocalDate> getConverter() {
        StringConverter<LocalDate> converter = converterProperty().get();
        if (converter != null) {
            return converter;
        } else {
            return defaultConverter;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        return new AllyCalendarViewSkin(this);
    }


}
