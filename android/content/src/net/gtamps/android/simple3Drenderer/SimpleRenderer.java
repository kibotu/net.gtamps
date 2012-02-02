package net.gtamps.android.simple3Drenderer;

import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.input.inputlistener.CameraMovementListener;
import net.gtamps.android.core.input.layout.InputLayoutIngame;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.threaded.ConnectionThread;
import net.gtamps.android.simple3Drenderer.helper.GL10Utils;
import net.gtamps.android.simple3Drenderer.shapes.TexturedQuad;
import android.content.Context;
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
	private static final int EVERY_N_TH_FRAME = 50;
	private int SCORE_COUNTER_OFFSET = 6;
	private static final char[] SCORE_LABEL = "SCORE         ".toCharArray();
	private static final long MILLIS_PER_FRAME = 1000 / MAX_FPS;

	private SimpleCamera camera;
	private SimpleWorld world;

	TexturedQuad[] fontQuads = new TexturedQuad[256];

	// private Bitmap hudScoreBoard;
	// private Bitmap hudFont;
	// private int hudFontCharSize = 16;

	// private Context context;

	public SimpleRenderer(Context context, SimpleWorld world) {
		this.world = world;
		// this.context = context;
		this.camera = new SimpleCamera(this.world, context);
		

		for (int i = 0; i < 256; i++) {
			float[] f = { 	
							(1f / 16f) * ((i) % 16), 	(1f / 16f) * ((i / 16)+1),
							(1f / 16f) * ((i % 16)+1),	(1f / 16f) * ((i / 16)+1),
							(1f / 16f) * ((i) % 16), 	(1f / 16f) * ((i) / 16),
							(1f / 16f) * ((i % 16)+1), (1f / 16f) * ((i) / 16),
							};
			fontQuads[i] = new TexturedQuad("spritefont_256.png");
			fontQuads[i].setTextureCoord(f);
		}

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
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_BLEND);
		
		
//		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, GL10Utils.floatsToFloatBuffer(new float[]{0.5f,0.5f,0.5f,1f}));
//		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, GL10Utils.floatsToFloatBuffer(new float[]{0.8f,0.7f,0.5f,1f}));
//		gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, GL10Utils.floatsToFloatBuffer(new float[]{0.1f,0.1f,10f,1f}));
//		gl.glEnable(GL10.GL_LIGHT1);
//		gl.glEnable(GL10.GL_LIGHTING);
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

	// preallocate
	int i = 0;
	int len = 0;

	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity(); // Reset model-view matrix ( NEW )
		gl.glTranslatef(0f, 0.0f, -6.0f);
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
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.setGL(gl);
		camera.setLastExplosion(world.getLastExplosionMillis());

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
		gl.glScalef(0.6f, 0.6f, 0.6f);

		if (RENDER_TILES) {
			if (world.getCubeTileMap() != null) {
				synchronized (world) {
					// lets assume, all tiles are always in one texture.
					world.getCubeTileMap().get(0).bindTexture(gl);
					len = world.getCubeTileMap().size();
					for (i = 0; i < len; i++) {
						camera.renderTile(world.getCubeTileMap().get(i));
					}
				}
			}
		}
		if (RENDER_ENTITIES) {
			// lets assume all entities are inside one texture
			if (!world.getAllEntities().isEmpty()) {
				((SimpleEntityView) world.getAllEntities().get(0)).bindTexture(gl);
				len = world.getAllEntities().size();
				for (i = 0; i < len; i++) {
					camera.renderEntityView((SimpleEntityView) world.getAllEntities().get(i), gl);
				}
			}

		}
		gl.glPopMatrix();

		if (RENDER_HUD) {
			fontQuads[0].bindTexture(gl);
			gl.glLoadIdentity();
			gl.glTranslatef(-2f, 1.0f, -4.0f);
			gl.glScalef(0.1f, 0.1f, 0.1f);
			drawHUD(gl);
		}
		

		if (FPScounter < FPS_AVERAGE_AMOUNT) {
			FPScounter++;
			avgFPS += (int) (1000 / ((System.currentTimeMillis() + 1) - lastDraw));
		} else {
			FPS = avgFPS / FPScounter;
			avgFPS = 0;
			FPScounter = 0;
		}
		
		frameCounter++;
		if (frameCounter > EVERY_N_TH_FRAME) {
			frameCounter = 0;
			everyNthFrameDo();
		}
		lastDraw = System.currentTimeMillis();

	}

	private void drawHUD(GL10 gl) {
		gl.glDisable(GL10.GL_DEPTH_TEST);
		drawHudFont(SCORE_LABEL, gl, -2f, -2f);
		if (RENDER_FPS) {
			FPSchars[0] = (char) (((FPS / 10) % 10) + 48);
			FPSchars[1] = (char) (((FPS) % 10) + 48);
			drawHudFont(FPSchars, gl, -2f, -4f);
		}
		gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	private void everyNthFrameDo() {
		world.ensureEntityAppearance();
		updateScoreLabel();
	}

	char drawChar = ' ';
	private int charNumber;

	private void drawHudFont(char[] s, GL10 gl, float positionx, float positiony) {
		gl.glPushMatrix();
		gl.glTranslatef(positionx, positiony, 0f);
		for (charNumber = 0; charNumber < s.length; charNumber++) {
			drawChar = s[charNumber];
			if (drawChar > 255)
				continue;
			fontQuads[drawChar].draw(gl);
			gl.glTranslatef(1f, 0f, 0f);

		}
		gl.glPopMatrix();
	}
	
	int scoreCount = 0;
	int scorePos = SCORE_COUNTER_OFFSET;
	private void updateScoreLabel() {
		Arrays.fill(SCORE_LABEL, SCORE_COUNTER_OFFSET, SCORE_LABEL.length, ' ');
		scoreCount = world.getPlayerFragScore();
		scorePos = Math.min(SCORE_COUNTER_OFFSET +  getNumberOfDigits(scoreCount+1)-1, SCORE_LABEL.length - 1);
		do {
//			Logger.d(this, "POSITION " + scorePos);
			SCORE_LABEL[scorePos] = (char) (scoreCount % 10 + 48);
			scoreCount /= 10; 
			scorePos--;
		} while (scoreCount != 0 && scorePos >= SCORE_COUNTER_OFFSET);
	}
	
	int count = 0;
	private int getNumberOfDigits(int n) {
		count = 0;
		while (n != 0) {
			count ++;
			n /= 10;
		}
		return count;
	}
}
