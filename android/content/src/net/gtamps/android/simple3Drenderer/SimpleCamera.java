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

	private static final float CAMERA_BIRDSEYE_SPEED = 25;
	private static final float CAMERA_FOLLOWER_SPEED = 10;

	// private static final float CAMERA_FOV = 300;
	private static final float ENTITY_GROUND_DISTANCE = -0.4f;

	private static final float CAMERA_FOLLOWER_BIAS = -40; // degree
	private static final float CAMERA_FOLLOWER_HEIGHT = 10f;

	private static final float CAMERA_BIRDSEYE_HEIGHT = 300f;
	private static final float CAMERA_BIRDSEYE_BIAS = 0; // degree
	private static final float CAMERA_BIRDSEYE_ROTATION = 0;

	private float x = 0f;
	private float y = 0f;
	private float z = CAMERA_BIRDSEYE_HEIGHT;
	private float rotx = 0f;
	private float roty = 0f;
	private float rotz = 0f;

	private SimpleWorld world;

	SimpleCamera(SimpleWorld world, Context context) {
		this.world = world;

	}

	public void setGL(GL10 gl) {
		this.gl = gl;
	}

	public void follow(AbstractEntityView activeView) {
		if (activeView.entity.getName().equals("CAR")) {
			this.x -= (this.x - (activeView.entity.x.value() / WORLD_TO_GRAPHICS_FACTOR)  ) / CAMERA_FOLLOWER_SPEED;
			this.y -= (this.y - (activeView.entity.y.value() / WORLD_TO_GRAPHICS_FACTOR)  ) / CAMERA_FOLLOWER_SPEED;
			this.z -= (this.z - CAMERA_FOLLOWER_HEIGHT / WORLD_TO_GRAPHICS_FACTOR) / CAMERA_FOLLOWER_SPEED;
			this.rotx -= (this.rotx - CAMERA_FOLLOWER_BIAS) / CAMERA_FOLLOWER_SPEED;
			this.rotz -= (this.rotz - (activeView.entity.rota.value() + 90)) / CAMERA_FOLLOWER_SPEED;
		} else {
			this.x -= (this.x - activeView.entity.x.value() / WORLD_TO_GRAPHICS_FACTOR) / CAMERA_BIRDSEYE_SPEED;
			this.y -= (this.y - activeView.entity.y.value() / WORLD_TO_GRAPHICS_FACTOR) / CAMERA_BIRDSEYE_SPEED;
			this.z -= (this.z - CAMERA_BIRDSEYE_HEIGHT / WORLD_TO_GRAPHICS_FACTOR) / CAMERA_BIRDSEYE_SPEED;
			this.rotx -= (this.rotx - CAMERA_BIRDSEYE_BIAS) / CAMERA_BIRDSEYE_SPEED;
			this.rotz -= (this.rotz - CAMERA_BIRDSEYE_ROTATION) / CAMERA_BIRDSEYE_SPEED;
		}
	}

	public void renderTile(CubeTile t) {

		if (gl != null) {
				gl.glPushMatrix();
				xtilepos = t.getX() - this.x;
				ytilepos = -t.getY() + this.y;
				ztilepos = - this.z;
				gl.glRotatef(this.rotx, 1, 0, 0);
				gl.glRotatef(this.rotz, 0, 0, 1);

				if (world != null) {
					gl.glTranslatef(xtilepos, ytilepos, ztilepos);
					gl.glScalef(1f, 1f, t.getHeight());
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
			gl.glRotatef(this.rotx, 1, 0, 0);
			gl.glRotatef(this.rotz, 0, 0, 1);

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

	}
}
