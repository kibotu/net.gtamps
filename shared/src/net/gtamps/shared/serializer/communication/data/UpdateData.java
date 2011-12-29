package net.gtamps.shared.serializer.communication.data;

import net.gtamps.shared.CheckedShareable;
import net.gtamps.shared.SharedObject;
import net.gtamps.shared.game.GameObject;

import java.util.ArrayList;

/**
 * @deprecated use {@link AbstractSendableData} instead
 */
@Deprecated
public class UpdateData extends SharedObject implements ISendableData {

	private static final long serialVersionUID = -1776226383964319523L;

	public transient long oldRevId;
	public long revId;
	@CheckedShareable
	public ArrayList<GameObject> gameObjects;

	public UpdateData(final long oldRevId, final long newRevId) {
		this.revId = newRevId;
		gameObjects = null;
	}

	@Override
	public String toString() {
		return "UpdateData{" +
				"revId=" + revId +
				", entites=" + gameObjects +
				'}';
	}
}
