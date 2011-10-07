package net.gtamps.game.property;

import net.gtamps.shared.game.GameObject;

public class ActivationProperty extends Property{

	private boolean isEnabled = true;
	
	public ActivationProperty(GameObject parent) {
		super(Property.Type.ACTIVATION, parent);
	}
	
	public void enable() {
		this.isEnabled = true;
		this.hasChanged = true;
		this.setParentChanged();
	}
	public void disable(){
		this.isEnabled = false;
		this.hasChanged = true;
		this.setParentChanged();
	}
	public boolean isEnabled(){
		return isEnabled;
	}
	
//	@Override
//	public Element toXMLElement(long revisionId, RevisionKeeper keeper) {
//		Element e = super.toXMLElement(revisionId, keeper);
//		if(e != null){
//			e.setAttribute(new Attribute(XmlElements.ATTRIB_VALUE.tagName(),this.isEnabled+""));
//		}
//		return e;
//	}
	
	@Override
	public String toString() {
		String s = super.toString();
		return s+" is activated: "+this.isEnabled();
	}

}
