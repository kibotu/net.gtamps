package net.gtamps.android.simple3Drenderer;

import android.graphics.Bitmap;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.shared.game.entity.Entity;

public class SimpleEntityView extends AbstractEntityView{

	private Bitmap bitmap;

	public SimpleEntityView(Entity e, Bitmap car1bitmap) {
		super(e);
		this.bitmap = car1bitmap;
	}

	@Override
	public void update(Entity serverEntity) {
		this.entity = serverEntity;
	}
	
	public int getHeight() {
		return bitmap.getHeight();
	}

	public int getWidth() {
		return bitmap.getWidth();
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public boolean hasBitmap() {
		return false;
	}

}
