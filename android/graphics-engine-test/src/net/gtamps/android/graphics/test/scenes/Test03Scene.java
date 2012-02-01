package net.gtamps.android.graphics.test.scenes;

import net.gtamps.android.graphics.R;
import net.gtamps.android.graphics.graph.scene.SceneGraph;
import net.gtamps.android.graphics.graph.scene.mesh.texture.TextureSample;
import net.gtamps.android.graphics.graph.scene.primitives.Camera;
import net.gtamps.android.graphics.graph.scene.primitives.Light;
import net.gtamps.android.graphics.graph.scene.primitives.Sphere;

import javax.microedition.khronos.opengles.GL10;

/**
 * User: Jan Rabe, Tom Walroth, Til Börner
 * Date: 31/01/12
 * Time: 18:48
 */
public class Test03Scene extends SceneGraph {

    public Test03Scene() {
        super(new Camera(0, 0, 10, 0, 0, -1, 0, 1, 0));
    }

    @Override
    public void onSurfaceCreatedInternal(GL10 gl10) {

        add(new Light(0, 0, 10, 0, 0, -1));

        // creating objects
        Sphere sphere = new Sphere();
        sphere.setPosition(0, 0, -10);
        Sphere sphere2 = new Sphere();
        sphere2.setPosition(-5, 0, -10);
        Sphere sphere3 = new Sphere();
        sphere3.setPosition(5, 0, -10);

        // adding texture
        sphere.addTexture(new TextureSample(R.drawable.earth, TextureSample.Type.texture_01));
        sphere2.addTexture(sphere.getTextureSamples());
        sphere3.addTexture(sphere.getTextureSamples());

        // add to scene
        add(sphere);
        add(sphere2);
        add(sphere3);
    }
}
