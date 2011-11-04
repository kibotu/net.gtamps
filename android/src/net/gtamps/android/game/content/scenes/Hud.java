package net.gtamps.android.game.content.scenes;

import net.gtamps.android.R;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.core.renderer.graph.scene.BasicScene;
import net.gtamps.android.core.renderer.graph.scene.primitives.AnimatedSprite;
import net.gtamps.android.core.renderer.graph.scene.primitives.Camera;
import net.gtamps.android.core.renderer.graph.scene.primitives.Sprite;
import net.gtamps.shared.game.state.State;

public class Hud extends BasicScene {

    private AnimatedSprite ring;
    private AnimatedSprite cursor;

    public Hud() {
        super();
    }

    @Override
    public void onCreate() {
        Camera camera = new Camera(0, 0, 1, 0, 0, 0, 0, 1, 0);
        setActiveCamera(camera);
        camera.enableDepthTest(false);

        ring = new AnimatedSprite();
        ring.setVisible(true);
        add(ring);
        cursor = new AnimatedSprite();
        cursor.setVisible(true);
        add(cursor);

        ring.loadBufferedTexture(R.drawable.hud, R.raw.hud, true);
        cursor.loadBufferedTexture(R.drawable.hud, R.raw.hud, true);
        cursor.animate(0.51f, State.Type.IDLE);

        InputEngine.getInstance().setCamera(camera);
    }

    public Sprite getCursor() {
        return cursor;
    }

    public Sprite getRing() {
        return ring;
    }

    @Override
    public void onDirty() {
    }
}
