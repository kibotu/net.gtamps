package net.gtamps.game.physics;

import net.gtamps.game.conf.PhysicalConstants;
import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.conf.WorldConstants;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.game.event.EventType;

import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class PhysicsFactory {

	private final static LogType TAG = LogType.PHYSICS;
	
	private PhysicsFactory(){
		
	}
	
	public static Box2DEngine createPhysics(int pixWidth, int pixHeight) {
		float minX = 0;
		float minY = 0;
		float maxX = lengthToPhysics(pixWidth);
		float maxY = lengthToPhysics(pixHeight);
		return new Box2DEngine(minX, minY, maxX, maxY);
	}
	
	/**
	 * creates a new car, puts it inside the box2d world and then returns it.
	 * 
	 * @param uid
	 * 			  the uid of this entity;
	 * @param x
	 *            the x position in box2d coordinates (1m = 10px)
	 * @param y
	 *            the y position in box2d coordinates (1m = 10px)
	 * @param rotation
	 *            the rotation in radians
	 * @return
	 */
	public static Body createSportsCar(World world, PhysicalProperties physprop, int pixX, int pixY, int deg) {
		Logger.i().log(TAG, "Creating car at x:"+pixX+" y:"+pixY+" deg:"+deg);
		float x = lengthToPhysics(pixX);
		float y = lengthToPhysics(pixY);
		float rotation = angleToPhysics(deg);
		
		BodyDef m_body_def = new BodyDef();
		m_body_def.position = new Vec2(x, y);
		m_body_def.angle = rotation;
		
		PolygonDef m_poly_def = new PolygonDef();
		m_poly_def.density = physprop.DENSITY;
		m_poly_def.friction = physprop.FRICTION;		
		m_poly_def.restitution = physprop.RESTITUTION;
		// car image size is 31x64px
		// that makes 3.1m x 6.4m
		// but set as box takes half the sizes as values
		m_poly_def.setAsBox(3.1f,1.55f);
//		m_poly_def.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_MOBILE;
//		m_poly_def.filter.maskBits = PhysicalConstants.COLLISION_MASK_ALL;
		m_poly_def.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_MOBILE;
		
		CircleDef explosionSensorDef = new CircleDef();
		explosionSensorDef.isSensor = true;
		explosionSensorDef.radius = 100;
//		explosionSensorDef.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_INSUBSTANTIAL;
//		explosionSensorDef.filter.maskBits = PhysicalConstants.COLLISION_MASK_SOLID;
		explosionSensorDef.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_SENSOR;
		explosionSensorDef.userData = EventType.ENTITY_SENSE_EXPLOSION;
		
		PolygonDef doorDef = new PolygonDef();
		doorDef.setAsBox(1f, 2.5f);
		doorDef.isSensor = true;
//		doorDef.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_INSUBSTANTIAL;
//		doorDef.filter.maskBits = PhysicalConstants.COLLISION_MASK_SOLID;
		doorDef.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_SENSOR;
		doorDef.userData = EventType.ENTITY_SENSE_DOOR;

		
		//TODO looks bad, may work.
		Body dynamicBody = null;
		do{
			dynamicBody = world.createBody(m_body_def);
		} while(dynamicBody == null);
		
		dynamicBody.wakeUp();
		
		dynamicBody.createShape(m_poly_def);
		dynamicBody.setMassFromShapes();
		
		dynamicBody.createShape(explosionSensorDef);
		dynamicBody.createShape(doorDef);
		
		
		dynamicBody.setLinearDamping(physprop.LINEAR_DAMPING);
		dynamicBody.setAngularDamping(physprop.ANGULAR_DAMPING);
		//not sure if thats all ok....
		/*MassData md = new MassData();
		md.mass = physprop.MASS;
		dynamicBody.setMass(md);*/
		
//		dynamicBody.setAngularDamping(0.1f);
//		dynamicBody.setLinearDamping(0.1f);
		
//		assert dynamicBody.isDynamic();
		
		return dynamicBody;
	}
	
	public static Body createHuman(World world, PhysicalProperties physprop, int pixX, int pixY, int rota) {
		Logger.i().log(TAG, "Creating human at x:"+pixX+" y:"+pixY);

		float x = lengthToPhysics(pixX);
		float y = lengthToPhysics(pixY);
		float rotation = angleToPhysics(rota);

		BodyDef m_body_def = new BodyDef();
		m_body_def.position = new Vec2(x, y);
		m_body_def.angle = rotation;
			
		CircleDef m_shape = new CircleDef();
		m_shape.density = physprop.DENSITY;
		m_shape.friction = physprop.FRICTION;
		m_shape.restitution = physprop.RESTITUTION;
		m_shape.radius = 0.8f;
//		m_shape.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_MOBILE;
//		m_shape.filter.maskBits = PhysicalConstants.COLLISION_MASK_ALL;
		m_shape.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_MOBILE;

		Body body = null;
		while(body == null) {
			body = world.createBody(m_body_def);
		}
		body.createShape(m_shape);
		body.setMassFromShapes();
		body.setLinearDamping(physprop.LINEAR_DAMPING);
		body.setAngularDamping(physprop.ANGULAR_DAMPING);
		body.wakeUp();
		return body;
	}

	public static Body createHouse(World world, int pixX, int pixY) {
		Logger.i().log(TAG, "Creating house at x:"+pixX+" y:"+pixY);
		
		float x = lengthToPhysics(pixX);
		float y = lengthToPhysics(pixY);
		float rotation = angleToPhysics(0);

		BodyDef m_body_def = new BodyDef();
		m_body_def.position = new Vec2(x, y);
		m_body_def.angle = rotation;

		PolygonDef m_poly_def = new PolygonDef();
//		m_poly_def.density = 1.0f;
		m_poly_def.friction = 0.1f;
		m_poly_def.restitution = 0.5f;
//		m_poly_def.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_STATIONARY;
//		m_poly_def.filter.maskBits = PhysicalConstants.COLLISION_MASK_ALL;
		m_poly_def.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_STATIONARY;		

		// house image size is 64x64px
		// that makes 6.4m x 6.4m
		// but setAsBox takes half the sizes as values
		m_poly_def.setAsBox(3.2f, 3.2f);
		
		Body houseBody = world.createBody(m_body_def);

		houseBody.m_userData = PhysicalProperties.Empty;
		
		houseBody.createShape(m_poly_def);
		return houseBody;
	}
	
	public static Body createSpawnPoint(World world, int pixX, int pixY, Integer rotation) {
		Logger.i().log(TAG, "Creating spawnpoint at x:"+pixX+" y:"+pixY);
		
		float x = lengthToPhysics(pixY);
		float y = lengthToPhysics(pixY);

		BodyDef body_def = new BodyDef();
		body_def.position = new Vec2(x, y);
		body_def.angle = angleToPhysics(rotation);
				
		CircleDef circle_def = new CircleDef();
		circle_def.density = 0f;
//		circle_def.friction = 0f;
		circle_def.isSensor = true;
		circle_def.radius = 3.2f;
//		circle_def.localPosition = new Vec2(0f, 0f);
//		circle_def.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_INSUBSTANTIAL;
//		circle_def.filter.maskBits = PhysicalConstants.COLLISION_MASK_SOLID;
		circle_def.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_SENSOR;
		circle_def.userData = EventType.ENTITY_SENSE_SPAWN;
		
		Body spawnBody = world.createBody(body_def);
		spawnBody.m_userData = PhysicalProperties.Empty;
		spawnBody.createShape(circle_def);
		return spawnBody;
		
	}
	
	public static Body createBullet(World world, PhysicalProperties physprop, int pixX, int pixY, int angle) {
		Logger.i().log(TAG, "Creating bullet at x:"+pixX+" y:"+pixY);
		
		float x = lengthToPhysics(pixX);
		float y = lengthToPhysics(pixY);

		Vec2 direction = new Vec2( (float) Math.cos(angleToPhysics(angle)), (float) Math.sin(angleToPhysics(angle)) );
		
		BodyDef body_def = new BodyDef();
		body_def.position = new Vec2(x, y);
		body_def.angle = angle;
		body_def.isBullet = true;
				
		CircleDef circle_def = new CircleDef();
		circle_def.density = physprop.DENSITY;
		circle_def.isSensor = false;
		circle_def.radius = 0.1f;
//		circle_def.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_MOBILE;
//		circle_def.filter.maskBits = PhysicalConstants.COLLISION_MASK_ALL;
		circle_def.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_MOBILE;
		Body bulletBody = null;
		do{
		bulletBody = world.createBody(body_def);
		} while (bulletBody == null);
		
		bulletBody.createShape(circle_def);
		bulletBody.setMassFromShapes();
		bulletBody.wakeUp();
		bulletBody.applyImpulse(direction.mul(PhysicalConstants.BulletSpeed),bulletBody.getWorldCenter());
		return bulletBody;

	}
	
	
	public static float angleToPhysics(int degrees) {
		return (float) ((degrees % 360) * Math.PI / 180f);
	}
	
	public static int angleToWorld(float radians) {
		return (int) (radians * 180f / Math.PI) % 360;
	}
	
	public static float lengthToPhysics(int pixels) {
		return pixels / WorldConstants.PIX_TO_PHYSICS_RATIO;
	}
	
	public static int lengthToWorld(float lengthUnits) {
		return (int) (lengthUnits * WorldConstants.PIX_TO_PHYSICS_RATIO);
	}

	
}
