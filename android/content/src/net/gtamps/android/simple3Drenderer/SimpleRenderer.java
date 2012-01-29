package net.gtamps.android.simple3Drenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.input.layout.InputLayoutIngame;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.ConnectionThread;
import net.gtamps.android.fakerenderer.FakeCamera;
import net.gtamps.android.fakerenderer.FakeEntityView;
import net.gtamps.android.fakerenderer.FakeWorld;
import net.gtamps.android.game.content.scenes.inputlistener.CameraMovementListener;
import net.gtamps.shared.game.level.Tile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class SimpleRenderer implements Renderer {
	private static final int FPS_AVERAGE_AMOUNT = 10;
	private static final long MAX_FPS = 60;
	private static final boolean LIMIT_FPS = false;

	private static final boolean RENDER_TILES = true;
	private static final boolean RENDER_ENTITIES = true;
	private static final boolean RENDER_HUD = true;
	private static final boolean RENDER_FPS = true;
	private static final int EVERY_N_TH_FRAME = 100;
	// private static final char[] SCORE_LABEL = "SCORE ".toCharArray();
	private static final long MILLIS_PER_FRAME = 1000 / MAX_FPS;

	private SimpleCamera camera;
	private SimpleWorld world;

	// private Bitmap hudScoreBoard;
	// private Bitmap hudFont;
	// private int hudFontCharSize = 16;

	private Context context;

	public SimpleRenderer(Context context) {
		this.context = context;
		this.world = new SimpleWorld(context);
		this.camera = new SimpleCamera(world,context);
		ConnectionThread connection = new ConnectionThread(world);
		new Thread(connection).start();

		InputEngineController.getInstance().setLayout(new InputLayoutIngame(world));
		CameraMovementListener pml = new CameraMovementListener(world, camera);
		InputEngineController.getInstance().getInputEventDispatcher().addInputEventListener(pml);
	}

	// Call back when the surface is first created or re-created
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Set color's clear-value to
													// black
		gl.glClearDepthf(1.0f); // Set depth's clear-value to farthest
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables depth-buffer for hidden
											// surface removal
		gl.glDepthFunc(GL10.GL_LEQUAL); // The type of depth testing to do
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); // nice
																		// perspective
																		// view
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable smooth shading of color
		gl.glDisable(GL10.GL_DITHER); // Disable dithering for better
										// performance

		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

	// Call back after onSurfaceCreated() or whenever the window's size changes
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0)
			height = 1; // To prevent divide by zero
		float aspect = (float) width / height;

		// Set the viewport (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
		gl.glLoadIdentity(); // Reset projection matrix
		// Use perspective projection
		GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); // Select model-view matrix
		gl.glLoadIdentity(); // Reset

		// You OpenGL|ES display re-sizing code here
		// ......
	}

	long lastDraw = System.currentTimeMillis() - 1;
	int avgFPS = 0;
	int FPS = 0;
	int FPScounter = 0;
	int frameCounter = 0;
	private char[] FPSchars = "   FPS".toCharArray();
	private long currentTimeMillis;

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity(); // Reset model-view matrix ( NEW )
		gl.glTranslatef(-1.5f, 0.0f, -6.0f); // Translate left and into the
												// screen ( NEW )

		if (LIMIT_FPS) {
			currentTimeMillis = System.currentTimeMillis();
			if (((currentTimeMillis + 1) - lastDraw) < MILLIS_PER_FRAME) {
				try {
					synchronized (this) {
						wait(MILLIS_PER_FRAME - ((currentTimeMillis + 1) - lastDraw));
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		// clear the color buffer to show the ClearColor we called above...
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.setGL(gl);

		if (world.getActiveView() == null) {
			for (AbstractEntityView aev : world.getAllEntities()) {
				if (aev.entity.getUid() == world.playerManager.getActivePlayer().getEntityUid()) {
					world.setActiveView(aev);
				}
			}
		} else {
			camera.follow(world.getActiveView());
		}

		gl.glPushMatrix();
		gl.glScalef(0.2f, 0.2f, 0.2f);
		if (RENDER_TILES) {
			if (world.getCubeTileMap() != null) {
				synchronized (world) {
					for (CubeTile t : world.getCubeTileMap()) {
						camera.renderTile(t);
					}
				}
			}
		}
		gl.glPopMatrix();

		if (RENDER_ENTITIES) {
			for (AbstractEntityView ev : world.getAllEntities()) {
				camera.renderEntityView((SimpleEntityView) ev, gl);
			}

		}
		if (RENDER_HUD) {
			// drawHUD(gl);
		}

		if (RENDER_FPS) {
			if (FPScounter < FPS_AVERAGE_AMOUNT) {
				FPScounter++;
				avgFPS += (int) (1000 / ((System.currentTimeMillis() + 1) - lastDraw));
			} else {
				FPS = avgFPS / FPScounter;
				avgFPS = 0;
				FPScounter = 0;
			}

			FPSchars[0] = (char) (((FPS / 10) % 10) + 48);
			FPSchars[1] = (char) (((FPS) % 10) + 48);
			// drawHudFont(FPSchars, canvas, 10, 10);
		}
		frameCounter++;
		if (frameCounter > EVERY_N_TH_FRAME) {
			frameCounter = 0;
			everyNthFrameDo();
		}
		lastDraw = System.currentTimeMillis();

	}

	private void everyNthFrameDo() {

	}
}
