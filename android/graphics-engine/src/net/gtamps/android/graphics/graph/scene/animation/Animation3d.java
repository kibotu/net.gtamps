package net.gtamps.android.graphics.graph.scene.animation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jan Rabe
 * Date: 18/11/12
 * Time: 16:18
 */
public class Animation3d {

    protected List<KeyFrame> frames;
    protected int fps;

    public Animation3d() {
        frames = new ArrayList<KeyFrame>(10);
        fps = 1;
    }

    public void addFrame(@NotNull KeyFrame frame) {
        frames.add(frame);
    }

    public void setFPS(int fps) {
        this.fps = fps;
    }
}
