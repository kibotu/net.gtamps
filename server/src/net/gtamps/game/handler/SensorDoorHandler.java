package net.gtamps.game.handler;

import net.gtamps.game.entity.Entity;
import net.gtamps.game.event.EventType;
import net.gtamps.game.event.GameEvent;
import net.gtamps.game.player.Player;
import net.gtamps.game.property.PositionProperty;
import net.gtamps.game.property.Property;

/**
 * <p>
 * A handler for door sensors. <strong>Give this to entities you want to be able
 * to <i>use</i> doors, not to the entity that actually *has* the door.</strong>
 * Think of the door as some sort of chime that announces its presense. This
 * handler will take note of the chime and remember the door for when the next
 * ENTER/EXIT event comes along, allowing its parent entity to enter the nearest
 * door it currently knows about.
 * </p>
 * <p>
 * Technicalities: in the end, a door is an immaterial shape that knows to
 * trigger {@link EventType#ENTITY_SENSE_DOOR} type
 * events. To be able to accept entities that will try to enter, the entity
 * owning the door shape must have a {@link DriverHandler}, which is called like
 * that even if the door-providing entity is not meant to be driven.
 * </p>
 * <p>
 * <table>
 * <tr>
 * <td><strong>sensor type:</strong></td><td>{@link EventType#ENTITY_SENSE_DOOR}</td>
 * </tr>
 * <tr>
 * <td><strong>trigger event:</strong></td><td>{@link EventType#ACTION_ENTEREXIT}</td>
 * </tr>
 * <tr>
 * <td><strong>requires:</strong></td><td>{@link DriverHandler} <i>(opposite)</i></td>
 * </tr>
 * </table>
 * </p>
 * 
 * @see Entity
 * 
 * @author jan, tom, til
 * 
 */
public class SensorDoorHandler extends SensorHandler {

	public SensorDoorHandler(Entity parent) {
		super(EventType.ENTITY_SENSE_DOOR, EventType.ACTION_ENTEREXIT, parent);
		EventType[] receives = { EventType.ENTITY_SENSE_DOOR,
				EventType.ACTION_ENTEREXIT };
		this.setReceivesDown(receives);
		this.connectUpwardsActor(parent);
	}

	@Override
	public void act(GameEvent event) {
		// Logger.i().log(TAG, getParent() + " looking for things to enter");
		Player player = (Player) event.getSource();
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
			// Logger.i().log(TAG, getParent() + " trying to enter " + closest);
			DriverHandler driver = (DriverHandler) closest
					.getHandler(Handler.Type.DRIVER);
			if (driver != null) {
				driver.enter(player);
			} else {
			}
		}

	}

}