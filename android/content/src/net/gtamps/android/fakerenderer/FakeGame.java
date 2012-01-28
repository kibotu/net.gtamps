package net.gtamps.android.fakerenderer;

import java.io.IOException;
import java.util.Arrays;

import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.input.layout.InputLayoutIngame;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.ConnectionThread;
import net.gtamps.android.game.content.scenes.inputlistener.CameraMovementListener;
import net.gtamps.shared.Utils.Logger;
import net.gtamps.shared.game.level.Tile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class FakeGame extends View {
	private static final int FPS_AVERAGE_AMOUNT = 10;
	private static final long MAX_FPS = 60;
	private static final boolean LIMIT_FPS = false;

	private static final boolean RENDER_TILES = true;
	private static final boolean RENDER_ENTITIES = true;
	private static final boolean RENDER_HUD = true;
	private static final boolean RENDER_FPS = true;
	private static final int EVERY_N_TH_FRAME = 3;
	private static final char[] DIGITS = "0123456789 ".toCharArray();
	private static final char[] SCORE_LABEL = "SCORE         ".toCharArray();
	private static final int SCORE_COUNTER_OFFSET = 6;
	
	FakeCamera camera;
	private FakeWorld world;
	private Paint paint = new Paint();
	
	private Bitmap hudFont;
	private int hudFontCharSize = 16;

	public FakeGame(Context context) {
		super(context);
		world = new FakeWorld(context);
		camera = new FakeCamera(context, world);
		ConnectionThread connection = new ConnectionThread(world);
		new Thread(connection).start();

		InputEngineController.getInstance().setLayout(new InputLayoutIngame(world));
		CameraMovementListener pml = new CameraMovementListener(world);
		InputEngineController.getInstance().getInputEventDispatcher().addInputEventListener(pml);

		try {
			hudFont = BitmapFactory.decodeStream(context.getAssets().open("spritefont_256.png"));
			if(hudFont==null){
				Logger.e(this, "Unable to load HUD font!");
			} else {
				Logger.e(this, "Loaded HUD font successfully!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		paint.setColor(0xffffffff);
		paint.setStyle(Paint.Style.FILL);
		this.invalidate();

	}

	long lastDraw = System.currentTimeMillis();
	int avgFPS = 0;
	int FPS = 0;
	int FPScounter = 0;
	int frameCounter = 0;
	private char[] FPSchars = "   FPS".toCharArray();

	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		paint.setColor(0xff000000);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
		
		camera.setCanvas(canvas);

		if (world.getActiveView() == null) {
			for (AbstractEntityView aev : world.getAllEntities()) {
				if (aev.entity.getUid() == world.playerManager.getActivePlayer().getEntityUid()) {
					world.setActiveView(aev);
				}
			}
		} else {
			camera.follow(world.getActiveView());
		}

		if (RENDER_TILES) {
			if (world.getTileMap() != null) {
				for (Tile t : world.getTileMap()) {
					camera.renderTile(t);
				}
			}
		}
		if (RENDER_ENTITIES) {
			for (AbstractEntityView ev : world.getAllEntities()) {
				camera.renderEntityView((FakeEntityView) ev);
			}
		}
		if (RENDER_HUD) {
			drawHUD(canvas);
		}

		if (LIMIT_FPS) {
			if ((1000 / (System.currentTimeMillis() - lastDraw)) > MAX_FPS) {
				try {
					synchronized (this) {
						wait(5);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (RENDER_FPS) {
			if (FPScounter < FPS_AVERAGE_AMOUNT) {
				FPScounter++;
				avgFPS += (int) (1000 / (System.currentTimeMillis() - lastDraw));
			} else {
				FPS = avgFPS / FPScounter;
				avgFPS = 0;
				FPScounter = 0;
			}

			FPSchars[0] = (char) (((FPS/10)%10)+48);
			FPSchars[1] = (char) (((FPS)%10)+48);
			drawHudFont(FPSchars, canvas, 10, 10);
			lastDraw = System.currentTimeMillis();
		}
		frameCounter++;
		if (frameCounter > EVERY_N_TH_FRAME) {
			frameCounter = 0;
			everyNthFrameDo();
		}
		this.invalidate();
	}

	private void everyNthFrameDo() {
		world.ensureEntityAppearance();
	}

	private void drawHUD(Canvas canvas) {
		updateScoreLabel();
		drawHudFont(SCORE_LABEL, canvas, 30, 30);
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

	Rect fontRenderDest = new Rect();
	Rect fontRenderSrc = new Rect();
	int charNumber = 0;
	char drawChar = ' ';
	private void drawHudFont(char[] s, Canvas canvas, int positionx, int positiony){
		for (charNumber = 0; charNumber < s.length; charNumber++) {
			drawChar = s[charNumber];
			if(drawChar>255) continue;
			fontRenderDest.left = positionx+hudFontCharSize*charNumber;
			fontRenderDest.top = positiony;
			fontRenderDest.right = positionx+hudFontCharSize*(charNumber+1);
			fontRenderDest.bottom = positiony+hudFontCharSize;
			
			fontRenderSrc.top = (drawChar/hudFontCharSize)*hudFontCharSize;
			fontRenderSrc.left = (drawChar%hudFontCharSize)*hudFontCharSize;
			fontRenderSrc.right = fontRenderSrc.left+hudFontCharSize;			
			fontRenderSrc.bottom = fontRenderSrc.top+hudFontCharSize;
			canvas.drawBitmap(hudFont, fontRenderSrc, fontRenderDest, paint);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		camera.setResolution(w, h);
	}

}
