package net.gtamps.game.handler;

import net.gtamps.XmlElements;
import net.gtamps.game.RevisionKeeper;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.game.player.PlayerManager;
import net.gtamps.game.property.PositionProperty;
import net.gtamps.game.property.Property;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.player.Player;

import org.jbox2d.common.Vec2;
import org.jdom.Attribute;
import org.jdom.Element;

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
 * @author jan, tom, til
 * @see Entity
 * @see Player
 *
 */
public class DriverHandler extends Handler {
	private static final LogType TAG = LogType.GAMEWORLD;
	private static final int COOLDOWN_MILLIS = 500;

	private static final Vec2 exitOffset = new Vec2(30f, 0);
	private static final int exitRotation = 90;
	
	private Player driver = null;
	private Entity driversHumanBody = null;
	private long lastActivationMillis = 0;
	private EventType[] sendsUp = { EventType.PLAYER_ENTERSCAR,
			EventType.PLAYER_EXITSCAR };
	private EventType[] receivesDown = { EventType.ENTITY_DESTROYED,
			EventType.ACTION_ENTEREXIT };

	public DriverHandler(Entity parent) {
		super(Handler.Type.DRIVER, parent);
		this.setSendsUp(sendsUp);
		this.setReceivesDown(receivesDown);
		this.connectUpwardsActor(parent);
	}

	public boolean isAvailable() {
		return driver == null || driver == PlayerManager.WORLD_PSEUDOPLAYER;
	}

	public void enter(Player player) {
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
		Logger.i().log(TAG, player + " enters car " + this.getParent());
	}

	public void exit() {
		if (!canActivate()) {
			return;
		}
		Player exDriver = driver;
		removeDriver();
		if (driversHumanBody != null) {
			//TODO
			PositionProperty pC = (PositionProperty) getParent().getProperty(Property.Type.POSITION);
			PositionProperty pH = (PositionProperty) driversHumanBody.getProperty(Property.Type.POSITION);
			int carRota = pC.getRotation();
			float carRotaRad = PhysicsFactory.angleToPhysics(carRota);
			float carX = (pC.getX());
			float carY = (pC.getY());
			Vec2 carPos = new Vec2(carX, carY);
			Vec2 exitLocalVec = new Vec2((float)Math.cos(carRotaRad)*exitOffset.x, (float) Math.sin(carRotaRad)*exitOffset.y);
			Vec2 newPos = carPos.add(exitLocalVec);
			pH.setX((int) newPos.x);
			pH.setY((int) newPos.y);
			pH.setRotation(carRota + exitRotation);
			driversHumanBody.enable();
			exDriver.setEntity(driversHumanBody);
		}
		{ // LOGGING
			String logMsg = String.format("%s exits car %s", exDriver, getParent());
			String logMsg2 = String.format("is now %s", driversHumanBody);
			Logger.i().log(TAG, logMsg);
			Logger.i().log(TAG, logMsg2);
		}
		driversHumanBody = null;
	}

	public void chuckOutCurrentDriver() {
		exit();
	}

	public void setDriver(Player player) {
		if (driver != null) {
			removeDriver();
		}
		driver = player;
		player.setEntity(getParent());
		this.hasChanged = true;
		this.getParent().setChanged();
	}

	public void removeDriver() {
		driver.removeEntity();
		driver = null;
		this.hasChanged = true;
		this.getParent().setChanged();
	}

	@Override
	public void receiveEvent(GameEvent event) {
		EventType type = event.getType();
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
		long now = System.currentTimeMillis();
		if (now - lastActivationMillis > COOLDOWN_MILLIS) {
			lastActivationMillis = now;
			return true;
		}
		return false;
	}

}
