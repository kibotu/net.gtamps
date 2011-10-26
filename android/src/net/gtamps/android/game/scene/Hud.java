package net.gtamps.android.game.scene;

import net.gtamps.android.R;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.core.renderer.graph.primitives.AnimatedSprite;
import net.gtamps.android.core.renderer.graph.primitives.Camera;
import net.gtamps.android.core.renderer.graph.primitives.Sprite;
import net.gtamps.shared.state.State;

public class Hud {

    private Camera camera;
    private Scene scene;

    private AnimatedSprite ring;
    private AnimatedSprite cursor;

    public Hud() {
    }

    public Scene getScene() {

        if(scene == null) {
            scene = new Scene();
            camera =  new Camera(0, 0,1, 0, 0, 0, 0, 1, 0);
            scene.setActiveCamera(camera);
            camera.enableDepthTest(false);

            ring = new AnimatedSprite();
            ring.setVisible(false);
            scene.addNode(ring);
            cursor = new AnimatedSprite();
            cursor.setVisible(false);
            scene.addNode(cursor);

            ring.loadBufferedTexture(R.drawable.hud, R.raw.hud, true);
            cursor.loadBufferedTexture(R.drawable.hud,R.raw.hud,true);
            cursor.animate(0.51f, State.Type.IDLE);

            InputEngine.getInstance().setCamera(camera);
        }

        return scene;
    }

    public Sprite getCursor() {
        return cursor;
    }

    public Sprite getRing() {
        return ring;
    }
}
