package net.gtamps.android.fakerenderer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.gtamps.android.R;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.IWorld;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.entity.Entity;

public class FakeWorld implements IWorld {

	private Bitmap car1bitmap;
	private Bitmap car2bitmap;
	private Bitmap characterdeadbitmap;
	private Bitmap characterbitmap;
	FakeWorld(Context context){
		car1bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.car1_90);
		car2bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.car2);
		characterbitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.char1_90);
		characterdeadbitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.char1dead);
	}
	
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
		Log.d("FakeWorld", "Adding Entity! "+entityView.entity.getName());
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
		if(e.getName().equals("CAR")){
			return new FakeEntityView(e,car1bitmap);
		} else if(e.getName().equals("HUMAN")){
			return new FakeEntityView(e,characterbitmap);
		} else {
			return new FakeEntityView(e, null);
		}
	}

}
