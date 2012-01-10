package net.gtamps.android.renderer.test.scene;

import net.gtamps.android.renderer.graph.scene.BasicScene;
import net.gtamps.android.renderer.graph.scene.primitives.*;
import net.gtamps.android.renderer.test.R;
import net.gtamps.shared.Utils.math.Color4;

public class World extends BasicScene {

    public World() {
    }

    @Override
    public void onCreate() {

        setActiveCamera(new Camera(0, 0, 25, 0, 0, -1, 0, 1, 0));
        setBackground(Color4.DARK_GRAY);

        Cube cube = new Cube();
        cube.setPosition(-3, -3, 0);
        cube.setTextureResourceId(R.drawable.crate);

        Sphere sphere = new Sphere(1, 20, 20);
        sphere.setPosition(3, -3, 0);
        sphere.setTextureResourceId(R.drawable.earth);

        Torus torus = new Torus();
        torus.setPosition(3, 3, 0);
        torus.setTextureResourceId(R.drawable.crate);

        Cylinder cylinder = new Cylinder();
        cylinder.setPosition(-3, 3, 0);
        cylinder.setTextureResourceId(R.drawable.crate);

        add(torus);
        add(cube);
        add(sphere);
        add(cylinder);
    }

    @Override
    public void onDirty() {
    }
}
