package net.gtamps.game.entity;

import net.gtamps.XmlElements;
import net.gtamps.game.GameActor;
import net.gtamps.game.RevisionKeeper;
import net.gtamps.game.event.GameEvent;
import net.gtamps.game.handler.Handler;
import net.gtamps.game.handler.SimplePhysicsHandler;
import net.gtamps.game.player.Player;
import net.gtamps.game.player.PlayerManager;
import net.gtamps.game.property.ActivationProperty;
import net.gtamps.game.property.Property;

import java.util.HashMap;
import java.util.Map;

import org.jdom.Element;

public class Entity extends GameActor {
	public static final Player DEFAULT_OWNER = PlayerManager.WORLD_PSEUDOPLAYER;
	
	protected Map<Property.Type,Property> properties;
	protected final Map<String, Handler> handlers = new HashMap<String, Handler>();
	private Player owner = DEFAULT_OWNER;

	Entity(String name){
		super(name, XmlElements.ENTITY.tagName());
		properties = new HashMap<Property.Type,Property>();
//		this.physicsHandler = new PhysicsHandler(this, physicalRepresentation);
	}
	
	@Override
	public Element toXMLElement(long externalRevision, RevisionKeeper keeper) {
		Element e = super.toXMLElement(externalRevision, keeper);
		if (e != null) {
			for (Property p: properties.values()) {
				Element f = p.toXMLElement(externalRevision, keeper);
				if (f != null) {
					e.addContent(f);
				}
			}
		}
		return e;
	}

	@Override
	public void receiveEvent(GameEvent event) {
		dispatchEvent(event);
	}
	
	void addProperty(Property value) {
		Property.Type key = value.getType();
		if (properties.containsKey(key)) {
			removeProperty(key);
		}
		properties.put(key, value);
		this.connectDownwardsActor(value);
	}
	
	void removeProperty(Property.Type key) {
		Property removed = properties.remove(key);
		if (removed != null) {
			this.disconnectDownwardsActor(removed);
		}
	}
	
	public Property getProperty(Property.Type atttype){
		return properties.get(atttype);
	}
	
	void setPhysicsHandler(SimplePhysicsHandler physicsHandler) {
		assert physicsHandler != null;
		this.setHandler(physicsHandler);
	}
	
	public SimplePhysicsHandler getPhysicsHandler() {
		return (SimplePhysicsHandler) this.getHandler(Handler.Type.PHYSICS);
	}
	
	public void setOwner(Player p) {
		if (p == null) {
			throw new IllegalArgumentException("'p' must not be null");
		}
		this.owner = p;
	}
	
	public void removeOwner() {
		this.owner = DEFAULT_OWNER;
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
		ActivationProperty ap = (ActivationProperty) this.getProperty(Property.Type.ACTIVATION);
		if (ap != null) {
			ap.enable();
		}
	}
	
	@Override
	public void disable() {
		super.disable();
		for (Handler h: handlers.values()) {
			h.disable();
		}
		ActivationProperty ap = (ActivationProperty) this.getProperty(Property.Type.ACTIVATION);
		if (ap != null) {
			ap.disable();
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
		this.disable();
		
	}
	
}
