package net.gtamps.game.property;

import net.gtamps.shared.game.GameObject;

public class PositionProperty extends Property {
	
	
	public PositionProperty(float x, float y, float rotation, GameObject parent) {
		super(Property.Type.POSITION, parent);
		this.x = (int) x;
		this.y = (int) y;
		this.rotation = (int) rotation;
	}

	private int x;
	private int y;
	// degrees!
	private int rotation;

//	@Override
//	public Element toXMLElement(long revisionId, RevisionKeeper keeper) {
//		Element e = super.toXMLElement(revisionId, keeper);
//		if (e != null) {
//			e.setAttribute(new Attribute("posX", this.x + ""));
//			e.setAttribute(new Attribute("posY", this.y + ""));
//			e.setAttribute(new Attribute("rotation", this.rotation + ""));
//		}
//		return e;
//	}

	@Override
	public String toString() {
		String s = super.toString();
		return s + String.format(" x:%.2f y:%.2f rot:%.2f", x, y, rotation);
	}

	public PositionProperty setX(float x) {
		int _x  = (int) x;
		if (_x != this.x) {
			this.x = _x;
			this.hasChanged = true;
			this.setParentChanged();
		}
		return this;
	}

	public PositionProperty setY(float y) {
		int _y = (int) y;
		if (_y != this.y) {
			this.y = _y;
			this.hasChanged = true;
			this.setParentChanged();
		}
		return this;
	}
	
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
	public int getRotation(){
		return this.rotation;
	}

	/**
	 * sets the rotation of the dings in <strike>radians</strike> degrees
	 * 
	 * @param rota
	 *            rotation in <strike>radians</strike> degrees
	 * @return this position property
	 */
	public PositionProperty setRotation(float rota) {
		int _rota = (int) rota;
		if (rota != this.rotation) {
			this.rotation = _rota;
			this.hasChanged = true;
			this.setParentChanged();
		}
		return this;
	}
}
