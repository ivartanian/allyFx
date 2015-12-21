package com.allynote.allyFx.behavior;

import com.allynote.allyFx.control.AllyCalendarView;
import com.allynote.allyFx.control.AllyDateTimeCalendarView;

/**
 * Created by super on 12/17/15.
 */
public class AllyDateTimeCalendarViewBehavior extends AllyAbstractCalendarBehavior<AllyDateTimeCalendarView> {

    public AllyDateTimeCalendarViewBehavior(AllyDateTimeCalendarView control) {
        super(control, KEY_BINDINGS);
        this.control = control;
    }

}
