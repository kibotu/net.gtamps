package net.gtamps.android.simple3Drenderer;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.BitmapFactory;

import net.gtamps.android.R;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.fakerenderer.FakeEntityView;
import net.gtamps.android.simple3Drenderer.shapes.TexturedQuad;
import net.gtamps.shared.Utils.Logger;

public class SimpleCamera {

	private GL10 gl;
	private float xtilepos;
	private float ytilepos;
	private float ztilepos;

	private static final float WORLD_TO_GRAPHICS_FACTOR = 32f;

	private static final float CAMERA_SPEED = 5;

	private static final float CAMERA_FOV = 300;

	private float x = 0f;
	private float y = 0f;
	private float z = 30f;
	private float rot = 0f;

	private SimpleWorld world;
	private TexturedQuad human;
	private TexturedQuad car;

	SimpleCamera(SimpleWorld world, Context context) {
		this.world = world;
		try {
			this.human = new TexturedQuad(BitmapFactory.decodeStream(context.getAssets().open("char1_90.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.car = new TexturedQuad(BitmapFactory.decodeStream(context.getAssets().open("car1_90.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		gl.glPushMatrix();

		gl.glTranslatef(ev.entity.x.value() / (WORLD_TO_GRAPHICS_FACTOR) - this.x, -ev.entity.y.value()
				/ (WORLD_TO_GRAPHICS_FACTOR) + this.y, -this.z);
		// matrix.postTranslate(ev.getWidth() / 2, ev.getHeight() / 2);
		if (ev.hasBitmap()) {
			gl.glPushMatrix();
			gl.glTranslatef(-ev.getWidth() / (2 * WORLD_TO_GRAPHICS_FACTOR), -ev.getHeight()
					/ (2 * WORLD_TO_GRAPHICS_FACTOR), 0);
			gl.glRotatef(ev.entity.rota.value(), 0, 0, 1);
			human.draw(gl);
			gl.glPopMatrix();
		} else if (ev.entity.getName().equals("SPAWNPOINT")) {
			car.bindTexture(gl);
			car.draw(gl);
		} else {
			car.bindTexture(gl);
			car.draw(gl);
		}
		gl.glPopMatrix();
	}
}
