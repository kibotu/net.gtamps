package net.gtamps.shared.game.level;

import net.gtamps.shared.Utils.math.Vector3;
import net.gtamps.shared.game.entity.Entity.Type;

import java.io.Serializable;

public class EntityPosition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -893131986434637247L;
    Vector3 position;
    Type type;
    float rotation;

    public EntityPosition(float x, float y, float z, float rotation, Type type) {
        this.position = Vector3.createNew(x, y, z);
        this.rotation = rotation;
        this.type = type;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Type getType() {
        return type;
    }
}
