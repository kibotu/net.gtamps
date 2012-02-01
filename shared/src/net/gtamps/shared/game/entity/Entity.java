package net.gtamps.shared.game.entity;

import java.util.HashMap;
import java.util.Map;

import net.gtamps.shared.Utils.validate.Validate;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.SharedGameActor;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.player.Player;
import net.gtamps.shared.serializer.communication.StringConstants;

public class Entity extends SharedGameActor {

	@SuppressWarnings("unused")
	private static final String TAG = Entity.class.getSimpleName();

	private static final long serialVersionUID = -5466989016443709708L;

	static public enum Type {
		CAR_CAMARO, CAR_RIVIERA, CAR_CHEVROLET_CORVETTE, HUMAN, HOUSE, BULLET, SPAWNPOINT, WAYPOINT, PLACEHOLDER, CUBE, CYLINDER, TORUS, SPHERE;
	}
	
	
	//TODO: use!
	public static String normalizeName(final String name) {
		return name.toUpperCase();
	}

	protected transient final Map<String, Handler<Entity>> handlers = new HashMap<String, Handler<Entity>>();
	public final IProperty<Integer> x = this.useProperty(StringConstants.PROPERTY_POSX, 0);
	public final IProperty<Integer> y = this.useProperty(StringConstants.PROPERTY_POSY, 0);
	public final IProperty<Integer> z = this.useProperty(StringConstants.PROPERTY_POSZ, 0);
	/**
	 * rotation about z
	 */
	public final IProperty<Integer> rota = this.useProperty(StringConstants.PROPERTY_ROTA, 0);
	public final IProperty<Integer> playerProperty = this.useProperty(StringConstants.PROPERTY_PLAYER, Player.INVALID_UID);
	public Type type;

	public Entity() {
		super();
		this.type = Type.PLACEHOLDER;
	}

	//TODO: fix the disconnect between the use of 'type' (serializer) and 'name' (server)

	public Entity(final Type type) {
		this(type.name(), INVALID_UID);
	}

	public Entity(final String name) {
		this(name, INVALID_UID);
	}

	public Entity(final String name, final int uid) {
		super(name, uid);
		assert this.name != null;
		this.type = getType(name);
	}

	//preallocate
	Type retGetType = null;
	private Type getType(final String name) {
		retGetType = EntityTypeProvider.valueOf(name);
		return retGetType==null ? Type.PLACEHOLDER : retGetType;
		/*try {
			return Type.valueOf(name.toUpperCase());
		} catch (final IllegalArgumentException e) {
			return Type.PLACEHOLDER;
		}*/
	}

	@Override
	public void receiveEvent(final GameEvent event) {
		final EventType type = event.getType();
		if (isEnabled()) {
			dispatchEvent(event);
		}
		if (type.isType(EventType.ENTITY_DESTROYED) && event.getTargetUid() == getUid()) {
			destroy();
		} else if (type.isType(EventType.ENTITY_DEACTIVATE)) {
			disable();
		} else if (type.isType(EventType.ENTITY_ACTIVATE)) {
			enable();
		}
		
	}

	public void setOwner(final Player p) {
		if (p == null) {
			throw new IllegalArgumentException("'p' must not be null");
		}
		System.out.println("entity owner set to: " + p);
		this.playerProperty.set(p.getUid());
	}

	public void removeOwner() {
		playerProperty.set(Player.INVALID_UID);
	}

	public int getOwnerUid() {
		return this.playerProperty.value();
	}

	public void setHandler(final Handler<Entity> handler) {
		if (handler == null) {
			throw new IllegalArgumentException("'handler' must not be null");
		}
		this.handlers.put(handler.getName(), handler);
	}

	public Handler<Entity> removeHandler(final Handler.Type type) {
		final String name = type.name().toLowerCase();
		final Handler<Entity> handler = this.handlers.remove(name);
		return handler;
	}

	public Handler<Entity> getHandler(final Handler.Type type) {
		final String name = type.name().toLowerCase();
		final Handler<Entity> handler = this.handlers.get(name);
		return handler;
	}

	@Override
	public void enable() {
		super.enable();
		for (final Handler<Entity> h : handlers.values()) {
			h.enable();
		}
		//FIXME
		//ActivationProperty ap = (ActivationProperty) this.getProperty(Property.Type.ACTIVATION);
		//		if (ap != null) {
		//			ap.enable();
		//		}
	}

	@Override
	public void disable() {
		super.disable();
		for (final Handler<?> h : handlers.values()) {
			h.disable();
		}
		//FIXME
		//ActivationProperty ap = (ActivationProperty) this.getProperty(Property.Type.ACTIVATION);
		//		if (ap != null) {
		//			ap.disable();
		//		}
	}

	@Override
	public String toString() {
		String s = super.toString();
		s += String.format("x:%d y:%d r:%d", this.x.value(), this.y.value(), this.rota.value());
		return s;
	}

	public void destroy() {
		// TODO Auto-generated method stub
		this.disable();

	}

	@Override
	public void setName(final String name) {
		Validate.notEmpty(name);
		this.name = name;
		type = getType(name);
	}

}

