package com.allynote.allyFx.behavior;

import com.allynote.allyFx.control.FXCalendarView;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import javafx.scene.control.Control;

import java.util.List;

/**
 * Created by uartan on 16.12.2015.
 */
public class FXCalendarBehavior extends BehaviorBase<FXCalendarView> {

    /**
     * Create a new BehaviorBase for the given control. The Control must not
     * be null.
     *
     * @param control     The control. Must not be null.
     * @param keyBindings The key bindings that should be used with this behavior.
     */
    public FXCalendarBehavior(FXCalendarView control, List<KeyBinding> keyBindings) {
        super(control, keyBindings);
    }
}
