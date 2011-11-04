package net.gtamps.shared.game.entity;

import net.gtamps.shared.game.GameActor;
import net.gtamps.shared.game.IProperty;
import net.gtamps.shared.game.event.GameEvent;
import net.gtamps.shared.game.handler.Handler;
import net.gtamps.shared.game.player.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Entity extends GameActor implements Serializable {

    @SuppressWarnings("unused")
    private static final String TAG = Entity.class.getSimpleName();

    private static final long serialVersionUID = -5466989016443709708L;

    static public enum Type {
        CAR_CAMARO, CAR_RIVIERA, CAR_CHEVROLET_CORVETTE, HUMAN, HOUSE, BULLET, SPAWNPOINT, WAYPOINT, PLACEHOLDER, CUBE, SPHERE;
    }

    //TODO: use!
    public static String normalizeName(String name) {
        return name.toUpperCase();
    }

    //public static final PlayerManager DEFAULT_OWNER = PlayerManager.WORLD_PSEUDOPLAYER;

    //	protected Map<Property.Type,Property> properties;
    protected transient final Map<String, Handler> handlers = new HashMap<String, Handler>();
    public final IProperty<Integer> x;
    public final IProperty<Integer> y;
    public final IProperty<Integer> z;
    /**
     * rotation about z
     */
    public final IProperty<Integer> rota;
    public final IProperty<Integer> playerProperty;
    public final Type type;

    //TODO: fix the disconnect between the use of 'type' (serializer) and 'name' (server)

    public Entity(Type type) {
        super(type.name().toLowerCase());
        this.type = type;
        x = this.useProperty("posx", 0);
        y = this.useProperty("posy", 0);
        z = this.useProperty("posz", 0);
        rota = this.useProperty("rota", 0);
//        playerProperty = this.useLazyProperty("player", Player.INVALID_UID);
        playerProperty = this.useProperty("player", Player.INVALID_UID);
    }

    public Entity(String name) {
        super(name);
        this.type = getType(name);
        x = this.useProperty("posx", 0);
        y = this.useProperty("posy", 0);
        z = this.useProperty("posz", 0);
        rota = this.useProperty("rota", 0);
//        playerProperty = this.useLazyProperty("player", Player.INVALID_UID);
        playerProperty = this.useProperty("player", Player.INVALID_UID);
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
        System.out.println("entity owner set to: " + p);
        this.playerProperty.set(p.getUid());
    }

    public void removeOwner() {
        playerProperty.set(Player.INVALID_UID);
    }

    public int getOwnerUid() {
        return this.playerProperty.value();
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
        for (Handler h : handlers.values()) {
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
        for (Handler h : handlers.values()) {
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
