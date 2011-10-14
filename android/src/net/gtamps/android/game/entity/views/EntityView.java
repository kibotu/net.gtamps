package net.gtamps.android.game.entity.views;

import net.gtamps.android.World;
import net.gtamps.android.core.graph.SceneNode;
import net.gtamps.android.core.utils.Utils;
import net.gtamps.android.game.Game;
import net.gtamps.shared.Config;
import net.gtamps.shared.IDirty;
import net.gtamps.android.game.objects.IObject3d;
import net.gtamps.android.game.objects.Object3dFactory;
import net.gtamps.shared.game.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityView implements IObject3d, IDirty {

    private static final String TAG = EntityView.class.getSimpleName();

    /**
     * Current Entity for visual display.
     */
    @NotNull
    public Entity entity;

    private IObject3d object3d;

    private boolean isDirty;

    /**
     * Constructs the view.
     *
     * @param entity
     */
    public EntityView(@NotNull Entity entity) {
        this.entity = entity;
        Utils.log(TAG, "\n\n\nENTITY TYPE: "+entity.type+"\n\n");
        object3d = Object3dFactory.create(entity.type);
        isDirty = true;
        onDirty();
    }

    @Override
    public SceneNode getNode() {
        return object3d.getNode();
    }

    public void update(Entity serverEntity) {
        setDirtyFlag();
        entity = serverEntity;
        onDirty();
    }

    public void setAsActiveObject() {
        World.activeObject = this;
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
        Utils.log(TAG, "update " +entity);
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
