package net.gtamps.shared.game.entity;

import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.player.Player;
import net.gtamps.shared.game.GameActor;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.event.GameEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Entity extends GameActor implements Serializable {

    private static final String TAG = Entity.class.getSimpleName();

	private static final long serialVersionUID = -5466989016443709708L;

	static public enum Type {
        CAR_CAMARO, CAR_RIVIERA, CAR_CHEVROLET_CORVETTE, HUMAN, HOUSE, BULLET, SPAWNPOINT, WAYPOINT, PLACEHOLDER;
    }

	//public static final Player DEFAULT_OWNER = PlayerManager.WORLD_PSEUDOPLAYER;

//	protected Map<Property.Type,Property> properties;
	protected transient final Map<String, Handler> handlers = new HashMap<String, Handler>();
	public final Propertay<Integer> x;
	public final Propertay<Integer> y;
	public final Propertay<Integer> z;
	/**
	 * rotation about z
	 */
	public final Propertay<Integer> rota;
	public final Type type;
	
	private transient Player owner = null;

	public Entity(String name){
		super(name);
        type = getType(name);
        x = this.useProperty("posx", 0);
        y = this.useProperty("posy", 0);
        z = this.useProperty("posz", 0);
        rota = this.useProperty("rota", 0);
	}

    private Type getType(String name) {
        try {
           return Type.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Type.PLACEHOLDER;
        }
    }

    @Override
	public void receiveEvent(GameEvent event) {
		dispatchEvent(event);
	}
	
	public void setOwner(Player p) {
		if (p == null) {
			throw new IllegalArgumentException("'p' must not be null");
		}
		this.owner = p;
	}
	
	public void removeOwner() {
		this.owner = null;
	}
	
	public Player getOwner() {
		return this.owner;
	}
	
	public void setHandler(Handler handler) {
		if (handler == null) {
			throw new IllegalArgumentException("'handler' must not be null");
		}
		this.handlers.put(handler.getName(), handler);
	}
	
	public Handler removeHandler(Handler.Type type) {
		String name = type.name().toLowerCase();
		Handler handler = this.handlers.remove(name);
		return handler;
	}
	
	public Handler getHandler(Handler.Type type) {
		String name = type.name().toLowerCase();
		Handler handler = this.handlers.get(name);
		return handler;
	}
	
	@Override
	public void enable() {
		super.enable();
		for (Handler h: handlers.values()) {
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
		for (Handler h: handlers.values()) {
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
	
}
