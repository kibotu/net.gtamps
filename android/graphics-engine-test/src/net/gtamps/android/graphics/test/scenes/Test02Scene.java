package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.R;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureSample;
import net.gtamps.android.graphics.graph.scene.primitives.Camera;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Triangle;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test02Scene extends SceneGraph {

    public Test02Scene() {
        super(new Camera(0, 0, 10, 0, 0, -1, 0, 1, 0));
//        getActiveCamera().setOrthographicView();
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        add(new Light(0, 0, 10, 0, 0, -1));

        // creating objects
        Triangle triangle = new Triangle();
        triangle.setPosition(0, 0, -10);

        // adding texture
        triangle.addTexture(new TextureSample(R.drawable.crate, TextureSample.Type.texture_01));

        // add to scene
        add(triangle);
    }
}
