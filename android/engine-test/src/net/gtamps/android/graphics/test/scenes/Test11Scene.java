package net.gtamps.android.graphics.test.scenes;

import android.graphics.Color;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.animation.skeleton.AnimatedSkeletonObject3D;
import net.gtamps.android.graphics.graph.scene.mesh.Material;
import net.gtamps.android.graphics.graph.scene.mesh.parser.Parser;
import net.gtamps.android.graphics.graph.scene.mesh.parser.SkeletonAnimationParser;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;
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
        getActiveCamera().setBackgroundColor(Color4.DARK_GRAY);

        DefaultLayout layout = new DefaultLayout();
        layout.addButton(new TouchInputButton(0, 0, 1, 1), new CameraInputInterpreter(getActiveCamera()));
        InputEngineController.INSTANCE.setLayout(layout);
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

//        drawKata();
        drawCube();
    }

    public void drawCube() {


        // Package where the obj file is located. Needed for context loader.
        String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";

        add(new Light(0, 0, 10, 0, 0, -1));

        // add obj + mtl
//        AnimatedSkeletonObject3D object3D = new AnimatedSkeletonObject3D(PACKAGE_NAME + "cube_01_obj", true, Parser.Type.OBJ);
        AnimatedSkeletonObject3D object3D = new AnimatedSkeletonObject3D(PACKAGE_NAME + "katarina_cat_skn", true, Parser.Type.SKN);

        // add texture
        object3D.addTexture(new Texture(R.drawable.katarina_cat, Texture.Type.texture_01, true));
        object3D.setScaling(0.01f,0.01f,0.01f);

        //add bones
        SkeletonAnimationParser.loadSkl(PACKAGE_NAME + "katarina_cat_skl", object3D);

        //add idle animation
        SkeletonAnimationParser.loadAmn(PACKAGE_NAME + "katarina_idle1_anm", object3D);

        // add obj to scene
        add(object3D);
    }

    public void drawKata() {

        // Package where the obj file is located. Needed for context loader.
        final String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";

        add(new Light(0, 0, 10, 0, 0, -1));

        Object3D object3D = new Object3D(PACKAGE_NAME + "katarina_cat_obj");
        object3D.addTexture(new Texture(R.drawable.katarina_cat, Texture.Type.texture_01, true));
        object3D.getMaterial().setShininess(24);
//        object3D.getRenderState().setDoubleSided(true);
        add(object3D);
    }
}
