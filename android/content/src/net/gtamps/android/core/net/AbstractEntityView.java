package net.gtamps.android.core.net;

import net.gtamps.shared.game.entity.Entity;

public abstract class AbstractEntityView {

	public Entity entity;
	
	public abstract void update(Entity serverEntity);
	
	public AbstractEntityView(Entity e) {
		this.entity = e;
	}

}
