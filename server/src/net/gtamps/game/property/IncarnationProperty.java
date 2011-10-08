package net.gtamps.game.property;

import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.player.Player;

public class IncarnationProperty extends Property {

	private static final LogType TAG = LogType.GAMEWORLD;
	
	private Player player = null;
	
	public IncarnationProperty(GameObject parent) {
		super(Property.Type.INCARNATION, parent);
		this.hasChanged = false;
	}
	
//	@Override
//	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
//		Element e = super.toXMLElement(baseRevision, keeper);
//		if (e != null && this.player != null) {
//			e.setAttribute(new Attribute(XmlElements.ATTRIB_VALUE.tagName(), this.player.getName()));
//		}
//		return e;
//	}
	
	public void setPlayer(Player player) {
		if (player == null) {
			throw new IllegalArgumentException("'player' must not be null");
		}
		Logger.i().log(TAG, player + " incarnation: " + this.parent);
		this.player = player;
		this.hasChanged = true;
		this.setParentChanged();
	}
	
	public void removePlayer() {
		this.player = null;
	}
	

}
