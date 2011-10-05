package net.gtamps.game.property;

import net.gtamps.XmlElements;
import net.gtamps.game.GameObject;
import net.gtamps.game.RevisionKeeper;
import net.gtamps.game.conf.WorldConstants;
import net.gtamps.game.entity.Entity;
import net.gtamps.game.event.BulletHitEvent;
import net.gtamps.game.event.CollisionEvent;
import net.gtamps.game.event.EventType;
import net.gtamps.game.event.GameEvent;
import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;

import org.jdom.Attribute;
import org.jdom.Element;


public class HealthProperty extends Property{
	
	private static final LogType TAG = LogType.GAMEWORLD;

	protected int health;
	protected int maxHealth;
	protected float dmgResistance = 0f;
	protected int dmgThreshold = 0;

	public HealthProperty(GameObject parent, int maxHealth, float resistance, int threshold) {
		super(Property.Type.HEALTH, parent);
		this.maxHealth = maxHealth;
		this.health = maxHealth;
		this.dmgResistance = resistance;
		this.dmgThreshold = threshold;
		this.setSendsUp(new EventType[] {EventType.ENTITY_DAMAGE, EventType.ENTITY_DESTROYED});
		this.setReceivesDown(new EventType[] {EventType.ENTITY_COLLIDE, EventType.ENTITY_BULLET_HIT});
	}
	
	public HealthProperty(GameObject parent, int maxHealth) {
		this(parent, maxHealth, 0f, 0);
	}
	
	@Override
	public Element toXMLElement(long revisionId, RevisionKeeper keeper) {
		Element e = super.toXMLElement(revisionId, keeper);
		if(e != null){
			e.setAttribute(new Attribute(XmlElements.ATTRIB_VALUE.tagName(),this.health+""));
		}
		return e;
	}

	@Override
	public String toString() {
		String s = super.toString();
		int split = s.indexOf(' ');
		String head = s.substring(0, split);
		String tail = s.substring(split);
		return head + " health:" + health + tail;
	}
	
	@Override
	public void receiveEvent(GameEvent event) {
		EventType type = event.getType();
		if (type.isType(EventType.ENTITY_COLLIDE)) {
			float impulse = ((CollisionEvent)event).getImpulse();
			this.takeDamage(impulse);
		}
		if (type.isType(EventType.ENTITY_BULLET_HIT)) {
			float impulse = ((BulletHitEvent)event).getImpulse();
			this.takeDamage(impulse*WorldConstants.BULLET_IMPULSE_DAMAGE_AMPLIFICATION);
			if(this.parent.getName().equals("bullet")){
				this.health = 0;
				((Entity)this.parent).destroy();
			}
		}
	}
	
	public void setHealth(int value) {
		this.health = (value < 0) ? 0 : value;
		this.hasChanged = true;
		this.setParentChanged();
		if (this.health <= 0) {
			die();
		}
	}
	
	public void takeDamage(float force) {
		int damage = (int) (force * (1-dmgResistance) - dmgThreshold);
		if (damage > 0 && health > 0) {
			setHealth(health - damage);
			Logger.i().log(TAG, String.format(this.parent + "damaged by %d (%d remaining)", 
					damage, health));
		}
	}
	
	public boolean isAlive() {
		return this.health > 0;
	}
	
	private void die() {
		Logger.i().log(TAG, String.format("%s destroyed", parent));
		GameEvent death = new GameEvent(EventType.ENTITY_DESTROYED, this.parent);
		this.dispatchEvent(death);
	}
	
}
