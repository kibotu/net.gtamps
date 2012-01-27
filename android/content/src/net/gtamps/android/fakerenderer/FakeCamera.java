package net.gtamps.android.fakerenderer;

import net.gtamps.android.R;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.entity.Entity.Type;
import net.gtamps.shared.game.level.Tile;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class FakeCamera {

	private static final int TILE_SIZE = 64;

	private static final float CAMERA_SPEED = 5;

	private int[] resolution = { 1024, 600 };

	private float x = 0f;
	private float y = 0f;
	private float rot = 0f;
	private Canvas canvas;
	private Paint paint = new Paint();

	private FakeWorld world;
	private Matrix matrix = new Matrix();

	FakeCamera(Context context, FakeWorld world) {
		this.world = world;
	}

	public void setCanvas(Canvas c) {
		this.canvas = c;
	}

	float xtilepos = 0;
	float ytilepos = 0;

	public void renderTile(Tile t) {
		matrix.reset();
		xtilepos = t.getX() - this.x;
		ytilepos = t.getY() - this.y;
		
		canvas.save();
		canvas.translate(resolution[0] / 2,resolution[1] / 2);
		
		//check if tile is inside screenarea
		if(	xtilepos>-(resolution[0]/2+TILE_SIZE) && xtilepos<resolution[0] &&
			ytilepos>-(resolution[1]/2+TILE_SIZE) && ytilepos<resolution[1] ){
			if (canvas != null) {
				if (world != null) {
					canvas.save();
					canvas.translate(xtilepos, ytilepos);
					matrix.setTranslate(-TILE_SIZE / 2, -TILE_SIZE / 2);
					matrix.postRotate(t.getRotation());
					matrix.postTranslate(TILE_SIZE/2, TILE_SIZE/2);
					canvas.drawBitmap(world.getTileBitmap(t.getBitmap()), matrix, paint);
					canvas.restore();
				} else {
					Logger.e(this, "World is not set!");
				}
			} else {
				Logger.e(this, "Canvas is not set!");
			}
		}
		canvas.restore();
	}

	public void renderEntityView(FakeEntityView ev) {
		matrix.reset();
		if (canvas != null) {
			canvas.save();
			canvas.translate(resolution[0] / 2,resolution[1] / 2);
			paint.setColor(0xffffffff);
			if (ev.hasBitmap()) {
				matrix.reset();
				canvas.save();
				matrix.setTranslate(-ev.getWidth() / 2, -ev.getHeight() / 2);
				matrix.postRotate(ev.entity.rota.value());
				matrix.postTranslate(ev.getWidth() / 2, ev.getHeight() / 2);
				canvas.translate(ev.interpolateCoordinateX() - this.x, ev.interpolateCoordinateY() - this.y);
				canvas.drawBitmap(ev.getBitmap(), matrix, paint);
				canvas.restore();
			} else if (ev.entity.getName().equals("SPAWNPOINT")) {
				paint.setColor(0xff0000ff);
				canvas.drawCircle(ev.interpolateCoordinateX() - this.x, ev.interpolateCoordinateY() - this.y, 5f, paint);
			} else {
				paint.setColor(0xffffffff);
				canvas.drawCircle(ev.interpolateCoordinateX() - this.x, ev.interpolateCoordinateY() - this.y, 3f, paint);
			}
			canvas.restore();
		}
	}

	public void follow(AbstractEntityView activeView) {
		this.x -= (this.x-activeView.entity.x.value() )/CAMERA_SPEED;
		this.y -= (this.y-activeView.entity.y.value() )/CAMERA_SPEED;
	}

	public void move(int i, int j) {
		this.x += i;
		this.y += j;
	}
}
