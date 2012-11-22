package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.R;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Cube;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test02Scene extends SceneGraph {

    public Test02Scene() {
        super(new Camera(0, 0, 15, 0, 0, -1, 0, 1, 0));
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        add(new Light(0, 0, 10, 0, 0, -1));

        Texture crateTexture = new Texture(R.drawable.crate, Texture.Type.texture_01);

        float scale = 0.3f;

        int counter = 0;

        for (int x = -3; x < 3; x++) {
            for (int y = -3; y < 3; y++) {
                for (int z = -3; z < 3; z++) {
                    Cube obj = new Cube();
                    obj.setPosition(x, y, z);
                    obj.setScaling(scale, scale, scale);
                    obj.addTexture(crateTexture);
                    add(obj);
                    counter++;
                }
            }
        }

        Logger.V(this, counter + " Cubes created.");
    }
}
