package net.gtamps.game.physics;

import java.util.ArrayList;
import java.util.Collection;

import net.gtamps.game.conf.PhysicalConstants;
import net.gtamps.game.conf.PhysicalProperties;
import net.gtamps.game.conf.WorldConstants;
import net.gtamps.game.handler.blueprints.MobilityBlueprint;
import net.gtamps.game.handler.blueprints.PhysicsBlueprint;
import net.gtamps.game.universe.Universe;
import net.gtamps.server.gui.GUILogger;
import net.gtamps.server.gui.LogType;
import net.gtamps.shared.Utils.math.Vector3;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.level.PhysicalShape;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.MassData;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.collision.shapes.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

public class PhysicsFactory {

    private final static LogType TAG = LogType.PHYSICS;

    private PhysicsFactory() {
    }

    public static Box2DEngine createPhysics(final Universe universe, final int pixWidth, final int pixHeight) {
        final float minX = 0;
        final float minY = 0;
        final float maxX = lengthToPhysics(pixWidth);
        final float maxY = lengthToPhysics(pixHeight);
        return new Box2DEngine(universe, minX, minY, maxX, maxY);
    }

    public static PhysicsBlueprint createHouseBlueprintFromLevelPhysicalShape(final Universe universe, final PhysicalShape levelshape) {
        final PhysicsBlueprint blup = createPhysicsBlueprint(universe, PhysicalProperties.Empty);
        final PolygonDef polyDef = new PolygonDef();
        for (final Vector3 vertex : levelshape) {
        	final float x = lengthToPhysics((int) vertex.x);
        	final float y = lengthToPhysics((int) vertex.y);
            polyDef.addVertex(new Vec2(x, y));
        }
        polyDef.filter = new FilterData();
        polyDef.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_STATIONARY;
        blup.addShapeDef(polyDef);
        return blup;
    }

    public static PhysicsBlueprint createPhysicsBlueprint(final Universe universe, final PhysicalProperties physprop) {
        final PhysicsBlueprint blup = new PhysicsBlueprint(
        		universe.getEventRoot(),
                universe.getPhysics().getWorld(),
                physpropToBodyDef(physprop),
                isDynamic(physprop)
        );
        blup.addAllShapeDefs(physpropToShapeDefs(physprop));
        return blup;
    }

    public static MobilityBlueprint createMobilityBlueprint(final Universe universe, final PhysicalProperties physprop) {
        if (!isDynamic(physprop)) {
            return null;
        }
        return new MobilityBlueprint(universe.getEventRoot(), physpropToMobilityProp(physprop));
    }

    private static boolean isDynamic(final PhysicalProperties physprop) {
        switch (physprop) {
            case Sportscar:
            case Taxi:
            case Human:
            case Bullet:
                return true;
            case Empty:
                return false;
            default:
                throw new IllegalStateException("handle all possible types");
        }
    }

    private static MobilityProperties physpropToMobilityProp(final PhysicalProperties physicalProperties) {
        MobilityProperties.Type type;
        switch (physicalProperties.TYPE) {
            case CAR:
                type = MobilityProperties.Type.CAR;
                break;
            case HUMAN:
                type = MobilityProperties.Type.HUMAN;
                break;
            case BULLET:
                type = MobilityProperties.Type.BULLET;
                break;
            case NONE:
                type = MobilityProperties.Type.NONE;
                break;
            default:
                throw new IllegalStateException("handle all possible types!");
        }
        return new MobilityProperties(
                physicalProperties.VELOCITY_FORCE,
                physicalProperties.STEERING_FORCE,
                physicalProperties.STEERING_RADIUS,
                physicalProperties.SLIDYNESS,
                physicalProperties.MAX_SPEED,
                type);
    }

    private static Collection<ShapeDef> physpropToShapeDefs(final PhysicalProperties physicalProperties) {
        final Collection<ShapeDef> defs = new ArrayList<ShapeDef>();

        // primary shape

        final ShapeDef def;
        switch (physicalProperties.TYPE) {
            case CAR:
                def = new PolygonDef();
                ((PolygonDef) def).setAsBox(3.1f, 1.55f);
                break;
            case HUMAN:
                def = new CircleDef();
                ((CircleDef) def).radius = 0.8f;
                break;
            case BULLET:
                def = new CircleDef();
                ((CircleDef) def).radius = 0.1f;
                break;
            case NONE:
                def = null;
                break;
            default:
                // shouldn't get here
                throw new IllegalStateException("treat all possible types");
        }
        if (def != null) {
            def.friction = physicalProperties.FRICTION;
            def.restitution = physicalProperties.RESTITUTION;
            def.density = physicalProperties.DENSITY;
            def.filter = new FilterData();
            //		def.filter.categoryBits = shapeDef.filter.categoryBits;
            //		def.filter.maskBits = shapeDef.filter.maskBits;
            def.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_MOBILE;
            def.isSensor = false;
            defs.add(def);
        } else {
        	
        }
        // secondary shapes
        switch (physicalProperties.TYPE) {
            case CAR:
                final CircleDef explosionSensorDef = new CircleDef();
                explosionSensorDef.isSensor = true;
                final float radius = 10f;
                explosionSensorDef.localPosition = new Vec2(0, 0);
                explosionSensorDef.radius = radius;
                explosionSensorDef.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_SENSOR;
                explosionSensorDef.userData = EventType.ENTITY_SENSE_EXPLOSION;
                final PolygonDef doorDef = new PolygonDef();
                doorDef.setAsBox(1f, 2.5f);
                doorDef.isSensor = true;
                doorDef.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_SENSOR;
                doorDef.userData = EventType.ENTITY_SENSE_DOOR;
                defs.add(explosionSensorDef);
                defs.add(doorDef);
                break;
        }
        return defs;
    }

    private static BodyDef physpropToBodyDef(final PhysicalProperties physicalProperties) {
        final BodyDef bd = new BodyDef();
        bd.angularDamping = physicalProperties.ANGULAR_DAMPING;
        bd.isBullet = physicalProperties.TYPE.equals(PhysicalProperties.Type.BULLET);
        bd.linearDamping = physicalProperties.LINEAR_DAMPING;
//		allowSleep = null;
//		angle = null;
//		fixedRotation = null;
//		massData = null;
//		position = null;
//		userData = null;
        return bd;
    }

    public static BodyDef copyBodyDef(final BodyDef other) {
        final BodyDef bd = new BodyDef();
        bd.angularDamping = other.angularDamping;
        bd.isBullet = other.isBullet;
        bd.linearDamping = other.linearDamping;
        bd.allowSleep = other.allowSleep;
        bd.angle = other.angle;
        bd.fixedRotation = other.fixedRotation;
        bd.massData = new MassData().clone();
        bd.position = other.position.clone();
        bd.userData = other.userData;
        return bd;
    }


    /**
     * creates a new car, puts it inside the box2d world and then returns it.
     *
     * @param uid      the uid of this entity;
     * @param x        the x position in box2d coordinates (1m = 10px)
     * @param y        the y position in box2d coordinates (1m = 10px)
     * @param rotation the rotation in radians
     * @return
     * @deprecated see other static methods
     */
    @Deprecated
    public static Body createSportsCar(final World world, final PhysicalProperties physprop, final int pixX, final int pixY, final int deg) {
        GUILogger.i().log(TAG, "Creating car at x:" + pixX + " y:" + pixY + " deg:" + deg);
        final float x = lengthToPhysics(pixX);
        final float y = lengthToPhysics(pixY);
        final float rotation = angleToPhysics(deg);

        final BodyDef m_body_def = new BodyDef();
        m_body_def.position = new Vec2(x, y);
        m_body_def.angle = rotation;

        final PolygonDef m_poly_def = new PolygonDef();
        m_poly_def.density = physprop.DENSITY;
        m_poly_def.friction = physprop.FRICTION;
        m_poly_def.restitution = physprop.RESTITUTION;
        // car image size is 31x64px
        // that makes 3.1m x 6.4m
        // but set as box takes half the sizes as values
        m_poly_def.setAsBox(3.1f, 1.55f);
//		m_poly_def.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_MOBILE;
//		m_poly_def.filter.maskBits = PhysicalConstants.COLLISION_MASK_ALL;
        m_poly_def.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_MOBILE;

        final CircleDef explosionSensorDef = new CircleDef();
        explosionSensorDef.isSensor = true;
        explosionSensorDef.radius = 100;
//		explosionSensorDef.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_INSUBSTANTIAL;
//		explosionSensorDef.filter.maskBits = PhysicalConstants.COLLISION_MASK_SOLID;
        explosionSensorDef.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_SENSOR;
        explosionSensorDef.userData = EventType.ENTITY_SENSE_EXPLOSION;

        final PolygonDef doorDef = new PolygonDef();
        doorDef.setAsBox(1f, 2.5f);
        doorDef.isSensor = true;
//		doorDef.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_INSUBSTANTIAL;
//		doorDef.filter.maskBits = PhysicalConstants.COLLISION_MASK_SOLID;
        doorDef.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_SENSOR;
        doorDef.userData = EventType.ENTITY_SENSE_DOOR;


        //TODO looks bad, may work.
        Body dynamicBody = null;
        do {
            dynamicBody = world.createBody(m_body_def);
        } while (dynamicBody == null);

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

    /**
     * @deprecated see other static methods	 *
     */
    @Deprecated
    public static Body createHuman(final World world, final PhysicalProperties physprop, final int pixX, final int pixY, final int rota) {
        GUILogger.i().log(TAG, "Creating human at x:" + pixX + " y:" + pixY);

        final float x = lengthToPhysics(pixX);
        final float y = lengthToPhysics(pixY);
        final float rotation = angleToPhysics(rota);

        final BodyDef m_body_def = new BodyDef();
        m_body_def.position = new Vec2(x, y);
        m_body_def.angle = rotation;

        final CircleDef m_shape = new CircleDef();
        m_shape.density = physprop.DENSITY;
        m_shape.friction = physprop.FRICTION;
        m_shape.restitution = physprop.RESTITUTION;
        m_shape.radius = 0.8f;
//		m_shape.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_MOBILE;
//		m_shape.filter.maskBits = PhysicalConstants.COLLISION_MASK_ALL;
        m_shape.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_MOBILE;

        Body body = null;
        while (body == null) {
            body = world.createBody(m_body_def);
        }
        body.createShape(m_shape);
        body.setMassFromShapes();
        body.setLinearDamping(physprop.LINEAR_DAMPING);
        body.setAngularDamping(physprop.ANGULAR_DAMPING);
        body.wakeUp();
        return body;
    }

    /**
     * @deprecated see other static methods
     */
    @Deprecated
    public static Body createHouse(final World world, final int pixX, final int pixY) {
        GUILogger.i().log(TAG, "Creating house at x:" + pixX + " y:" + pixY);

        final float x = lengthToPhysics(pixX);
        final float y = lengthToPhysics(pixY);
        final float rotation = angleToPhysics(0);

        final BodyDef m_body_def = new BodyDef();
        m_body_def.position = new Vec2(x, y);
        m_body_def.angle = rotation;

        final PolygonDef m_poly_def = new PolygonDef();
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

        final Body houseBody = world.createBody(m_body_def);

        houseBody.m_userData = PhysicalProperties.Empty;

        houseBody.createShape(m_poly_def);
        return houseBody;
    }

    /**
     * @deprecated see other static methods
     */
    @Deprecated
    public static Body createSpawnPoint(final World world, final int pixX, final int pixY, final Integer rotation) {
        GUILogger.i().log(TAG, "Creating spawnpoint at x:" + pixX + " y:" + pixY);

        final float x = lengthToPhysics(pixY);
        final float y = lengthToPhysics(pixY);

        final BodyDef body_def = new BodyDef();
        body_def.position = new Vec2(x, y);
        body_def.angle = angleToPhysics(rotation);

        final CircleDef circle_def = new CircleDef();
        circle_def.density = 0f;
//		circle_def.friction = 0f;
        circle_def.isSensor = true;
        circle_def.radius = 3.2f;
//		circle_def.localPosition = new Vec2(0f, 0f);
//		circle_def.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_INSUBSTANTIAL;
//		circle_def.filter.maskBits = PhysicalConstants.COLLISION_MASK_SOLID;
        circle_def.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_SENSOR;
        circle_def.userData = EventType.ENTITY_SENSE_SPAWN;

        final Body spawnBody = world.createBody(body_def);
        spawnBody.m_userData = PhysicalProperties.Empty;
        spawnBody.createShape(circle_def);
        return spawnBody;

    }

    /**
     * @deprecated see other static methods
     */
    @Deprecated
    public static Body createBullet(final World world, final PhysicalProperties physprop, final int pixX, final int pixY, final int angle) {
        GUILogger.i().log(TAG, "Creating bullet at x:" + pixX + " y:" + pixY);

        final float x = lengthToPhysics(pixX);
        final float y = lengthToPhysics(pixY);

        final Vec2 direction = new Vec2((float) Math.cos(angleToPhysics(angle)), (float) Math.sin(angleToPhysics(angle)));

        final BodyDef body_def = new BodyDef();
        body_def.position = new Vec2(x, y);
        body_def.angle = angle;
        body_def.isBullet = true;

        final CircleDef circle_def = new CircleDef();
        circle_def.density = physprop.DENSITY;
        circle_def.isSensor = false;
        circle_def.radius = 0.1f;
//		circle_def.filter.categoryBits = PhysicalConstants.COLLISION_CATEGORY_MOBILE;
//		circle_def.filter.maskBits = PhysicalConstants.COLLISION_MASK_ALL;
        circle_def.filter.groupIndex = PhysicalConstants.COLLISION_GROUP_MOBILE;
        Body bulletBody = null;
        do {
            bulletBody = world.createBody(body_def);
        } while (bulletBody == null);

        bulletBody.createShape(circle_def);
        bulletBody.setMassFromShapes();
        bulletBody.wakeUp();
        bulletBody.applyImpulse(direction.mul(PhysicalConstants.BulletSpeed), bulletBody.getWorldCenter());
        return bulletBody;

    }


    public static float angleToPhysics(final int degrees) {
        return (float) ((degrees % 360) * Math.PI / 180f);
    }

    public static int angleToWorld(final float radians) {
        return (int) (radians * 180f / Math.PI) % 360;
    }

    public static float lengthToPhysics(final float pixels) {
        return pixels / WorldConstants.PIX_TO_PHYSICS_RATIO;
    }

    public static int lengthToWorld(final float lengthUnits) {
        return (int) (lengthUnits * WorldConstants.PIX_TO_PHYSICS_RATIO);
    }
}
