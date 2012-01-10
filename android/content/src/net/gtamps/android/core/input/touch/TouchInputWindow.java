package net.gtamps.android.core.input.touch;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import net.gtamps.android.core.input.event.InputInterpreter;

import java.util.HashMap;

public class TouchInputWindow implements OnTouchListener {
    HashMap<TouchInputButton, InputInterpreter> buttons = new HashMap<TouchInputButton, InputInterpreter>();
    private float resolutionX;
    private float resolutionY;

    public TouchInputWindow() {
        this.resolutionX = 1024;
        this.resolutionY = 600;
    }

    public void addButton(TouchInputButton tib, InputInterpreter ia) {
        buttons.put(tib, ia);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float normX = event.getX() / resolutionX;
        float normY = event.getY() / resolutionY;
        for (TouchInputButton tib : buttons.keySet()) {
            if (tib.isHit(normX, normY)) {
                ((InputInterpreter) buttons.get(tib)).interpretTouch((normX - tib.getX()) / tib.getSizeX(), (normY - tib.getY()) / tib.getSizeY(), event);
            }
        }
        //touch event was handled:
        return true;
    }

    public void setResolution(int width, int height) {
        this.resolutionX = width;
        this.resolutionY = height;
    }
}
