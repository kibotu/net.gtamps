package net.gtamps.android.fakerenderer;

import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.shared.game.entity.Entity.Type;
import android.graphics.Canvas;
import android.graphics.Paint;

public class FakeCamera {
	private float x = 0f;
	private float y = 0f;
	private float rot = 0f;
	private Canvas canvas;
	private Paint paint = new Paint();
	FakeCamera(){
		
	}
	
	public void setCanvas(Canvas c){
		this.canvas = c;
	}
	
	public void renderTile(Tile t){
		if(canvas!=null){
			
			canvas.drawBitmap(t.getBitmap(), t.getX()-this.x, t.getY()-this.y, paint);
		}
	}

	public void renderEntityView(FakeEntityView ev) {
		if(canvas!=null){
			Paint paint = new Paint();
			paint.setColor(0xffffffff);
			if(ev.entity.getName().equals("CAR") || ev.entity.type==Type.CAR_RIVIERA || ev.entity.type==Type.CAR_CHEVROLET_CORVETTE ){
				canvas.drawRect( ev.interpolateCoordinateX()-this.x, ev.interpolateCoordinateY()-this.y, 10f, 10f, paint);
			} else if( ev.entity.type == Type.SPAWNPOINT){
				paint.setColor(0xff0000ff);
				canvas.drawCircle( ev.interpolateCoordinateX()-this.x, ev.interpolateCoordinateY()-this.y, 5f, paint);
			} else if(ev.entity.getName().equals("HUMAN")){
				paint.setColor(0xffff0000);
				canvas.drawCircle( ev.interpolateCoordinateX()-this.x, ev.interpolateCoordinateY()-this.y, 5f, paint);
			} else {
				paint.setColor(0xff0000ff);
				canvas.drawCircle( ev.interpolateCoordinateX()-this.x, ev.interpolateCoordinateY()-this.y, 10f, paint);
			}
		}
	}

	public void follow(AbstractEntityView activeView) {
		this.x = activeView.entity.x.value();
		this.y = activeView.entity.y.value();
	}

	public void move(int i, int j) {
		this.x += i;
		this.y += j;
	}
}
