package com.allynote.allyFx.behavior;

import com.allynote.allyFx.control.AllyCalendarView;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by super on 12/17/15.
 */
public class AllyCalendarViewBehavior extends BehaviorBase<AllyCalendarView> {

    protected static final List<KeyBinding> LIST_VIEW_BINDINGS = new ArrayList<KeyBinding>();

    public AllyCalendarViewBehavior(AllyCalendarView control) {
        super(control, LIST_VIEW_BINDINGS);
    }

}
