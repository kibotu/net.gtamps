package net.gtamps.android.game.scenes;

import net.gtamps.android.core.renderer.graph.SceneNode;
import net.gtamps.android.game.objects.EntityView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class EntityScene extends BasicScene {

    protected final ArrayList<EntityView> views;

    public EntityScene() {
        super();
        views = new ArrayList<EntityView>();
    }

    final public EntityView getView(int location) {
        return views.get(location);
    }

    final public void add(@NotNull SceneNode node) {
        throw new IllegalArgumentException("Not supported scene node for entity scene");
    }

    final public void remove(@NotNull SceneNode node) {
        throw new IllegalArgumentException("Not supported scene node for entity scene");
    }

    final public void add(@NotNull EntityView view) {
        views.add(view);
        super.add(view.getObject3d());
    }

    final public void remove(@NotNull EntityView view) {
        views.remove(view);
        super.remove(view.getObject3d());
    }

    final public int size() {
        return views.size();
    }
}
