package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.animation.morph.AnimatedObject3D;
import net.gtamps.android.graphics.graph.scene.animation.morph.KeyFrame;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.android.graphics.test.R;
import net.gtamps.android.graphics.test.input.CameraInputInterpreter;
import net.gtamps.android.input.controller.InputEngineController;
import net.gtamps.android.input.view.DefaultLayout;
import net.gtamps.android.input.view.TouchInputButton;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test10Scene extends SceneGraph {

    public Test10Scene() {
        super(new Camera(0, 0, 15, 0, 0, -1, 0, 1, 0));

        DefaultLayout layout = new DefaultLayout();
        layout.addButton(new TouchInputButton(0, 0, 1, 1), new CameraInputInterpreter(getActiveCamera()));
        InputEngineController.INSTANCE.setLayout(layout);
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        // Package where the obj file is located. Needed for context loader.
        String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";

        add(new Light(0, 0, 10, 0, 0, -1));
        Texture crate = new Texture(R.drawable.crate, Texture.Type.u_Texture01, true);

        AnimatedObject3D cube01 = new AnimatedObject3D(PACKAGE_NAME + "cube_01_obj");
        cube01.addTexture(crate);

        Object3D cube02 = new Object3D(PACKAGE_NAME + "cube_02_obj");
        cube02.addTexture(crate);
        cube01.addFrame("shapeshift", new KeyFrame("shapeshift01", cube02, 1000));

        Object3D cube03 = new Object3D(PACKAGE_NAME + "cube_03_obj");
        cube03.addTexture(crate);
        cube01.addFrame("shapeshift", new KeyFrame("shapeshift02", cube03, 1000));

        add(cube01);
    }
}
