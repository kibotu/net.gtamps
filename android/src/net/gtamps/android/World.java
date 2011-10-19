package net.gtamps.android;

import net.gtamps.android.core.graph.CameraNode;
import net.gtamps.android.core.graph.LightNode;
import net.gtamps.android.game.Scene;
import net.gtamps.android.game.objects.City;
import net.gtamps.android.game.objects.IObject3d;
import net.gtamps.android.game.objects.Object3dFactory;
import net.gtamps.shared.game.entity.Entity;

public class World {

    private CameraNode camera;
    private Scene scene;
    private LightNode light;
    public static IObject3d activeObject;

    public World() {
    }

    public void init() {

         // world
        scene = new Scene();

        camera =  new CameraNode(0, 0,40, 0, 0, 0, 0, 0, 1);
        scene.setActiveCamera(camera);
        scene.getBackground().setAll(0x111111);

        scene.addChild(activeObject = Object3dFactory.create(Entity.Type.CAR_CHEVROLET_CORVETTE));
        IObject3d camaro = Object3dFactory.create(Entity.Type.CAR_CAMARO);
        camaro.getNode().setPosition(-15,0,0);
        camaro.getNode().setScaling(5,5,5);
        IObject3d riviera = Object3dFactory.create(Entity.Type.CAR_RIVIERA);
        riviera.getNode().setPosition(15,0,0);
        riviera.getNode().setScaling(35,35,35);
        scene.addChild(camaro);
        scene.addChild(riviera);
//
//        IParser objParser = Parser.createParser(Parser.Type.OBJ, "net.gtamps.android:raw/camaro_obj", true);
//        objParser.parse();
//        scene.addChild(objParser.getParsedObject());

        light = new LightNode();
        light.setPosition(0,0,0);
        light.setDirection(0, 0, -1);
        light.ambient.setAll(64, 64, 64, 255);
        light.diffuse.setAll(128,128,128,255);
        light.specular.setAll(64,64,64,255);
        scene.add(light);

        addCity();
    }

    private void addCity() {
        City city = new City();
        scene.add(city);
        city.setRotation(90, 0, 0);
        city.setScaling(100, 2, 100);
    }

    public Scene getScene() {
        if(scene==null) {
            init();
        }
        return scene;
    }

    public static IObject3d getActiveObject() {
        return activeObject;
    }
}
