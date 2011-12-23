package net.gtamps.android.game.content;

import net.gtamps.android.core.renderer.graph.RenderableNode;
import net.gtamps.shared.Config;
import net.gtamps.shared.Utils.IDirty;
import net.gtamps.shared.game.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityView implements IDirty {

    /**
     * Current Entity for visual display.
     */
    @NotNull
    public Entity entity;

    private RenderableNode object3d;

    private boolean isDirty = true;

    /**
     * Constructs the view.
     *
     * @param entity
     */
    public EntityView(@NotNull Entity entity) {
        this.entity = entity;
        object3d = Object3dFactory.create(entity.type);
        isDirty = true;
        onDirty();
    }

    @Deprecated
    public EntityView(RenderableNode node) {
        entity = new Entity("bla");
        object3d = node;
    }

    public void update(Entity serverEntity) {
        entity = serverEntity;
        setDirtyFlag();
        onDirty();
    }

    @Override
    public void onDirty() {

        // position
        object3d.setPosition(entity.x.value() * Config.PIXEL_TO_NATIVE, entity.y.value() * Config.PIXEL_TO_NATIVE, entity.z.value() * Config.PIXEL_TO_NATIVE);
        // scaling
//        object3d.setScaling(1, 1, 1);
//        rotation
        object3d.setRotation(0, 0, entity.rota.value()+Config.SERVER_ANGLE_TO_NATIVE);

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

    public RenderableNode getObject3d() {
        return object3d;
    }

    public void setObject3d(RenderableNode object3d) {
        this.object3d = object3d;
    }
}
