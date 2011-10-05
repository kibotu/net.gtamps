package net.gtamps.game.handler;

import org.jbox2d.common.Vec2;

import net.gtamps.game.conf.WorldConstants;
import net.gtamps.game.entity.Entity;
import net.gtamps.game.event.EventType;
import net.gtamps.game.event.GameEvent;
import net.gtamps.game.property.HealthProperty;
import net.gtamps.game.property.PositionProperty;
import net.gtamps.game.property.Property;

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
 * 											<li>{@link HealthProperty} <i>(opposite)</i></li></ul></td>
 * </tr>
 * </table>
 * </p>
 * 
 * @see Entity
 * 
 * @author jan, tom, til
 * 
 */
public class SensorExplosionHandler extends SensorHandler {
	private static final float FALLOFF_SLOPE = -1f;
	
	private final float initialForce;
	private final float maxDistanceSq;

	public SensorExplosionHandler(Entity parent, float force) {
		super(EventType.ENTITY_SENSE_EXPLOSION, EventType.ENTITY_DESTROYED, parent);
		EventType[] receives = { EventType.ENTITY_SENSE_EXPLOSION,
				EventType.ENTITY_DESTROYED };
		this.setReceivesDown(receives);
		this.connectUpwardsActor(parent);
		this.initialForce = force;
		this.maxDistanceSq = -(force / FALLOFF_SLOPE);
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
	public void act(GameEvent event) {
		PositionProperty p1 = (PositionProperty) getParent().getProperty(
				Property.Type.POSITION);
		PositionProperty p2;
		for (Entity e : this.sensed) {
			if (e == this.getParent()) {
				continue;
			}
			p2 = (PositionProperty) e.getProperty(Property.Type.POSITION);
			if (p2 == null) {
				continue;
			}
			Vec2 vec = new Vec2(p1.getX() - p2.getX(), p1.getY() - p2.getY());
			float distanceSq = vec.lengthSquared();
			if (distanceSq >= this.maxDistanceSq) {
				continue;
			}
			vec.normalize();
			float impulseWorld = (FALLOFF_SLOPE * distanceSq) + this.initialForce;
			float impulse = impulseWorld / WorldConstants.PIX_TO_PHYSICS_RATIO;
			SimplePhysicsHandler ph = e.getPhysicsHandler();
			if (ph != null) {
				ph.applyImpulse(vec.mul(impulse));
			}
			HealthProperty h = (HealthProperty) e.getProperty(Property.Type.HEALTH);
			if (h != null) {
				h.takeDamage(impulse);
			}
		}
	}

}
