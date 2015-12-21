package com.allynote.allyFx.behavior;

import com.allynote.allyFx.control.AllyCalendarView;

/**
 * Created by super on 12/17/15.
 */
public class AllyCalendarViewBehavior extends AllyAbstractCalendarBehavior<AllyCalendarView> {

    public AllyCalendarViewBehavior(AllyCalendarView control) {
        super(control, KEY_BINDINGS);
        this.control = control;
    }

}
