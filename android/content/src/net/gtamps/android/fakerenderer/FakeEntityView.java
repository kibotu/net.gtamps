package net.gtamps.android.fakerenderer;

import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.shared.game.entity.Entity;

public class FakeEntityView extends AbstractEntityView {
	
	private static final long INTERPOLATION_TIME_MILLIS = 1000;
	private Entity lastEntity;
	private long millisUpdateTimeStamp;
	
	public FakeEntityView(Entity iev){
		super(iev);
		this.lastEntity = iev;
		millisUpdateTimeStamp = System.currentTimeMillis();
		
	}
	
	@Override
	public void update(Entity serverEntity) {
		this.lastEntity = this.entity;
		this.entity = serverEntity; 
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

}