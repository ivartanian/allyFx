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

import com.allynote.allyFx.skin.AllyDesignCalendarViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DateCell;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;

import java.time.LocalDate;


public class AllyDesignCalendarView extends AllyAbstractCalendarView<DateCell, LocalDate> {

    private static final String DEFAULT_STYLE = "css/allydesigncalendarview.css";
    private static final String DEFAULT_STYLE_CLASS = "ally-designcalendar-view";

    public AllyDesignCalendarView() {
        this(LocalDate.now());
    }

    public AllyDesignCalendarView(LocalDate selectedValue) {
        this(selectedValue, true);
    }

    public AllyDesignCalendarView(LocalDate selectedValue, Boolean showMonthYearPane) {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.selectedValue = new SimpleObjectProperty<>(selectedValue);
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
        return new AllyDesignCalendarViewSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return DEFAULT_STYLE;
    }

}
