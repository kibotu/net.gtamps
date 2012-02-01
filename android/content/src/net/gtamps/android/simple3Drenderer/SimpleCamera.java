package net.gtamps.android.simple3Drenderer;

import javax.microedition.khronos.opengles.GL10;

import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.shared.Utils.Logger;
import android.content.Context;

public class SimpleCamera {

	private GL10 gl;
	private float xtilepos;
	private float ytilepos;
	private float ztilepos;

	private static final float WORLD_TO_GRAPHICS_FACTOR = 32f;

	private static final float CAMERA_SPEED = 5;

//	private static final float CAMERA_FOV = 300;
	private static final float ENTITY_GROUND_DISTANCE = 1.3f;

	private float x = 0f;
	private float y = 0f;
	private float z = 30f;
	private float rot = 0f;

	private SimpleWorld world;

	SimpleCamera(SimpleWorld world, Context context) {
		this.world = world;

		/*try {
//			this.car = new TexturedQuad(BitmapFactory.decodeStream(context.getAssets().open("car1_90.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	public void setGL(GL10 gl) {
		this.gl = gl;
	}

	public void follow(AbstractEntityView activeView) {
		this.x -= (this.x - activeView.entity.x.value() / WORLD_TO_GRAPHICS_FACTOR) / CAMERA_SPEED;
		this.y -= (this.y - activeView.entity.y.value() / WORLD_TO_GRAPHICS_FACTOR) / CAMERA_SPEED;
	}

	public void renderTile(CubeTile t) {

		if (gl != null) {
			gl.glPushMatrix();
			xtilepos = t.getX() - this.x;
			ytilepos = -t.getY() + this.y;
			ztilepos = t.getHeight() - this.z;
			gl.glRotatef(this.rot, 0, 0, 1);
			
			if (world != null) {
				gl.glTranslatef(xtilepos, ytilepos, ztilepos);
				gl.glRotatef(-t.getRotation(), 0, 0, 1);
				t.draw(gl);
			} else {
				Logger.e(this, "World is not set!");
			}
			gl.glPopMatrix();
		} else {
			Logger.e(this, "GL Context is not set");
		}
	}

	public void move(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void renderEntityView(SimpleEntityView ev, GL10 gl) {
		
		if (gl != null) {
			gl.glPushMatrix();
			xtilepos = ev.getX() - this.x;
			ytilepos = -ev.getY() + this.y;
			ztilepos = ENTITY_GROUND_DISTANCE - this.z;
			gl.glRotatef(this.rot, 0, 0, 1);

			if (world != null) {
				gl.glTranslatef(xtilepos, ytilepos, ztilepos);
				gl.glRotatef(-ev.getRotation(), 0, 0, 1);
				ev.draw(gl);
			} else {
				Logger.e(this, "World is not set!");
			}
			gl.glPopMatrix();
		} else {
			Logger.e(this, "GL Context is not set");
		}
		
//		gl.glPushMatrix();
//
//		gl.glTranslatef(ev.entity.x.value() / (WORLD_TO_GRAPHICS_FACTOR) - this.x, -ev.entity.y.value()
//				/ (WORLD_TO_GRAPHICS_FACTOR) + this.y, -this.z);
//		if (ev.hasBitmap()) {
//			gl.glPushMatrix();
//			gl.glTranslatef(-ev.getWidth() / (2 * WORLD_TO_GRAPHICS_FACTOR), -ev.getHeight()
//					/ (2 * WORLD_TO_GRAPHICS_FACTOR), 0);
//			gl.glRotatef(ev.entity.rota.value(), 0, 0, 1);
//			human.draw(gl);
//			gl.glPopMatrix();
//		} else if (ev.entity.getName().equals("SPAWNPOINT")) {
//			car.bindTexture(gl);
//			car.draw(gl);
//		} else {
//			car.bindTexture(gl);
//			car.draw(gl);
//		}
//		gl.glPopMatrix();
	}
}
