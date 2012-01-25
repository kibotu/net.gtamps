package net.gtamps.android.renderer.test.scene;

import net.gtamps.android.renderer.graph.scene.BasicScene;
import net.gtamps.android.renderer.graph.scene.primitives.*;
import net.gtamps.android.renderer.mesh.texture.TextureSample;
import net.gtamps.android.renderer.test.R;
import net.gtamps.shared.Utils.math.Color4;

import static net.gtamps.android.graphic.R.drawable.crate;
import static net.gtamps.android.graphic.R.drawable.earth;

public class World extends BasicScene {

    public World() {
    }

    @Override
    public void onCreate() {

        setActiveCamera(new Camera(0, 0, 25, 0, 0, -1, 0, 1, 0));
        setBackground(Color4.DARK_GRAY);

        Cube cube = new Cube();
        cube.setPosition(-3, -3, 0);
        cube.addTexture(new TextureSample(crate, TextureSample.Type.texture_01, true));

        Sphere sphere = new Sphere(1, 20, 20);
        sphere.setPosition(3, -3, 0);
        sphere.addTexture(new TextureSample(earth, TextureSample.Type.texture_01, true));

        Torus torus = new Torus();
        torus.setPosition(3, 3, 0);
        torus.addTexture(new TextureSample(crate, TextureSample.Type.texture_01, true));

        Cylinder cylinder = new Cylinder();
        cylinder.setPosition(-3, 3, 0);
        cylinder.addTexture(new TextureSample(crate, TextureSample.Type.texture_01, true));

        SkyBox skyBox = new SkyBox();
        skyBox.setScaling(3, 3, 3);
        skyBox.enableLighting(false);

        add(torus);
        add(cube);
        add(sphere);
        add(cylinder);
//        add(skyBox);

    }

    @Override
    public void onDirty() {
    }
}
