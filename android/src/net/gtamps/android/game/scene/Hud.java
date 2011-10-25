package net.gtamps.android.game.scene;

import net.gtamps.android.R;
import net.gtamps.android.core.renderer.graph.primitives.Camera;
import net.gtamps.android.core.input.InputEngine;
import net.gtamps.android.game.objects.Sprite;
import net.gtamps.shared.state.State;

public class Hud {

    private Camera camera;
    private Scene scene;

    private Sprite ring;
    private Sprite cursor;

    public Hud() {
    }

    public Scene getScene() {

        if(scene == null) {
            scene = new Scene();
            camera =  new Camera(0, 0,1, 0, 0, 0, 0, 1, 0);
            scene.setActiveCamera(camera);
            camera.enableDepthTest(false);

            ring = new Sprite();
            ring.setVisible(false);
            scene.add(ring);
            cursor = new Sprite();
            cursor.setVisible(false);
            scene.add(cursor);

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
