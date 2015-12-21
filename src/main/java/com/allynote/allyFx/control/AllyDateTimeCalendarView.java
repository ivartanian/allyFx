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
import com.allynote.allyFx.skin.AllyDateTimeCalendarViewSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DateCell;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;

import java.time.LocalDate;


public class AllyDateTimeCalendarView extends AllyAbstractCalendarView<DateCell, LocalDate> {

    public AllyDateTimeCalendarView() {
        this(LocalDate.now());
    }

    public AllyDateTimeCalendarView(LocalDate selectedValue) {
        this(selectedValue, true);
    }

    public AllyDateTimeCalendarView(LocalDate selectedValue, Boolean showMonthYearPane) {
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
        return new AllyDateTimeCalendarViewSkin(this);
    }


}