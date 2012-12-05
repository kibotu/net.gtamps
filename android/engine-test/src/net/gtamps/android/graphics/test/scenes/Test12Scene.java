package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.animation.rigged.RiggedObject3D;
import net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader.*;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Cube;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.android.graphics.renderer.Shader;
import net.gtamps.android.graphics.test.R;
import net.gtamps.android.graphics.test.input.CameraInputInterpreter;
import net.gtamps.android.input.controller.InputEngineController;
import net.gtamps.android.input.view.DefaultLayout;
import net.gtamps.android.input.view.TouchInputButton;
import net.gtamps.shared.Utils.math.Color4;

import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;

/**
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:30
 */
public class Test12Scene extends SceneGraph {

    public Test12Scene() {
        super(new Camera(0, 1, 7, 0, 1, -1, 0, 1, 0));

        DefaultLayout layout = new DefaultLayout();
        layout.addButton(new TouchInputButton(0, 0, 1, 1), new CameraInputInterpreter(getActiveCamera()));
        InputEngineController.INSTANCE.setLayout(layout);
    }

    @Override
    protected void onSurfaceCreatedInternal(GL10 gl10) {
        // set background
        getActiveCamera().setBackgroundColor(Color4.BLACK);

        // add lights
        Light light = new Light(0, 0, 10, 0, 0, -1);
        add(light);

        // hang light on camera
        light.setParent(getActiveCamera());

        // Package where the obj file is located. Needed for context loader.
        String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";

//        Cube cube = new Cube();
//        cube.addTexture(new Texture(R.drawable.crate, Texture.Type.u_Texture01, true));
//        add(cube);

        SKNFile katarina_cat_skn = SKNReader.readBinary(PACKAGE_NAME + "katarina_cat_skn");
        SKLFile katarina_cat_skl = SKLReader.readBinary(PACKAGE_NAME + "katarina_cat_skl");
        ANMFile katarina_idle1_anm = ANMReader.readBinary(PACKAGE_NAME + "katarina_idle1_anm");
        ANMFile katarina_dance_anm = ANMReader.readBinary(PACKAGE_NAME + "katarina_dance_anm");

        HashMap<String, ANMFile> animations = new HashMap<String, ANMFile>();
        animations.put(PACKAGE_NAME + "katarina_idle1_anm", katarina_idle1_anm);
        animations.put(PACKAGE_NAME + "katarina_dance_anm", katarina_dance_anm);

        RiggedObject3D katarina = new RiggedObject3D();
        katarina.create(katarina_cat_skn,katarina_cat_skl,animations);
        katarina.setScaling(0.01f,0.01f,0.01f);
        katarina.addTexture(new Texture(R.drawable.katarina_cat, Texture.Type.u_Texture01, true));
        katarina.getRenderState().setShader(Shader.Type.PHONG);

        add(katarina);
    }
}
