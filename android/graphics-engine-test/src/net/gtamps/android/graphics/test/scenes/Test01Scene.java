package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.primitives.Camera;
import net.gtamps.android.graphics.graph.scene.primitives.Cube;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Triangle;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test01Scene extends SceneGraph {

    @Override
    public void onSurfaceCreated(GL10 gl10) {
        setActiveCamera(new Camera(0, 0, 10, 0, 0, -1, 0, 1, 0));
        add(new Light(0, 0, 10, 0, 0, -1));
        add(new Cube());
        add(new Cube());
        add(new Triangle());
        add(new Triangle());
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
    }

    @Override
    public void onResume(GL10 gl10) {
    }
}
