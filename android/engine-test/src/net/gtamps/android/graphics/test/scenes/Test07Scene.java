package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.mesh.texture.Texture;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureAnimation;
import net.gtamps.android.graphics.graph.scene.primitives.Camera;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Plane;
import net.gtamps.android.graphics.test.R;
import net.gtamps.shared.game.state.State;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test07Scene extends SceneGraph {

    public Test07Scene() {
        super(new Camera(0, 0, 4, 0, 0, -1, 0, 1, 0));
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        add(new Light(0, 0, 10, 0, 0, -1));

        Texture spritesheet = new Texture(R.drawable.spritesheet, Texture.Type.texture_01);
        TextureAnimation uvsheet = TextureAnimation.load(R.raw.advocate);

        Plane sprite = new Plane();
        sprite.addTexture(spritesheet);
        sprite.addTextureAnimation(uvsheet);
        sprite.animate(State.Type.IDLE, 0.1f);

        add(sprite);
    }
}
