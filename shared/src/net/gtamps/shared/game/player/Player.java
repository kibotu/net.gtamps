package net.gtamps.shared.game.player;

import net.gtamps.shared.game.GameActor;
import net.gtamps.shared.game.Propertay;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.event.EventType;
import net.gtamps.shared.game.event.GameEvent;

public class Player extends GameActor {
	private static final long serialVersionUID = -8070689911091703487L;
	
	private transient Entity entity;
	
	public Player (String name) {
		this(name, null);
	}
	
	Player(String name, Entity entity) {
		super(name);
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
		//((IncarnationProperty)entity.getProperty(Property.Type.INCARNATION)).setPlayer(this);
		entity.addEventListener(EventType.ENTITY_EVENT, this);
		entity.addEventListener(EventType.PLAYER_EVENT, this);
		this.addEventListener(EventType.ACTION_EVENT, entity);
	}
	
	public void removeEntity() {
		if (this.entity == null) {
			return;
		}
		//((IncarnationProperty)entity.getProperty(Property.Type.INCARNATION)).removePlayer();
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
	
}
