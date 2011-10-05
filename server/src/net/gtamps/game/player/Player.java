package net.gtamps.game.player;

import net.gtamps.XmlElements;
import net.gtamps.game.GameActor;
import net.gtamps.game.RevisionKeeper;
import net.gtamps.game.entity.Entity;
import net.gtamps.game.event.EventType;
import net.gtamps.game.event.GameEvent;
import net.gtamps.game.property.IncarnationProperty;
import net.gtamps.game.property.Property;

import org.jdom.Element;

public class Player extends GameActor {
	

	private Entity entity;
	
	Player (String name) {
		this(name, null);
	}
	
	Player(String name, Entity entity) {
		super(name,  XmlElements.PLAYER.tagName());
		if (entity != null) {
			setEntity(entity);
		}
	}
	
	/**
	 * Set the player's representation to the given entity, replacing an 
	 * existing one.
	 * 
	 * @param entity not <code>null</code>.
	 */
	public void setEntity(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("'entity' must not be null");
		}
		removeEntity();
		this.entity = entity;
		entity.setOwner(this);
		((IncarnationProperty)entity.getProperty(Property.Type.INCARNATION)).setPlayer(this);
		entity.addEventListener(EventType.ENTITY_EVENT, this);
		entity.addEventListener(EventType.PLAYER_EVENT, this);
		this.addEventListener(EventType.ACTION_EVENT, entity);
	}
	
	public void removeEntity() {
		if (this.entity == null) {
			return;
		}
		((IncarnationProperty)entity.getProperty(Property.Type.INCARNATION)).removePlayer();
		entity.removeOwner();
		entity.removeEventListener(EventType.GAME_EVENT, this);
		this.removeEventListener(EventType.GAME_EVENT, entity);
		this.entity = null;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	@Override
	public void receiveEvent(GameEvent event) {
		dispatchEvent(event);
	}
	
	@Override
	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
		Element e = super.toXMLElement(baseRevision, keeper);
		Element f = (entity == null) ? null : entity.toXMLElement(baseRevision, keeper);
		if (e != null && f != null) {
			e.addContent(f);
		}
		return e;
	}
}
