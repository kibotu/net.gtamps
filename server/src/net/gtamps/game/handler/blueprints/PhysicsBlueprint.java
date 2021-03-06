package net.gtamps.game.handler.blueprints;

import java.util.ArrayList;
import java.util.Collection;

import net.gtamps.game.handler.ServersideHandler;
import net.gtamps.game.handler.SimplePhysicsHandler;
import net.gtamps.game.physics.PhysicsFactory;
import net.gtamps.game.universe.Universe;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.handler.Handler;

import org.jbox2d.collision.FilterData;
import org.jbox2d.collision.shapes.CircleDef;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.collision.shapes.ShapeDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jetbrains.annotations.NotNull;

public class PhysicsBlueprint extends HandlerBlueprint<Entity> {

	public static final boolean DYNAMIC = true;
	public static final boolean STATIC = false;
	@NotNull
	private final World world;
	private final boolean isDynamic;
	@NotNull
	private final BodyDef bodyDef;
	private final Collection<ShapeDef> shapeDefs;
	private int initialImpulse = 0;


	public PhysicsBlueprint(final Universe universe, final World world, final BodyDef bodyDef, final boolean dynamic) {
		super(universe, Handler.Type.PHYSICS);
		this.world = world;
		this.bodyDef = PhysicsFactory.copyBodyDef(bodyDef);
		isDynamic = dynamic;
		shapeDefs = new ArrayList<ShapeDef>();
	}

	public PhysicsBlueprint(final Universe universe, final World world, final BodyDef bodyDef, final int initialImpulse) {
		this(universe, world, bodyDef, true);
		this.initialImpulse = initialImpulse;
	}

	public PhysicsBlueprint(final PhysicsBlueprint other) {
		this(other.universe, other.world, other.bodyDef, other.isDynamic);
		initialImpulse = other.initialImpulse;
		for (final ShapeDef shapeDef : other.shapeDefs) {
			shapeDefs.add(shapeDef);
		}
	}

	@Override
	public ServersideHandler<Entity> createHandler(final Entity parent) {
		if (initialImpulse == 0) {
			return new SimplePhysicsHandler(universe, parent, this);
		} else {
			return new SimplePhysicsHandler(universe, parent, this, initialImpulse);
		}
	}

	public Body createBody(final Object userData) {
		return createBody(userData, null, null, null);
	}

	public Body createBody(final Object userData, final Integer pixX, final Integer pixY, final Integer deg) {
		Body body = null;
		final Vec2 opos = bodyDef.position;
		final float oang = bodyDef.angle;
		final boolean positioned = (pixX != null && pixY != null && deg != null);
		if (positioned) {
			final float xPos = PhysicsFactory.lengthToPhysics(pixX);
			final float yPos = PhysicsFactory.lengthToPhysics(pixY);
			final float angleRad = PhysicsFactory.angleToPhysics(deg);
			bodyDef.position = new Vec2(xPos, yPos);
			bodyDef.angle = angleRad;
		}
		while (body == null) {
			body = world.createBody(bodyDef);
		}
		if (positioned) {
			bodyDef.position = opos;
			bodyDef.angle = oang;
		}
		body.setUserData(userData);
		for (final ShapeDef shapeDef : shapeDefs) {
			body.createShape(shapeDef);
		}
		if (isDynamic && body.getMass() == 0f) {
			body.setMassFromShapes();
		}
		return body;
	}

	public World getWorld() {
		return world;
	}


	@Override
	public HandlerBlueprint<Entity> copy() {
		return new PhysicsBlueprint(this);
	}

	public void addAllShapeDefs(final Collection<ShapeDef> defs) {
		for (final ShapeDef shapeDef : defs) {
			addShapeDef(shapeDef);
		}
	}

	public void addShapeDef(final ShapeDef shapeDef) {
		final ShapeDef newShapeDef;
		if (shapeDef instanceof PolygonDef) {
			newShapeDef = new PolygonDef();
			((PolygonDef) newShapeDef).set((PolygonDef) shapeDef);
		} else {
			if (shapeDef instanceof CircleDef) {
				final CircleDef cdef = new CircleDef();
				cdef.localPosition = ((CircleDef) shapeDef).localPosition.clone();
				cdef.radius = ((CircleDef) shapeDef).radius;
				newShapeDef = cdef;
			} else {
				newShapeDef = new ShapeDef();
			}
			newShapeDef.type = shapeDef.type;
			newShapeDef.userData = shapeDef.userData;
			newShapeDef.friction = shapeDef.friction;
			newShapeDef.restitution = shapeDef.restitution;
			newShapeDef.density = shapeDef.density;
			newShapeDef.filter = new FilterData();
			newShapeDef.filter.categoryBits = shapeDef.filter.categoryBits;
			newShapeDef.filter.maskBits = shapeDef.filter.maskBits;
			newShapeDef.filter.groupIndex = shapeDef.filter.groupIndex;
			newShapeDef.isSensor = shapeDef.isSensor;
		}
		shapeDefs.add(newShapeDef);
	}

}
