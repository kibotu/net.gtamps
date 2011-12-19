package net.gtamps.game.handler;

import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.game.player.PlayerManager;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.player.Player;

import org.jbox2d.common.Vec2;

/**
 * Enables an entity to be "entered" by another (player-controlled) entity,
 * which is used by cars, hence the name: <tt>DriverHandler</tt>. Theoretically,
 * an entity with a handler like this need not necessarily be "drivable"
 * or in any other way controllable; it could be a restroom stall, for all
 * it matters.
 * <p>
 * Triggered by a call to its {@link #enter(Player)} function,
 * this handler takes the entity currently controlled by the player,
 * disables it and stores it in a safe place. Then, player control is
 * transferred to the entity this handler belongs to. The whole process
 * is reversed once an {@link EventType#ACTION_ENTEREXIT enter/exit command}
 * is received. The formerly controlled entity is pulled out of storage, put
 * next to the active one, and re-attached to the player.
 * </p><p>
 * There is an enforced delay between succeeding enter/exit activities
 * with the same handler, which is controlled by {@link #COOLDOWN_MILLIS}.
 * </p>
 *
 * @author jan, tom, til
 * @see Entity
 * @see Player
 */
public class DriverHandler extends Handler {
	private static final LogType TAG = LogType.GAMEWORLD;
	private static final int COOLDOWN_MILLIS = 500;

	private static final Vec2 exitOffset = new Vec2(30f, 0);
	private static final int exitRotation = 90;

	private Player driver = null;
	private Entity driversHumanBody = null;
	private long lastActivationMillis = 0;
	private final EventType[] sendsUp = {EventType.PLAYER_ENTERSCAR,
			EventType.PLAYER_EXITSCAR};
	private final EventType[] receivesDown = {EventType.ENTITY_DESTROYED,
			EventType.ACTION_ENTEREXIT};

	public DriverHandler(final IGameEventDispatcher eventRoot, final Entity parent) {
		super(eventRoot, Handler.Type.DRIVER, parent);
		setReceives(receivesDown);
		connectUpwardsActor(parent);
	}

	public boolean isAvailable() {
		return driver == null || driver == PlayerManager.WORLD_PSEUDOPLAYER;
	}

	public void enter(final Player player) {
		Logger.i("GAMEWORLD", player.getUid() + ": player sent enter/exit to " + parent.getUid());
		if (!isAvailable()) {
			return;
		}
		if (!canActivate()) {
			return;
		}
		if (driver != null) {
			chuckOutCurrentDriver();
		}
		driversHumanBody = player.getEntity();
		driversHumanBody.disable();

		setDriver(player);
		eventRoot.dispatchEvent(new GameEvent(EventType.ENTITY_NEW_PLAYER, parent, player));
		GUILogger.i().log(TAG, player + " enters car " + getParent());
	}

	public void exit() {
		if (!canActivate()) {
			return;
		}
		final Player exDriver = driver;
		removeDriver();
		if (driversHumanBody != null) {
			final int carRota = parent.rota.value();
			final float carRotaRad = PhysicsFactory.angleToPhysics(carRota);
			final float carX = parent.x.value();
			final float carY = parent.y.value();
			final Vec2 carPos = new Vec2(carX, carY);
			final Vec2 exitLocalVec = new Vec2((float) Math.cos(carRotaRad) * exitOffset.x, (float) Math.sin(carRotaRad) * exitOffset.y);
			final Vec2 newPos = carPos.add(exitLocalVec);
			driversHumanBody.x.set((int) newPos.x);
			driversHumanBody.y.set((int) newPos.y);
			driversHumanBody.rota.set(carRota + exitRotation);
			driversHumanBody.enable();
			exDriver.setEntity(driversHumanBody);
			eventRoot.dispatchEvent(new GameEvent(EventType.ENTITY_NEW_PLAYER, driversHumanBody, exDriver));
		}
		{ // LOGGING
			final String logMsg = String.format("%s exits car %s", exDriver, getParent());
		final String logMsg2 = String.format("is now %s", driversHumanBody);
		GUILogger.i().log(TAG, logMsg);
		GUILogger.i().log(TAG, logMsg2);
		}
		driversHumanBody = null;
	}

	public void chuckOutCurrentDriver() {
		exit();
	}

	public void setDriver(final Player player) {
		if (driver != null) {
			removeDriver();
		}
		driver = player;
		player.setEntity(getParent());
		//		hasChanged = true;
		getParent().setChanged();
	}

	public void removeDriver() {
		driver.removeEntity();
		driver = null;
		//		hasChanged = true;
		getParent().setChanged();
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		switch (type) {
			case ACTION_ENTEREXIT:
				exit();
				break;
			case ENTITY_DESTROYED:
				getParent().removeHandler(this.type);
				break;
		}
	}

	//TODO driver property
	//	@Override
	//	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
	//		Element e = super.toXMLElement(baseRevision, keeper);
	//		if (e != null) {
	//			String driverStr = (driver == null) ? "none" : driver.getUid() + "";
	//			Attribute value = new Attribute(XmlElements.ATTRIB_VALUE.tagName(),
	//					driverStr);
	//			e.setAttribute(value);
	//		}
	//		return e;
	//	}

	private boolean canActivate() {
		final long now = System.currentTimeMillis();
		if (now - lastActivationMillis > COOLDOWN_MILLIS) {
			lastActivationMillis = now;
			return true;
		}
		return false;
	}

}
