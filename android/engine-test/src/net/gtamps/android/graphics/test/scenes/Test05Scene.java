package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.R;
import net.gtamps.android.graphics.graph.RootNode;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.primitives.Cylinder;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.shared.Utils.Logger;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test05Scene extends SceneGraph {

    public Test05Scene() {
        super(new Camera(0, 0, 15, 0, 0, -1, 0, 1, 0));
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        add(new Light(0, 0, 10, 0, 0, -1));
        Texture crateTexture = new Texture(R.drawable.crate, Texture.Type.u_Texture01);

        float scale = 0.3f;
        int counter = 0;
        Cylinder obj = new Cylinder();
        obj.addTexture(crateTexture);

        for (int x = -3; x < 3; x++) {
            for (int y = -3; y < 3; y++) {
                for (int z = -3; z < 3; z++) {
                    RootNode node = new RootNode();
                    node.setPosition(x, y, z);
                    node.add(obj);
                    node.setScaling(scale, scale, scale);
                    add(node);
                    counter++;
                }
            }
        }

        counter++;

        Logger.V(this, counter + " Cylinder created.");
    }
}
