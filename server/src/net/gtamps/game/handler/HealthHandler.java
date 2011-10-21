package net.gtamps.game.handler;

import net.gtamps.game.conf.WorldConstants;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.BulletHitEvent;
import net.gtamps.shared.game.event.CollisionEvent;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

public class HealthHandler extends Handler {

	private final Propertay<Boolean> isAlive;
	private final Propertay<Integer> health;
	private final Propertay<Float> healthRatio;
	private final int maxHealth;
	private final int dmgThreshold;
	private final float dmgResistance;
	
	public HealthHandler(final Entity parent, final int maxHealth, final float resistance, final int threshold) {
		super(Handler.Type.HEALTH, parent);
		this.maxHealth = maxHealth;
		dmgThreshold = threshold;
		dmgResistance = resistance;
		isAlive = super.useParentProperty("isAlive", maxHealth > 0 ? true : false);
		health = super.useParentProperty("health", maxHealth);
		healthRatio = super.useParentProperty("healthRatio", 1f);
		setHealth(maxHealth);
	}
	
	@Override
	public String toString() {
		final String s = super.toString();
		final int split = s.indexOf(' ');
		final String head = s.substring(0, split);
		final String tail = s.substring(split);
		return head + " health:" + health.value() + tail;
	}
	
	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		if (type.isType(EventType.ENTITY_COLLIDE)) {
			final float impulse = ((CollisionEvent)event).getImpulse();
			takeDamage(impulse);
		}
		if (type.isType(EventType.ENTITY_BULLET_HIT)) {
			final float impulse = ((BulletHitEvent)event).getImpulse();
			takeDamage(impulse*WorldConstants.BULLET_IMPULSE_DAMAGE_AMPLIFICATION);
			if(parent.getName().equals("bullet")){
				setHealth(0);
				(parent).destroy();
			}
		}
	}
	
	public boolean isAlive() {
		return isAlive.value();
	}

	private void takeDamage(final float force) {
		final int damage = (int) (force * (1-dmgResistance) - dmgThreshold);
		if (damage > 0) {
			setHealth(health.value() - damage);
		}
	}
	
	private void setHealth(int value) {
		if (value < 0) {
			value = 0;
		}
		health.set(value);
		isAlive.set(value > 0);
		healthRatio.set((float)value / maxHealth);
		if(value == 0) {
			die();
		}
	}
	
	private void die() {
		final GameEvent death = new GameEvent(EventType.ENTITY_DESTROYED, parent);
		dispatchEvent(death);
	}
	
	
}