package net.gtamps.android.simple3Drenderer;

import javax.microedition.khronos.opengles.GL10;

import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.simple3Drenderer.shapes.AbstractShape;
import net.gtamps.android.simple3Drenderer.shapes.TexturedQuad;
import net.gtamps.shared.game.entity.Entity;

public class SimpleEntityView extends AbstractEntityView{

	private static final float WORLD_TO_GRAPHICS_FACTOR = 32f;
	private AbstractShape texturedQuad;

	public SimpleEntityView(Entity e, AbstractShape shape3D) {
		super(e);
		this.texturedQuad = shape3D;
	}

	@Override
	public void update(Entity serverEntity) {
		this.entity = serverEntity;
	}
	
//	public int getHeight() {
//		return bitmap.getHeight();
//	}

//	public int getWidth() {
//		return bitmap.getWidth();
//	}

//	public Bitmap getBitmap() {
//		return bitmap;
//	}

	public boolean hasBitmap() {
		return false;
	}

	public float getX() {
		return this.entity.x.value()/WORLD_TO_GRAPHICS_FACTOR;
	}

	public float getY() {
		return this.entity.y.value()/WORLD_TO_GRAPHICS_FACTOR;
	}

	public int getRotation() {
		return this.entity.rota.value();
	}

	public void draw(GL10 gl) {
		this.texturedQuad.draw(gl);
	}

	public void set3DShape(TexturedQuad shape3D) {
		this.texturedQuad = shape3D;		
	}

}
