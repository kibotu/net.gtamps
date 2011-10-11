package net.gtamps.android.game.entity.views;

import net.gtamps.android.core.graph.SceneNode;
import net.gtamps.shared.Config;
import net.gtamps.shared.IDirty;
import net.gtamps.android.game.objects.IObject3d;
import net.gtamps.android.game.objects.Object3dFactory;
import net.gtamps.shared.game.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityView implements IObject3d, IDirty {

    /**
     * Current Entity for visual display.
     */
    @NotNull
    private Entity entity;

    private IObject3d object3d;

    private boolean isDirty;

    /**
     * Constructs the view.
     *
     * @param entity
     */
    public EntityView(@NotNull Entity entity) {
        this.entity = entity;
        object3d = Object3dFactory.create(entity.type);
        isDirty = true;
    }

    @Override
    public SceneNode getNode() {
        return object3d.getNode();
    }

    public void update(Entity serverEntity) {
        setDirtyFlag();
        entity = serverEntity;
    }

    @Override
    public void onDirty() {

        // position
        object3d.getNode().setPosition(entity.x.value()* Config.PIXEL_TO_NATIVE,entity.y.value()* Config.PIXEL_TO_NATIVE,entity.z.value()* Config.PIXEL_TO_NATIVE);
        // scaling
        object3d.getNode().setScaling(1,1,1);
//        rotation
        object3d.getNode().setRotation(0,0,entity.rota.value());

        clearDirtyFlag();
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void setDirtyFlag() {
        isDirty = true;
    }

    @Override
    public void clearDirtyFlag() {
        isDirty = false;
    }
}
