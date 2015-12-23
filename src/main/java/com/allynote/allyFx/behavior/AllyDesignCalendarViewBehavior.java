package com.allynote.allyFx.behavior;

import com.allynote.allyFx.control.AllyDesignCalendarView;

/**
 * Created by super on 12/17/15.
 */
public class AllyDesignCalendarViewBehavior extends AllyAbstractCalendarBehavior<AllyDesignCalendarView> {

    public AllyDesignCalendarViewBehavior(AllyDesignCalendarView control) {
        super(control, KEY_BINDINGS);
        this.control = control;
    }

}
