package net.gtamps.android.input.view;

import net.gtamps.android.input.controller.event.InputInterpreter;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 22/02/12
 * Time: 16:15
 */
public class AbstractInputLayout {

    private TouchPanel touchPanel;

    public AbstractInputLayout(float x, float y, float width, float height) {
        touchPanel = new TouchPanel(x,y,width,height);
    }

    public TouchPanel getTouchPanel() {
        return touchPanel;
    }

    public void addButton(TouchInputButton touchInputButton, InputInterpreter inputInterpreter) {
        touchPanel.addButton(touchInputButton,inputInterpreter);
    }
}
