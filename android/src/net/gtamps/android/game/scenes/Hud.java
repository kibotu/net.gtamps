package net.gtamps.android.game.scenes;

import net.gtamps.android.R;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.core.renderer.graph.primitives.AnimatedSprite;
import net.gtamps.android.core.renderer.graph.primitives.Camera;
import net.gtamps.android.core.renderer.graph.primitives.Sprite;
import net.gtamps.shared.state.State;

public class Hud extends BasicScene {

    private AnimatedSprite ring;
    private AnimatedSprite cursor;

    public Hud() {
        super();
    }

    @Override
    public void onCreate() {
        Camera camera =  new Camera(0, 0, 1, 0, 0, 0, 0, 1, 0);
        setActiveCamera(camera);
        camera.enableDepthTest(false);

        ring = new AnimatedSprite();
        ring.setVisible(false);
        add(ring);
        cursor = new AnimatedSprite();
        cursor.setVisible(false);
        add(cursor);

        ring.loadBufferedTexture(R.drawable.hud, R.raw.hud, true);
        cursor.loadBufferedTexture(R.drawable.hud,R.raw.hud,true);
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
