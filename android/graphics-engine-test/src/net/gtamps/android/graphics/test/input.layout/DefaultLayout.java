package net.gtamps.android.graphics.test.input.layout;

import net.gtamps.android.input.view.AbstractInputLayout;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 22/02/12
 * Time: 19:15
 */
public class DefaultLayout extends AbstractInputLayout {

    public DefaultLayout() {
        this(0, 0, 800, 480);
    }

    public DefaultLayout(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
}
