package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.AnimatedSkeletonObject3D;
import net.gtamps.android.graphics.graph.scene.mesh.parser.Parser;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.android.graphics.test.R;
import net.gtamps.android.graphics.test.input.CameraInputInterpreter;
import net.gtamps.android.input.controller.InputEngineController;
import net.gtamps.android.input.view.DefaultLayout;
import net.gtamps.android.input.view.TouchInputButton;
import net.gtamps.shared.Utils.math.Color4;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test11Scene extends SceneGraph {

    public Test11Scene() {
        super(new Camera(0, 0, 5, 0, 0, -1, 1, 0, 0));

        DefaultLayout layout = new DefaultLayout();
        layout.addButton(new TouchInputButton(0, 0, 1, 1), new CameraInputInterpreter(getActiveCamera()));
        InputEngineController.INSTANCE.setLayout(layout);
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        // set background
        getActiveCamera().setBackgroundColor(Color4.BLACK);

        // add lights
        add(new Light(0, 0, 10, 0, 0, -1));

        // Package where the obj file is located. Needed for context loader.
        String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";

        // add skn
        AnimatedSkeletonObject3D object3D = new AnimatedSkeletonObject3D(PACKAGE_NAME + "katarina_cat_skn", true, Parser.Type.SKN);

        // add texture
        object3D.addTexture(new Texture(R.drawable.katarina_cat, Texture.Type.texture_01, true));

        // default scaling to 0.01f
        object3D.setScaling(0.01f,0.01f,0.01f);

        // add bones
        object3D.addSkl(PACKAGE_NAME + "katarina_cat_skl");

        // add idle animation
        object3D.addAnm(PACKAGE_NAME + "katarina_idle1_anm");

        //add obj to scene
        add(object3D);
    }
}
