package net.gtamps.android.gtandroid.core.net;

import net.gtamps.shared.game.entity.Entity;

public abstract class AbstractEntityView {

	public Entity entity;
	protected boolean activated = true; 
	public abstract void update(Entity serverEntity);
	
	public AbstractEntityView(Entity e) {
		this.entity = e;
	}

	public abstract void deactivate();

}
