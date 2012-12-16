package net.gtamps.android.fakerenderer;

import android.graphics.Bitmap;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.shared.game.entity.Entity;

public class FakeEntityView extends AbstractEntityView {
	
	private static final long INTERPOLATION_TIME_MILLIS = 200;
	private Entity lastEntity;
	private long millisUpdateTimeStamp;
	private Bitmap bitmap;
	
	public FakeEntityView(Entity iev, Bitmap b){
		super(iev);
		this.bitmap = b;
		this.lastEntity = iev;
		millisUpdateTimeStamp = System.currentTimeMillis();		
	}
	
	@Override
	public void update(Entity serverEntity) {
		this.lastEntity = this.entity;
		this.entity = serverEntity;
		this.activated = true;
		millisUpdateTimeStamp = System.currentTimeMillis();
	}
	
	public float interpolateCoordinateX(){
		if(System.currentTimeMillis()-millisUpdateTimeStamp<INTERPOLATION_TIME_MILLIS){
			return this.entity.x.value()+((this.entity.x.value()-this.lastEntity.x.value())*(System.currentTimeMillis()-millisUpdateTimeStamp))/INTERPOLATION_TIME_MILLIS;
		}
		return this.entity.x.value();
	}
	
	public float interpolateCoordinateY(){
		if(System.currentTimeMillis()-millisUpdateTimeStamp<INTERPOLATION_TIME_MILLIS){
			return this.entity.y.value()+((this.entity.y.value()-this.lastEntity.y.value())*(System.currentTimeMillis()-millisUpdateTimeStamp))/INTERPOLATION_TIME_MILLIS;
		}
		return this.entity.y.value();

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
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public boolean hasBitmap() {
		return bitmap!=null;
	}

	@Override
	public void deactivate() {
		this.activated = false;
	}

}
