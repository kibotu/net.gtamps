package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.graph.RootNode;
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

        SKNFile skn = SKNReader.readBinary(PACKAGE_NAME + "ahri_skn");
        SKLFile skl = SKLReader.readBinary(PACKAGE_NAME + "ahri_skl");
        ANMFile anm = ANMReader.readBinary(PACKAGE_NAME + "ahri_dance_anm");

        HashMap<String, ANMFile> animations = new HashMap<String, ANMFile>();
        animations.put(PACKAGE_NAME + "ahri_dance_anm", anm);

        RiggedObject3D ahri = new RiggedObject3D();
        ahri.create(skn,skl,animations);
        ahri.setScaling(0.01f,0.01f,0.01f);
        ahri.addTexture(new Texture(R.drawable.ahri, Texture.Type.u_Texture01, true));
        ahri.getRenderState().setShader(Shader.Type.PHONG);

        // skeleton
        RootNode skeleton = ahri.getSkeleton();

        // show skeleton first
        boolean showSkeletonFirst = false;
        ahri.setVisible(!showSkeletonFirst);
        skeleton.setVisible(showSkeletonFirst);

        //add obj to scene
        add(ahri);
        add(skeleton);
    }
}
