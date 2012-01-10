package net.gtamps.shared.game;

import java.util.ArrayList;
import java.util.List;

public class GameObjectUpdater {

	String name;
	int uid;
	long revision;

	final boolean hasChanged = true;
	final boolean isSilent = false;

	private final List<IProperty<?>> properties = new ArrayList<IProperty<?>>();

	public GameObjectUpdater() {
		reset();
	}

	public void reset() {
		uid = GameObject.INVALID_UID;
		name = "";
		revision = 0;
		properties.clear(); 
	}

	public void set(final int uid, final String name, final long revision) {
		this.uid = uid;
		this.name = name;
		this.revision = revision;
	}

	public void addProperty(final IProperty<?> p) {
		properties.add(p);
	}



	public void update(final GameObject gob) {
		validateCorrectGameobject(gob);
		gob.updateRevision(revision);
		for (final IProperty<?> p : properties) {
			updateProperty(gob, p);
		}
	}

	private void updateProperty(final GameObject gob, final IProperty<?> p) {
		//TODO 
	}

	private void validateCorrectGameobject(final GameObject gob) {
		if (gob.uid != uid || !gob.name.equals(name)) {
			throw new IllegalArgumentException("can't update GameObject: uid or name don't match");
		}
	}

}
