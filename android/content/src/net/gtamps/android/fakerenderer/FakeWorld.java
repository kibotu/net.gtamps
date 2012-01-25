package net.gtamps.android.fakerenderer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.IWorld;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.entity.Entity;

public class FakeWorld implements IWorld {

	private HashMap<Integer, AbstractEntityView> fakeEntityMap = new HashMap<Integer, AbstractEntityView>();
	AbstractEntityView activeEntityView;
	
	@Override
	public AbstractEntityView getViewById(int uid) {
		return fakeEntityMap.get(uid);
	}

	@Override
	public void setActiveView(AbstractEntityView entityView) {
		activeEntityView = entityView;
	}

	@Override
	public void add(AbstractEntityView entityView) {
		Logger.d(this, "Adding Entity!");
		this.fakeEntityMap.put(entityView.entity.getUid(), entityView);
	}

	@Override
	public List<AbstractEntityView> getAllEntities() {
		return new LinkedList<AbstractEntityView>(this.fakeEntityMap.values());
	}

	@Override
	public AbstractEntityView getActiveView() {
		return activeEntityView;
	}

	@Override
	public AbstractEntityView createEntityView(Entity e) {
		return new FakeEntityView(e);
	}

}
