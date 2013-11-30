package net.gtamps.android.input.view;

import android.view.MotionEvent;
import android.view.View;
import net.gtamps.android.input.controller.event.InputInterpreter;

import java.util.HashMap;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 22/02/12
 * Time: 16:28
 */
public class TouchPanel extends TouchInputButton {

    private HashMap<TouchInputButton, InputInterpreter> buttons;

    public TouchPanel(float x, float y, float width, float height) {
        super(x, y, width, height);
        buttons = new HashMap<TouchInputButton, InputInterpreter>();
    }

    public void addButton(TouchInputButton touchInputButton, InputInterpreter inputInterpreter) {
        buttons.put(touchInputButton,inputInterpreter);
    }

    public InputInterpreter removeButton(TouchInputButton touchInputButton) {
        return buttons.remove(touchInputButton);
    }

    public boolean onTouch(View view, MotionEvent event) {

        float px = event.getX() / width;
        float py = event.getY() / height;

        if (!isHit(px, py)) return true;

        for (TouchInputButton b : buttons.keySet()) {
            if(b.isHit(px,py)) buttons.get(b).interpretTouch((px - b.x) / b.width, (py - b.y) / b.height, view, event);
        }

        //touch event was handled:
        return true;
    }
}

