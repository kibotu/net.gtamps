package net.gtamps.android;

import net.gtamps.android.core.graph.CameraNode;
import net.gtamps.android.core.graph.LightNode;
import net.gtamps.android.game.Scene;
import net.gtamps.android.game.objects.Car;
import net.gtamps.android.game.objects.City;
import net.gtamps.android.game.objects.IObject3d;

public class World {

    private CameraNode camera;
    private Scene scene;
    private LightNode light;
    private IObject3d activeObject;

    public World() {
    }

    public void init() {

         // world
        scene = new Scene();

        camera =  new CameraNode(0, 0,40, 0, 0, 0, 0, 0, 1);
        scene.setActiveCamera(camera);
        scene.getBackground().setAll(0x111111);

        scene.addChild(activeObject = new Car());

        light = new LightNode();
        light.setPosition(0,0,30);
        light.setDirection(0, 0, -1);
        light.ambient.setAll(64, 64, 64, 255);
        light.diffuse.setAll(128,128,128,255);
        light.specular.setAll(64,64,64,255);
        scene.add(light);

//        addCity();
    }

    private void addCity() {
        City city = new City();
        scene.add(city);
        city.setRotation(90, 0, 0);
        city.setScaling(20, 20, 20);
    }

    public Scene getScene() {
        if(scene==null) {
            init();
        }
        return scene;
    }

    public IObject3d getActiveObject() {
        return activeObject;
    }
}
