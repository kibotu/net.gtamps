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
	
	private int[] resolution = {1024,600}; 
	
	private float x = 0f;
	private float y = 0f;
	private float rot = 0f;
	private Canvas canvas;
	private Paint paint = new Paint();

	private FakeWorld world;
	

	FakeCamera(Context context, FakeWorld world){
		this.world = world;
	}
	
	public void setCanvas(Canvas c){
		this.canvas = c;
	}
	
	public void renderTile(Tile t){
		if(canvas!=null){
			canvas.drawBitmap(world.getTileBitmap(t.getBitmap()), t.getX()-this.x, t.getY()-this.y, paint);
		}
	}
	Matrix matrix = new Matrix();
	public void renderEntityView(FakeEntityView ev) {
		matrix.reset();
		if(canvas!=null){
			paint.setColor(0xffffffff);
			if( ev.hasBitmap()){
				matrix.reset();
				canvas.save();
					canvas.save();
						canvas.translate(-ev.getWidth()/2,-ev.getHeight()/2);				
						canvas.rotate(ev.entity.rota.value());
						canvas.translate(ev.getWidth()/2,ev.getHeight()/2);
					canvas.restore();
					canvas.translate(ev.interpolateCoordinateX()-this.x, ev.interpolateCoordinateY()-this.y);
					canvas.drawBitmap(ev.getBitmap(), matrix, paint);
				canvas.restore();
			} else if(ev.entity.getName().equals("SPAWNPOINT")){
				paint.setColor(0xff0000ff);
				canvas.drawCircle( ev.interpolateCoordinateX()-this.x, ev.interpolateCoordinateY()-this.y, 5f, paint);
			} else {
				paint.setColor(0xffffffff);
				canvas.drawCircle( ev.interpolateCoordinateX()-this.x, ev.interpolateCoordinateY()-this.y, 3f, paint);
			}
		}
	}

	public void follow(AbstractEntityView activeView) {
		this.x = activeView.entity.x.value()-resolution[0]/2;
		this.y = activeView.entity.y.value()-resolution[1]/2;
	}

	public void move(int i, int j) {
		this.x += i;
		this.y += j;
	}
}
