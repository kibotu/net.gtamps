package net.gtamps.game.handler;

import net.gtamps.game.conf.WorldConstants;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;
import org.jbox2d.common.Vec2;

/**
 * <p>
 * A handler for entities that explode on destruction, with variable
 * initial force. All entities caught in the blast will be given a
 * push and be dealt some damage, depending on initial force and their
 * distance to the explosion epicenter.
 * </p>
 * <p>
 * Technicalities: an explosion is an immaterial shape that knows to
 * trigger {@link EventType#ENTITY_SENSE_EXPLOSION ENTITY_SENSE_EXPLOSION}
 * type events. If triggered by its parent entity's
 * {@link EventType#ENTITY_DESTROYED destruction}, it will visit all
 * entities in the shape's range and have their
 * {@link PhysicsHandler#applyImpulse(Vec2) PhysicsHandler} apply an
 * impulse and their {@link HealthProperty#takeDamage(float) HealthProperty}
 * apply some damage.
 * </p>
 * <p>
 * <table>
 * <tr>
 * <td><strong>sensor type:</strong></td><td>{@link EventType#ENTITY_SENSE_EXPLOSION}</td>
 * </tr>
 * <tr>
 * <td><strong>trigger event:</strong></td><td>{@link EventType#ENTITY_DESTROYED}</td>
 * </tr>
 * <tr>
 * <td><strong>requires:</strong></td><td><ul><li>{@link PhysicsHandler} <i>(opposite)</i></li>
 * <li>{@link HealthProperty} <i>(opposite)</i></li></ul></td>
 * </tr>
 * </table>
 * </p>
 *
 * @author jan, tom, til
 * @see Entity
 */
public class SensorExplosionHandler extends SensorHandler {
    private static final float FALLOFF_SLOPE = -1f;

    private final float initialForce;
    private final float maxDistanceSq;

    public SensorExplosionHandler(final Entity parent, final float force) {
        super(EventType.ENTITY_SENSE_EXPLOSION, EventType.ENTITY_DESTROYED, parent);
        final EventType[] receives = {EventType.ENTITY_SENSE_EXPLOSION,
                EventType.ENTITY_DESTROYED};
        setReceives(receives);
        connectUpwardsActor(parent);
        initialForce = force;
        maxDistanceSq = -(force / FALLOFF_SLOPE);
    }


    /* calculation of force and distance, if you're interested:
      *
      * (non-Javadoc)
      * @see net.net.gtamps.game.handler.SensorHandler#act(net.net.gtamps.game.event.GameEvent)
      *
      *
      *
      * FORCE AS A FUNCTION OF DISTANCE from explosion epicenter:
      *
      * assuming a linear relationship, we get
      *
      * for distance = 0, force is maximum
      * at maximum distance, force reaches 0
      *
      * therefore,
      * f(0) = f_max
      * f(d_max) = 0 , 	with a slope of  [m] (m is negative)
      *
      * f(d) = m * d  + f_max
      * ---------------------
      *
      * MAXIMUM DISTANCE:
      *
      * since [m = -f_max / d_max]	, we get
      *
      * d_max = -f_max / m
      * ------------------
      *
      *
      * EXPONENTIAL FALLOFF:
      *
      * if you interpret d to be actually be the *square* of the real distance
      * (lets call the real distance 'l': d = l^2), the force will
      * diminish that much faster:
      *
      * f(l^2) = m * l^2  + f_max
      * f(l) = f(sqr(d))
      *
      * l_max^2 = -f_max / m
      *
      */
    @Override
    public void act(final GameEvent event) {
//		PositionProperty p1 = (PositionProperty) getParent().getProperty(
//				Property.Type.POSITION);
//		PositionProperty p2;
        for (final Entity e : sensed) {
            if (e == getParent()) {
                continue;
            }
//			p2 = (PositionProperty) e.getProperty(Property.Type.POSITION);
//			if (p2 == null) {
//				continue;
//			}
            final Vec2 vec = new Vec2(parent.x.value() - e.x.value(), parent.y.value() - e.y.value());
            //Vec2 vec = new Vec2(p1.getX() - p2.getX(), p1.getY() - p2.getY());
            final float distanceSq = vec.lengthSquared();
            if (distanceSq >= maxDistanceSq) {
                continue;
            }
            vec.normalize();
            final float impulseWorld = (FALLOFF_SLOPE * distanceSq) + initialForce;
            final float impulse = impulseWorld / WorldConstants.PIX_TO_PHYSICS_RATIO;
            final SimplePhysicsHandler ph = (SimplePhysicsHandler) e.getHandler(Handler.Type.PHYSICS);
            if (ph != null) {
                ph.applyImpulse(vec.mul(impulse));
            }
            //TODO
//			HealthProperty h = (HealthProperty) e.getProperty(Property.Type.HEALTH);
//			if (h != null) {
//				h.takeDamage(impulse);
//			}
        }
    }

}
