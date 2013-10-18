package net.gtamps.android.graphics.test.scenes;

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
import net.gtamps.shared.Utils.math.MathUtils;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe
 * Date: 05/12/12
 * Time: 10:30
 */
public class Test14Scene extends SceneGraph {

    public Test14Scene() {
        super(new Camera(0, 0, 10, 0, 0, -10, 0, 1, 0));

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

        Object3D heart = new Object3D(PACKAGE_NAME + "heart_obj");
        heart.addTexture(new Texture(R.drawable.red_color, Texture.Type.u_Texture01, true));
        heart.setRotation(MathUtils.DEG_TO_RAD * -90, MathUtils.DEG_TO_RAD * -90, MathUtils.DEG_TO_RAD * 90);
        heart.getRenderState().setShader(Shader.Type.PHONG);

        Skybox skybox = new Skybox();
        skybox.addTexture(new Texture(R.drawable.night_positive_z), Skybox.Face.NEAR_POSITIVE_Z);
        skybox.addTexture(new Texture(R.drawable.night_negative_z), Skybox.Face.FAR_NEGATIVE_Z);
        skybox.addTexture(new Texture(R.drawable.night_negative_y), Skybox.Face.BOTTOM_NEGATIVE_Y);
        skybox.addTexture(new Texture(R.drawable.night_positive_y), Skybox.Face.TOP_POSITIVE_Y);
        skybox.addTexture(new Texture(R.drawable.night_negative_x), Skybox.Face.LEFT_NEGATIVE_X);
        skybox.addTexture(new Texture(R.drawable.night_positive_x), Skybox.Face.RIGHT_POSITIVE_X);
        skybox.setDimension(30, 30, 30);
        add(skybox);

        RootNode heartNode = new RootNode();
        heartNode.add(heart);
        heartNode.setPosition(0, 0, 0);
        heartNode.setScaling(0.3f,0.3f,0.3f);

        add(heartNode);
    }
}
