package net.gtamps.game.handler;

import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;

import java.util.HashSet;
import java.util.Set;

/**
 * A bare-bones sensor handler than can be easily extended to provide
 * actual functionality.
 *
 * @author jan, tom, til
 */
public class SensorHandler extends Handler {
    private static final LogType TAG = LogType.GAMEWORLD;

    private static EventType[] sendsUp = {};
    private static EventType[] receivesDown = {EventType.ENTITY_SENSE};
    //EventType.ACTION_ENTEREXIT, EventType.ENTITY_DESTROYED };

    protected Set<Entity> sensed = new HashSet<Entity>();
    protected final EventType sensorType;
    protected final EventType triggerEvent;

    public SensorHandler(EventType sensorType, EventType triggerEvent, Entity parent) {
        super(Handler.Type.SENSOR, parent);
        this.sensorType = sensorType;
        this.triggerEvent = triggerEvent;
        this.setSendsUp(sendsUp);
        this.setReceivesDown(receivesDown);
        this.connectUpwardsActor(parent);
    }

    @Override
    public void receiveEvent(GameEvent event) {
        EventType type = event.getType();
        if (type.isType(this.sensorType)) {
            sense(event);
        } else if (type.isType(triggerEvent)) {
            act(event);
        }

/*		switch (type) {
		case ENTITY_SENSE_BEGIN:
		case ENTITY_SENSE_END:
			sense(event);
			break;
		case ACTION_ENTEREXIT:
			if (this.sensorType == Type.ENTER_DOORS) {
				enterdoor((Player) event.getSource());
			}
			break;
		case ENTITY_DESTROYED:
			if (this.sensorType == Type.EXPLOSION) {
				explode();
			}
			break;
		}
*/
    }

    protected void act(GameEvent event) {
        // override this! method will be triggered by TriggerEvent events
    }

    protected void sense(GameEvent event) {
        assert event.getType().isType(this.sensorType);
        GameObject source = event.getSource();
        GameObject target = event.getTarget();
        GameObject subject = (source == this.getParent()) ? target : source;
        if (event.isBegin()) {
            this.sensed.add((Entity) subject);
        } else if (event.isEnd()) {
            this.sensed.remove((Entity) subject);
        }
        if (this.sensorType.isType(EventType.ENTITY_SENSE_DOOR)) {
            Logger.i().log(TAG, target + " detected door of " + source);
        }

/*		switch (event.getType()) {
        case ENTITY_SENSE_BEGIN:
            if (this.sensorType == Type.ENTER_DOORS) {
                Logger.i().log(TAG, getParent() + " sensed a door");
            }
            this.sensed.add((Entity) subject);
            break;
        case ENTITY_SENSE_END:
            if (this.sensorType == Type.ENTER_DOORS) {
                Logger.i().log(TAG, getParent() + " forgot a door");
            }
            this.sensed.remove((Entity) subject);
            break;
        }
*/
    }

    protected void update() {
        // nothing
    }

/*	protected void explode() {
		float maxdistance = 100;
		float maximpulse = 100000;
		PositionProperty p1 = (PositionProperty) getParent().getProperty(
				Property.Type.POSITION);
		PositionProperty p2;
		for (Entity e : sensed) {
			p2 = (PositionProperty) e
					.getProperty(Property.Type.POSITION);
			if (p2 == null) {
				continue;
			}
			Vec2 vec = new Vec2(p1.getX() - p2.getX(), p1.getY() - p2.getY());
			float relativeDistance = vec.length() / maxdistance;
			if (relativeDistance >= 1) {
				continue;
			}
			vec.normalize();
			float impulse = (1 - relativeDistance) * maximpulse;
			SimplePhysicsHandler ph = e.getPhysicsHandler();
			if (ph != null) {
				ph.applyImpulse(vec.mul(impulse * relativeDistance));
			}
			HealthProperty h = (HealthProperty) e
					.getProperty(Property.Type.HEALTH);
			if (h != null) {
				h.takeDamage(impulse);
			}
		}
	}
*/
/*	protected void enterdoor(Player p) {
		//Logger.i().log(TAG, getParent() + " looking for things to enter");
		float minDistance = Float.POSITIVE_INFINITY;
		Entity closest = null;
		PositionProperty p1 = (PositionProperty) getParent().getProperty(
				Property.Type.POSITION);
		PositionProperty p2;
		for (Entity e : sensed) {
			if (e.getHandler(Handler.Type.DRIVER) == null) {
				continue;
			}
			p2 = (PositionProperty) e.getProperty(Property.Type.POSITION);
			float dx = p1.getX() - p2.getX();
			float dy = p1.getY() - p2.getY();
			float dist = dx * dx + dy * dy;
			if (dist < minDistance) {
				minDistance = dist;
				closest = e;
			}

		}
		if (closest != null) {
			//Logger.i().log(TAG, getParent() + " trying to enter " + closest);
			DriverHandler driver = (DriverHandler) closest
					.getHandler(Handler.Type.DRIVER);
			if (driver != null) {
				driver.enter(p);
			} else {
			}
		}
	}
*/
}
