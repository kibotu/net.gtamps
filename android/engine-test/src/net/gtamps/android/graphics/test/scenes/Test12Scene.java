package net.gtamps.android.graphics.test.scenes;

import com.badlogic.gdx.math.MathUtils;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.animation.rigged.RiggedObject3D;
import net.gtamps.android.graphics.graph.scene.mesh.parser.lolreader.*;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Plane;
import net.gtamps.android.graphics.graph.scene.primitives.Skybox;
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
        super(new Camera(0, 1, -7, 0, 1, 0, 0, 1, 0));

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

        SKNFile skn = SKNReader.readBinary(PACKAGE_NAME + "ahri_fixed_skn");
        SKLFile skl = SKLReader.readBinary(PACKAGE_NAME + "ahri_fixed_skl");
//        ANMFile anm_idle = ANMReader.readBinary(PACKAGE_NAME + "ahri_idle1_anm");
        ANMFile anm_dance = ANMReader.readBinary(PACKAGE_NAME + "ahri_dance_fixed_anm");
//        ANMFile anm_attack1 = ANMReader.readBinary(PACKAGE_NAME + "ahri_attack1_anm");
//        ANMFile anm_attack2 = ANMReader.readBinary(PACKAGE_NAME + "ahri_attack2_anm");

        HashMap<String, ANMFile> animations = new HashMap<String, ANMFile>();
//        animations.put(PACKAGE_NAME + "ahri_idle1_anm", anm_idle);
        animations.put(PACKAGE_NAME + "ahri_dance_fixed_anm", anm_dance);
//        animations.put(PACKAGE_NAME + "ahri_attack1_anm", anm_attack1);
//        animations.put(PACKAGE_NAME + "ahri_attack2_anm", anm_attack2);

        RiggedObject3D ahri = new RiggedObject3D();
        ahri.create(skn, skl, animations);
        ahri.setScaling(0.01f, 0.01f, 0.01f);
        ahri.addTexture(new Texture(R.drawable.ahri, Texture.Type.u_Texture01, true));
        ahri.getRenderState().setShader(Shader.Type.PHONG);

        //add obj to scene
        RootNode ahri1 = new RootNode();
        ahri1.add(ahri);
        ahri1.setPosition(-4f, 0, 0);
        add(ahri1);

        RootNode ahri2 = new RootNode();
        ahri2.add(ahri);
        add(ahri2);

        RootNode ahri3 = new RootNode();
        ahri3.add(ahri);
        ahri3.setPosition(4f, 0, 0);
        add(ahri3);

        Plane ground = new Plane();
        ground.setRotation(MathUtils.degreesToRadians * 90, 0, 0);
        ground.setDimension(20, 20, 20);
        ground.getRenderState().setShader(Shader.Type.PHONG);
        ground.addTexture(new Texture(R.drawable.stonebg, Texture.Type.u_Texture01, true));
        add(ground);

        Skybox skybox = new Skybox();
        skybox.addTexture(new Texture(R.drawable.night_positive_z), Skybox.Face.NEAR_POSITIVE_Z);
        skybox.addTexture(new Texture(R.drawable.night_negative_z), Skybox.Face.FAR_NEGATIVE_Z);
        skybox.addTexture(new Texture(R.drawable.night_negative_y), Skybox.Face.BOTTOM_NEGATIVE_Y);
        skybox.addTexture(new Texture(R.drawable.night_positive_y), Skybox.Face.TOP_POSITIVE_Y);
        skybox.addTexture(new Texture(R.drawable.night_negative_x), Skybox.Face.LEFT_NEGATIVE_X);
        skybox.addTexture(new Texture(R.drawable.night_positive_x), Skybox.Face.RIGHT_POSITIVE_X);
        skybox.setDimension(30, 30, 30);
        add(skybox);
    }
}
