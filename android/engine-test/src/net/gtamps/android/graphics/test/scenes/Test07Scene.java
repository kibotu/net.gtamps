package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureAnimation;
import net.gtamps.android.graphics.graph.scene.primitives.Cylinder;
import net.gtamps.android.graphics.graph.scene.primitives.camera.Camera;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Plane;
import net.gtamps.android.graphics.test.R;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.state.State;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test07Scene extends SceneGraph {

    public Test07Scene() {
        super(new Camera(0, 0, 10, 0, 0, -1, 0, 1, 0));
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        add(new Light(0, 0, 10, 0, 0, -1));

        Texture spritesheet = new Texture(R.drawable.spritesheet, Texture.Type.texture_01);
        TextureAnimation uvsheet = TextureAnimation.load(R.raw.advocate);

        float scale = 2f;
        int counter = 0;

        for (int x = -3; x < 3; x++) {
            for (int y = -3; y < 3; y++) {
                for (int z = -3; z < 3; z++) {
                    Plane obj = new Plane();
                    obj.setPosition(x, y, z);
                    obj.setScaling(scale, scale, scale);
                    obj.addTexture(spritesheet);
                    obj.addTextureAnimation(uvsheet);
                    obj.animate(State.Type.IDLE, 0.1f);
                    add(obj);
                    counter++;
                }
            }
        }

        Logger.V(this, counter + " Sprites created.");
    }
}