package net.gtamps.android.game.content.scenes;

import net.gtamps.android.core.renderer.graph.SceneNode;
import net.gtamps.android.core.renderer.graph.scene.BasicScene;
import net.gtamps.android.game.content.EntityView;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class EntityScene extends BasicScene {

    protected final HashMap<Integer, EntityView> views;

    public EntityScene() {
        super();
        views = new HashMap<Integer, EntityView>(150);
    }

    final public void add(@NotNull SceneNode node) {
        throw new IllegalArgumentException("Not supported scene node for entity scene");
    }

    final public void remove(@NotNull SceneNode node) {
        throw new IllegalArgumentException("Not supported scene node for entity scene");
    }

    final public void add(@NotNull EntityView view) {
        views.put(view.entity.getUid(), view);
        super.add(view.getObject3d());
    }

    final public void remove(@NotNull EntityView view) {
        views.remove(view);
        super.remove(view.getObject3d());
    }

    final public int size() {
        return views.size();
    }

    public EntityView getViewById(int uid) {
        return views.get(uid);
    }
}
