package net.gtamps.android.simple3Drenderer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.IWorld;
import net.gtamps.android.simple3Drenderer.shapes.AbstractShape;
import net.gtamps.android.simple3Drenderer.shapes.TexturedCube;
import net.gtamps.android.simple3Drenderer.shapes.TexturedQuad;
import net.gtamps.android.simple3Drenderer.textures.TileBitmapBuilder;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.game.level.Tile;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class SimpleWorld implements IWorld {

	private AbstractShape character1;
	private AbstractShape car1;
	private AbstractShape bullet;
	private AbstractShape defaultshape;
	private AssetManager assetManager;
//	private TexturedQuad spawnpoint;

	SimpleWorld(Context context) {
		// car1bitmap = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.car1_90);
		// car2bitmap = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.car2);
		// characterbitmap =
		// BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.char1_90);
		// characterdeadbitmap =
		// BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.char1dead);
		// defaulttile = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.defaulttile);
		assetManager = context.getAssets();

		TileBitmapBuilder entityTextures = new TileBitmapBuilder(assetManager, "entities");
		entityTextures.putIfAbsent("char1_90.png");
		entityTextures.putIfAbsent("car1_90.png");
		entityTextures.putIfAbsent("bullet.png");
//		entityTextures.putIfAbsent("spawnpoint.png");
		entityTextures.putIfAbsent("default.png");
		StaticTextureHolder.add(entityTextures.generateTextureMapper());
		character1 = new TexturedQuad("char1_90.png", 0.3f, 0.3f);
		car1 = new TexturedQuad("car1_90.png", 1.0f, 0.5f);
		bullet = new TexturedQuad("bullet.png", 0.15f, 0.15f);
//		spawnpoint = new TexturedQuad("spawnpoint.png", 0.15f, 0.15f);
		defaultshape = new TexturedQuad("default.png");
		
		TileBitmapBuilder spritefont = new TileBitmapBuilder(assetManager, "spritefont");
		spritefont.putIfAbsent("spritefont_256.png");
		StaticTextureHolder.add(spritefont.putInsideTextureMapper());
	}

	private HashMap<Integer, AbstractEntityView> fakeEntityMap = new HashMap<Integer, AbstractEntityView>();

	AbstractEntityView activeEntityView;
	private LinkedList<CubeTile> cubeTileMap;

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
		if (entityViewList == null) {
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
	public SimpleEntityView createEntityView(Entity e) {
		if (e.getName().toUpperCase().equals("CAR")) {
			return new SimpleEntityView(e, car1);
		} else if (e.getName().toUpperCase().equals("HUMAN")) {
			return new SimpleEntityView(e, character1);
		} else if (e.getName().toUpperCase().equals("BULLET")) {
			return new SimpleEntityView(e, bullet);
		} else if (e.getName().toUpperCase().equals("SPAWNPOINT")) {
			return new SimpleEntityView(e, defaultshape);
		} else {
			return new SimpleEntityView(e, defaultshape);
		}
	}

	@Override
	public boolean supports2DTileMap() {
		return true;
	}

	@Override
	public void setTileMap(LinkedList<Tile> tileMap) {
		synchronized (this) {
			this.cubeTileMap = new LinkedList<CubeTile>();
		}
		synchronized (this) {
			TileBitmapBuilder tbb = new TileBitmapBuilder(assetManager, "tiles");
			for (Tile t : tileMap) {
				tbb.putIfAbsent(t.getBitmap());
			}
			StaticTextureHolder.add(tbb.generateTextureMapper());
			// now that all textures have been loaded, create the corresponding
			// 3d objects
			for (Tile t : tileMap) {
				if (t.getHeight() == 0) {
					this.cubeTileMap.add(new CubeTile(t, new TexturedQuad(t.getBitmap())));
				} else {
					this.cubeTileMap.add(new CubeTile(t, new TexturedCube(t.getBitmap())));
				}
			}
		}
		// this.tileMap = tileMap;
	}

	private int fragCount;

	/*
	 * // preallocate Bitmap returnBitmap = null;
	 * 
	 * public Bitmap getTileBitmap(String bitmap) { returnBitmap =
	 * tileBitmapLookup.get(bitmap); if (returnBitmap == null) { return
	 * defaulttile; } return returnBitmap; }
	 */

	@Override
	public void remove(int targetUid) {
		fakeEntityMap.remove(targetUid);
		refreshEntityViewList();
	}

	public void ensureEntityAppearance() {
		for (AbstractEntityView aev : getAllEntities()) {
			SimpleEntityView fev = (SimpleEntityView) aev;
			if (!fev.hasBitmap()) {
				if (aev.entity.getName().equals("HUMAN")) {
					fev.set3DShape(this.character1);
				} else if (aev.entity.getName().equals("CAR")) {
					fev.set3DShape(this.car1);
				} else if (aev.entity.getName().equals("SPAWNPOINT")) {
					fev.set3DShape(this.defaultshape);
				}
			}
		}

	}

	public LinkedList<CubeTile> getCubeTileMap() {
		return cubeTileMap;
	}

	@Override
	public void setPlayerFragScore(int count) {
		this.fragCount = count;
	}

	@Override
	public int getPlayerFragScore() {
		return fragCount;
	}

}
