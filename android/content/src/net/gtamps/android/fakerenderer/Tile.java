package net.gtamps.android.fakerenderer;

import android.graphics.Bitmap;

public class Tile {

	private Bitmap bitmap;
	private float x;
	private float y;

	Tile(Bitmap b, float x, float y, float height){
		this.bitmap = b;
		this.x = x;
		this.y = y;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public float getY() {
		return 0;
	}
	public float getX() {
		return 0;
	}

}
