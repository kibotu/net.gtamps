package net.gtamps.game.handler;

import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.event.IGameEventDispatcher;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.player.Player;

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
 * @author jan, tom, til
 * @see Entity
 */
public class SensorDoorHandler extends SensorHandler {

    public SensorDoorHandler(final IGameEventDispatcher eventRoot, final Entity parent) {
        super(eventRoot, EventType.ENTITY_SENSE_DOOR, EventType.ACTION_ENTEREXIT, parent);
        final EventType[] receives = {EventType.ENTITY_SENSE_DOOR,
                EventType.ACTION_ENTEREXIT};
        setReceives(receives);
        connectUpwardsActor(parent);
    }

    @Override
    public void act(final GameEvent event) {
        // Logger.i().log(TAG, getParent() + " looking for things to enter");
        final Player player = (Player) event.getSource();
        float minDistance = Float.POSITIVE_INFINITY;
        Entity closest = null;
        for (final Entity e : sensed) {
            if (e.getHandler(Handler.Type.DRIVER) == null) {
                continue;
            }
            final float dx = parent.x.value() - e.x.value();
            final float dy = parent.y.value() - e.y.value();
            final float dist = dx * dx + dy * dy;
            if (dist < minDistance) {
                minDistance = dist;
                closest = e;
            }
        }
        if (closest != null) {
            // Logger.i().log(TAG, getParent() + " trying to enter " + closest);
            final DriverHandler driver = (DriverHandler) closest
                    .getHandler(Handler.Type.DRIVER);
            if (driver != null) {
                driver.enter(player);
            } else {
            }
        }

    }

}
