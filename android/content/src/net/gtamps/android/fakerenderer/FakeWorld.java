package net.gtamps.android.fakerenderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.gtamps.android.R;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.IWorld;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.level.Tile;

public class FakeWorld implements IWorld {

	private Bitmap car1bitmap;
	private Bitmap car2bitmap;
	private Bitmap characterdeadbitmap;
	private Bitmap characterbitmap;
	private HashMap<String, Bitmap> tileBitmapLookup = new HashMap<String, Bitmap>();
	private Bitmap defaulttile;
	private AssetManager assetManager;

	FakeWorld(Context context) {
		car1bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.car1_90);
		car2bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.car2);
		characterbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.char1_90);
		characterdeadbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.char1dead);
		defaulttile = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaulttile);
		assetManager = context.getAssets();
	}

	private HashMap<Integer, AbstractEntityView> fakeEntityMap = new HashMap<Integer, AbstractEntityView>();
	AbstractEntityView activeEntityView;
	private LinkedList<Tile> tileMap;

	@Override
	public AbstractEntityView getViewById(int uid) {
		synchronized (this) {
			return fakeEntityMap.get(uid);
		}
	}

	@Override
	public void setActiveView(AbstractEntityView entityView) {
		activeEntityView = entityView;
	}

	@Override
	public void add(AbstractEntityView entityView) {
		Log.d("FakeWorld", "Adding Entity! " + entityView.entity.getName());
		synchronized (this) {
			this.fakeEntityMap.put(entityView.entity.getUid(), entityView);
		}
		refreshEntityViewList();
	}

	LinkedList<AbstractEntityView> entityViewList = null; 
	@Override
	public List<AbstractEntityView> getAllEntities() {
		if(entityViewList==null){
			refreshEntityViewList();
		}
		return entityViewList;
	}

	private void refreshEntityViewList() {
		synchronized (this) {
			entityViewList = new LinkedList<AbstractEntityView>(this.fakeEntityMap.values());
		}		
	}

	@Override
	public AbstractEntityView getActiveView() {
		return activeEntityView;
	}

	@Override
	public AbstractEntityView createEntityView(Entity e) {
		if (e.getName().toUpperCase().equals("CAR")) {
			return new FakeEntityView(e, car1bitmap);
		} else if (e.getName().toUpperCase().equals("HUMAN")) {
			return new FakeEntityView(e, characterbitmap);
		} else {
			return new FakeEntityView(e, null);
		}
	}

	@Override
	public boolean supports2DTileMap() {
		return true;
	}

	@Override
	public void setTileMap(LinkedList<Tile> tileMap) {
		for (Tile t : tileMap) {
			if (!tileBitmapLookup.containsKey(t.getBitmap())) {
				Bitmap bitmapLoader = null;
				try {
					bitmapLoader = BitmapFactory.decodeStream(assetManager.open("tiles/" + t.getBitmap()));
				} catch (IOException e) {
					Logger.e(this, "unable to load tile: " + t.getBitmap());
				}
				if (bitmapLoader != null) {
					tileBitmapLookup.put(t.getBitmap(), bitmapLoader);
					Logger.e(this, "Sucessfully loaded tile: " + t.getBitmap());
				}
			}
		}
		this.tileMap = tileMap;
	}

	public LinkedList<Tile> getTileMap() {
		return tileMap;
	}

	// preallocate
	Bitmap returnBitmap = null;
	private int playerFragScore;

	public Bitmap getTileBitmap(String bitmap) {
		returnBitmap = tileBitmapLookup.get(bitmap);
		if (returnBitmap == null) {
			return defaulttile;
		}
		return returnBitmap;
	}

	@Override
	public void remove(int targetUid) {
		fakeEntityMap.remove(targetUid);
		refreshEntityViewList();
	}

	public void ensureEntityAppearance() {
		for (AbstractEntityView aev : getAllEntities()) {
			FakeEntityView fev = (FakeEntityView) aev;
			if (!fev.hasBitmap()) {
				if (aev.entity.getName().equals("HUMAN")) {
					fev.setBitmap(characterbitmap);
				} else if (aev.entity.getName().equals("CAR")) {
					fev.setBitmap(car1bitmap);
				}
			}
		}
	}

	@Override
	public void setPlayerFragScore(int count) {
		this.playerFragScore = count;
	}
	
	@Override
	public int getPlayerFragScore() {
		return playerFragScore;
	}

	@Override
	public void deactivate(int targetUid) {
		this.fakeEntityMap.get(targetUid).deactivate();
	}

}
