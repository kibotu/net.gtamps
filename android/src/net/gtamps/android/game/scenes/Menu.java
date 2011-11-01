package net.gtamps.android.game.scenes;

import net.gtamps.android.core.renderer.graph.primitives.Camera;

public class Menu extends BasicScene {

    @Override
    public void onCreate() {
        setActiveCamera(new Camera(0, 0, 1, 0, 0, 0, 0, 1, 0));
    }

    @Override
    public void onDirty() {
    }
}
