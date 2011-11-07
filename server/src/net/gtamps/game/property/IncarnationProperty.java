package net.gtamps.game.property;

import net.gtamps.server.gui.LogType;
import net.gtamps.server.gui.Logger;
import net.gtamps.shared.game.GameObject;
import net.gtamps.shared.game.player.Player;

/**
 *
 * @deprecated kept on for legacy code reference
 * 
 * @author Jan Rabe, Tom Wallroth, Til Boerner
 *
 */
@Deprecated
public class IncarnationProperty extends Property {

	private static final LogType TAG = LogType.GAMEWORLD;
	
	private Player player = null;
	
	public IncarnationProperty(final GameObject parent) {
		super(Property.Type.INCARNATION, parent);
//		hasChanged = false;
	}
	
//	@Override
//	public Element toXMLElement(long baseRevision, RevisionKeeper keeper) {
//		Element e = super.toXMLElement(baseRevision, keeper);
//		if (e != null && this.player != null) {
//			e.setAttribute(new Attribute(XmlElements.ATTRIB_VALUE.tagName(), this.player.getName()));
//		}
//		return e;
//	}
	
	public void setPlayer(final Player player) {
		if (player == null) {
			throw new IllegalArgumentException("'player' must not be null");
		}
		Logger.i().log(TAG, player + " incarnation: " + parent);
		this.player = player;
//		hasChanged = true;
		setParentChanged();
	}
	
	public void removePlayer() {
		player = null;
	}
	

}
