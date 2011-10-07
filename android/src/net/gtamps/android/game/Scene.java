package net.gtamps.android.game;

import net.gtamps.android.core.graph.SceneGraph;
import net.gtamps.android.game.objects.IObject3d;

import java.util.ArrayList;

public class Scene extends SceneGraph {

    private ArrayList<IObject3d> object3ds;

    public Scene() {
        super();
        object3ds = new ArrayList<IObject3d>();
    }

    public IObject3d getObject3D(int location) {
        return object3ds.get(location);
    }

    public void addChild(IObject3d object3D) {
        object3ds.add(object3D);
        add(object3D.getNode());
    }

    public void removeChild(IObject3d object3D) {
        object3ds.remove(object3D);
        remove(object3D.getNode());
    }
}
