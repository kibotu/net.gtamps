package net.gtamps.game.handler;

import net.gtamps.game.universe.Universe;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.serializer.communication.StringConstants;

public class HealthHandler extends ServersideHandler<Entity> {
	private static final LogType TAG = LogType.GAMEWORLD;

	private static final EventType[] eventsReceived = {EventType.ACTION_SUICIDE, EventType.ENTITY_DAMAGE};

	private final IProperty<Boolean> isAlive;
	private final IProperty<Integer> health;
	private final IProperty<Float> healthRatio;
	private final int maxHealth;
	private final int dmgThreshold;
	private final float dmgMultiplier;

	public HealthHandler(final Entity parent, final int maxHealth, final Universe universe) {
		this(universe, parent, maxHealth, 1.0f, 0);
	}

	public HealthHandler(final Universe universe, final Entity parent, final int maxHealth, final float dmgMultiplier, final int threshold) {
		super(universe, Handler.Type.HEALTH, parent, eventsReceived);
		if (maxHealth < 0) {
			throw new IllegalArgumentException("'maxHealth' must be >= 0");
		}
		if (dmgMultiplier < 0f) {
			throw new IllegalArgumentException("'dmgMultiplier' must be > 0");
		}
		this.maxHealth = maxHealth;
		dmgThreshold = threshold;
		this.dmgMultiplier = dmgMultiplier;
		isAlive = parent.useProperty(StringConstants.PROPERTY_ALIVE, maxHealth > 0 ? true : false);
		health = parent.useProperty(StringConstants.PROPERTY_HEALTH, maxHealth);
		healthRatio = parent.useProperty(StringConstants.PROPERTY_HEALTHRATIO, 1f);
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
		if (type.isType(EventType.ENTITY_DAMAGE) && parent.getUid() == event.getTargetUid()) {
			final int dmg = Integer.valueOf(event.useProperty(StringConstants.PROPERTY_VALUE, "0").value());
			takeDamage(dmg);
			//		} else if (type.isType(EventType.ENTITY_BULLET_HIT)) {
			//			final float impulse = ((BulletHitEvent) event).getImpulse();
			//			takeDamage(impulse * WorldConstants.BULLET_IMPULSE_DAMAGE_AMPLIFICATION);
			//			if (parent.getName().equals("bullet")) {
			//				setHealth(0);
			//				(parent).destroy();
			//			}
		} else if (type.isType(EventType.ACTION_SUICIDE)) {
			setHealth(0);
		}
	}

	public boolean isAlive() {
		return isAlive.value();
	}

	private void takeDamage(final float force) {
		final int damage = (int) (force * dmgMultiplier - dmgThreshold);
		if (damage > 0) {
			setHealth(health.value() - damage);
			GUILogger.getInstance().log(TAG, parent.getName() + " takes " + damage + " damage");
		}
	}

	private void setHealth(int value) {
		if (value < 0) {
			value = 0;
		}
		health.set(value);
		isAlive.set(value > 0);
		healthRatio.set(maxHealth == 0 ? 0 : (float) value / maxHealth);
		if (value == 0) {
			die();
		}
	}

	private void die() {
		final GameEvent death = new GameEvent(EventType.ENTITY_DESTROYED, parent);
		System.out.println(parent + " death! destruction! calamity!");

		//TODO
		//FIXME queue events, implement eventHandling phase to decouple from physics

		//		eventRoot.dispatchEvent(death);
	}


}
