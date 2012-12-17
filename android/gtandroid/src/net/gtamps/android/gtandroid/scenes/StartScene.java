package net.gtamps.android.gtandroid.scenes;

import net.gtamps.android.graphics.R;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Cube;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe
 * Date: 17/12/12
 * Time: 09:51
 */
public class StartScene extends SceneGraph {

    public StartScene() {
        super(new Camera(0, 0, 10, 0, 0, -1, 0, 1, 0));
//        getActiveCamera().setOrthographicView();
    }

    @Override
    protected void onSurfaceCreatedInternal(GL10 gl10) {

        Light light = new Light(0, 0, 10, 0, 0, -1);
        light.setParent(getActiveCamera());
        add(light);

        Cube cube = new Cube();
        cube.addTexture(new Texture(R.drawable.crate, Texture.Type.u_Texture01));
//        cube.getRenderState().setShader(Shader.Type.PHONG);
        add(cube);


        Logger.V(this, "World has been created.");
    }
}
