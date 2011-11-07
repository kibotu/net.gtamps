package net.gtamps.game.property;

import net.gtamps.shared.game.GameObject;

// TODO create handler??

@Deprecated
public class ActivationProperty extends Property{

	private boolean isEnabled = true;
	
	public ActivationProperty(final GameObject parent) {
		super(Property.Type.ACTIVATION, parent);
	}
	
	@Override
	public void enable() {
		isEnabled = true;
//		hasChanged = true;
		setParentChanged();
	}
	@Override
	public void disable(){
		isEnabled = false;
//		hasChanged = true;
		setParentChanged();
	}
	@Override
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
		final String s = super.toString();
		return s+" is activated: "+isEnabled();
	}

}
