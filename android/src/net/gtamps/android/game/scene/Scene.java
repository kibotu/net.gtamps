package net.gtamps.android.game.scene;

import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.android.core.renderer.graph.SceneGraph;
import net.gtamps.android.core.renderer.graph.SceneNode;
import net.gtamps.android.core.renderer.graph.primitives.Camera;
import net.gtamps.android.game.objects.EntityView;
import net.gtamps.shared.math.Color4;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Scene {

    private SceneGraph sceneGraph;
    private ArrayList<RenderableNode> object3ds;

    public Scene() {
        sceneGraph = new SceneGraph();
        object3ds = new ArrayList<RenderableNode>();
    }

    public RenderableNode getObject3D(int location) {
        return object3ds.get(location);
    }

    public void addChild(RenderableNode object3D) {
        object3ds.add(object3D);
        sceneGraph.add(object3D);
    }

    public void removeChild(RenderableNode object3D) {
        object3ds.remove(object3D);
        sceneGraph.remove(object3D);
    }

    public int getObjects3dCount() {
        return object3ds.size();
    }

    public EntityView getEntityView(int index) {
//        Object3d iObject3d = object3ds.get(index);
//            if(iObject3d instanceof EntityView) {
//                return (EntityView) iObject3d;
//            }
        return null;
    }

    public EntityView getObject3DById(int entityUid) {
//        for(int i = 0; i < object3ds.size(); i++) {
//            Object3d iObject3d = object3ds.get(i);
//            if(iObject3d instanceof EntityView) {
//                if(((EntityView)iObject3d).entity.getUid() == entityUid) {
//                    return (EntityView)iObject3d;
//                }
//            }
//        }
        return null;
    }

    public void setActiveCamera(Camera camera) {
        sceneGraph.setActiveCamera(camera);
    }

    public Camera getActiveCamera() {
        return sceneGraph.getActiveCamera();
    }

    public void addNode(@NotNull SceneNode node) {
        sceneGraph.add(node);
    }

    public void removeNode(@NotNull SceneNode node) {
        sceneGraph.remove(node);
    }

    public Color4 getBackground() {
        return sceneGraph.getBackground();
    }

    public SceneGraph getSceneGraph() {
        return sceneGraph;
    }
}
