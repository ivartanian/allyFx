package com.allynote.allyFx.behavior;

import com.allynote.allyFx.control.AllyAbstractCalendarView;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by super on 12/17/15.
 */
public abstract class AllyAbstractCalendarBehavior<T extends AllyAbstractCalendarView> extends BehaviorBase<T> {

    protected static final List<KeyBinding> KEY_BINDINGS = new ArrayList<>();
    static {
        KEY_BINDINGS.add(new KeyBinding(LEFT, KEY_PRESSED, "Left"));
        KEY_BINDINGS.add(new KeyBinding(RIGHT, KEY_PRESSED, "Right"));
        KEY_BINDINGS.add(new KeyBinding(UP, KEY_PRESSED, "Up"));
        KEY_BINDINGS.add(new KeyBinding(DOWN, KEY_PRESSED, "Down"));
    }

    T control;

    public AllyAbstractCalendarBehavior(T control) {
        super(control, KEY_BINDINGS);
        this.control = control;
    }

    public AllyAbstractCalendarBehavior(T control, List<KeyBinding> bindings) {
        super(control, bindings);
        this.control = control;
    }

}
