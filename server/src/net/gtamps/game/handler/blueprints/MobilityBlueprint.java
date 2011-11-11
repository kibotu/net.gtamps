package net.gtamps.game.handler.blueprints;

import net.gtamps.game.handler.MobilityHandler;
import net.gtamps.game.handler.SimplePhysicsHandler;
import net.gtamps.game.physics.MobilityProperties;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.handler.HandlerBlueprint;

public class MobilityBlueprint extends HandlerBlueprint {

    private final MobilityProperties mobProp;

    public MobilityBlueprint(final MobilityProperties mobProp) {
        super(Handler.Type.MOBILITY);
        this.mobProp = mobProp;
    }

    public MobilityBlueprint(final MobilityBlueprint other) {
        this(other.mobProp);
    }

    @Override
    public Handler createHandler(final Entity parent, final Integer pixX, final Integer pixY, final Integer deg) {
        final MobilityHandler mobh = new MobilityHandler(parent, mobProp, (SimplePhysicsHandler) parent.getHandler(Handler.Type.PHYSICS));
        return mobh;
    }

    @Override
    public HandlerBlueprint copy() {
        return new MobilityBlueprint(this);
    }


}
