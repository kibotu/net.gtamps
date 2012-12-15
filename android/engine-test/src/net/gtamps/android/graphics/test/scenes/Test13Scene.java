package net.gtamps.android.graphics.test.scenes;

import com.badlogic.gdx.math.MathUtils;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Object3D;
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

/**
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:30
 */
public class Test13Scene extends SceneGraph {

    public Test13Scene() {
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
        Light light = new Light(0, 10, 0, 0, -1, 0);
        add(light);

        // hang light on camera
        light.setParent(getActiveCamera());

        // Package where the obj file is located. Needed for context loader.
        String PACKAGE_NAME = "net.gtamps.android.graphics.test:raw/";

        Object3D camaro = new Object3D(PACKAGE_NAME + "camaro_obj");
        camaro.addTexture(new Texture(R.drawable.camaro, Texture.Type.u_Texture01, true));
        camaro.setRotation(MathUtils.degreesToRadians*-90,0,0);
        camaro.getRenderState().setShader(Shader.Type.PHONG);

        Plane ground = new Plane();
        ground.setRotation(MathUtils.degreesToRadians*90,0,0);
        ground.setDimension(10,10,10);
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

        RootNode camaro1 = new RootNode();
        camaro1.add(camaro);
        camaro1.setPosition(-2.5f,0,0);
        RootNode camaro2 = new RootNode();
        camaro2.add(camaro);
        RootNode camaro3 = new RootNode();
        camaro3.add(camaro);
        camaro3.setPosition(2.5f,0,0);

        add(camaro1);
        add(camaro2);
        add(camaro3);
    }
}
