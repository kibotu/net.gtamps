package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.R;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test08Scene extends SceneGraph {

    public Test08Scene() {
        super(new Camera(0, 0, 15, 0, 0, -1, 0, 1, 0));
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        // Package where the obj file is located. Needed for context loader.
        String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";

        add(new Light(0, 0, 10, 0, 0, -1));

        Object3D camaro = new Object3D(PACKAGE_NAME + "camaro_obj");
        camaro.addTexture(new Texture(R.drawable.camaro, Texture.Type.texture_01, true));

        add(camaro);
    }
}