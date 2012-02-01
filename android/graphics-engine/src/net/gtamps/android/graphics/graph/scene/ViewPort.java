package net.gtamps.android.graphics.graph.scene;

import net.gtamps.android.graphics.utils.Registry;
import net.gtamps.shared.Utils.Logger;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 01/02/12
 * Time: 11:11
 */
public class ViewPort {

    private int x;
    private int y;
    private int width;
    private int height;
    private float aspectRatio;

    public ViewPort(int x, int y, int width, int height) {
        setViewPort(x, y, width, height);
    }

    public void setViewPort(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.aspectRatio = (float)width / height;
    }

    public void applyViewPort() {
        Registry.getRenderer().setViewPort(x, y, width,height);
        Logger.i(this, this.toString());
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    @Override
    public String toString() {
        return "ViewPort[" +
                 + x +
                "|" + y +
                "|" + width +
                "|" + height +
                "|" + aspectRatio +
                ']';
    }
}
