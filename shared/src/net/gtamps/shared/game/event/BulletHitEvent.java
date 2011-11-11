package net.gtamps.shared.game.event;

import net.gtamps.shared.game.GameObject;

public class BulletHitEvent extends GameEvent {

    private static final long serialVersionUID = 6778477773135704293L;
    protected final float impulse;

    public BulletHitEvent(GameObject source, GameObject target, float impulse) {
        super(EventType.ENTITY_BULLET_HIT, source, target);
        this.impulse = impulse;
    }

    public float getImpulse() {
        return this.impulse;
    }
}
