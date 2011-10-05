package net.gtamps.game.property;

import net.gtamps.game.GameObject;
import net.gtamps.game.RevisionKeeper;

import org.jdom.Attribute;
import org.jdom.Element;


public class SpeedProperty extends Property{

	public SpeedProperty(float x, float y, GameObject parent) {
		super(Property.Type.SPEED, parent);
		this.speedX = x;
		this.speedY = y;
	}
	public SpeedProperty(GameObject parent) {
		this(0f, 0f, parent);
	}

	protected double speedX;
	protected double speedY;
	
	@Override
	public Element toXMLElement(long revisionId, RevisionKeeper keeper) {
		Element e = super.toXMLElement(revisionId, keeper);
		if(e != null){
			e.setAttribute(new Attribute("speedX",(int)this.speedX+""));
			e.setAttribute(new Attribute("speedY",(int)this.speedY+""));
		}
		return e;
	}

	@Override
	public String toString() {
		String s = super.toString();
		int split = s.indexOf(' ');
		String head = s.substring(0, split);
		String tail = s.substring(split);
		return head + " x:" + speedX + " y:" + speedY + tail;
	}
	
	public SpeedProperty setSpeedX(double sx) {
		this.speedX = sx;
		this.hasChanged = true;
		this.setParentChanged();
		return this;
	}
	
	public SpeedProperty setSpeedY(double sy) {
		this.speedY = sy;
		this.hasChanged = true;
		this.setParentChanged();
		return this;
	}
	
	public double getSpeedX() {
		return this.speedX;
	}
	
	public double getSpeedY() {
		return this.speedY;
	}
	
	public double getSpeed(){
		return Math.sqrt(this.speedX*this.speedX+this.speedY*this.speedY);
	}
	

}
