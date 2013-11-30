package net.gtamps.android.simple3Drenderer;

import javax.microedition.khronos.opengles.GL10;

import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.simple3Drenderer.shapes.AbstractShape;
import net.gtamps.android.simple3Drenderer.shapes.TexturedQuad;
import net.gtamps.shared.game.entity.Entity;
import net.gtamps.shared.serializer.communication.StringConstants;

public class SimpleEntityView extends AbstractEntityView{

	private static final float WORLD_TO_GRAPHICS_FACTOR = 32f;
	private AbstractShape texturedQuad;
	private long lastUpdate;

	public SimpleEntityView(Entity e, AbstractShape shape3D) {
		super(e);
		this.texturedQuad = shape3D;
		this.lastUpdate = System.currentTimeMillis();
	}

	@Override
	public void update(Entity serverEntity) {
		this.entity = serverEntity;
		this.activated = true;
		this.lastUpdate = System.currentTimeMillis();
	}
	
	public float getSpeedX(){
		return this.entity.useProperty(StringConstants.PROPERTY_SPEEDX, 0).value()/WORLD_TO_GRAPHICS_FACTOR;
	}
	
	public float getSpeedY(){
		return this.entity.useProperty(StringConstants.PROPERTY_SPEEDY, 0).value()/WORLD_TO_GRAPHICS_FACTOR;
	}
	
	public long getLastUpdateMillis(){
		return System.currentTimeMillis()-lastUpdate;
	}
	
	public boolean hasBitmap() {
		return false;
	}

	public float getX() {
		return (this.entity.x.value())/WORLD_TO_GRAPHICS_FACTOR;
	}

	public float getY() {
		return (this.entity.y.value())/WORLD_TO_GRAPHICS_FACTOR;
	}

	public int getRotation() {
		return this.entity.rota.value();
	}

	public void draw(GL10 gl) {
		if(this.activated){
			this.texturedQuad.draw(gl);
		}
	}

	public void set3DShape(AbstractShape character1) {
		this.texturedQuad = character1;		
	}

	public void bindTexture(GL10 gl) {
		this.texturedQuad.bindTexture(gl);
	}

	@Override
	public void deactivate() {
		this.activated = false;
	}

	public float getLastUpdateSeconds() {
		return getLastUpdateMillis()/1000f;
	}

}
