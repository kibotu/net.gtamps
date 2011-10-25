package net.gtamps.android.game;

import net.gtamps.android.core.renderer.graph.SceneGraph;
import net.gtamps.android.game.entity.views.EntityView;
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

    public int getObjects3dCount() {
        return object3ds.size();
    }

    public EntityView getEntityView(int index) {
        IObject3d iObject3d = object3ds.get(index);
            if(iObject3d instanceof EntityView) {
                return (EntityView) iObject3d;
            }
        return null;
    }

    public EntityView getObject3DById(int entityUid) {
        for(int i = 0; i < object3ds.size(); i++) {
            IObject3d iObject3d = object3ds.get(i);
            if(iObject3d instanceof EntityView) {
                if(((EntityView)iObject3d).entity.getUid() == entityUid) {
                    return (EntityView)iObject3d;
                }
            }
        }
        return null;
    }
}
