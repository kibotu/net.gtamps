package net.gtamps.android.fakerenderer;

import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.input.layout.InputLayoutIngame;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.ConnectionThread;
import net.gtamps.android.game.content.scenes.inputlistener.CameraMovementListener;
import net.gtamps.shared.game.level.Tile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class FakeGame extends View {
	private static final int FPS_AVERAGE_AMOUNT = 10;
	private static final long MAX_FPS = 60;
	private static final boolean LIMIT_FPS = false;

	private static final boolean RENDER_TILES = true;
	private static final boolean RENDER_ENTITIES = true;
	private static final boolean RENDER_HUD = true;
	private static final boolean RENDER_FPS = true;
	private static final int EVERY_N_TH_FRAME = 100;
	FakeCamera camera;
	private FakeWorld world;
	private Paint paint = new Paint();

	public FakeGame(Context context) {
		super(context);
		world = new FakeWorld(context);
		camera = new FakeCamera(context, world);
		ConnectionThread connection = new ConnectionThread(world);
		new Thread(connection).start();

		InputEngineController.getInstance().setLayout(new InputLayoutIngame());
		CameraMovementListener pml = new CameraMovementListener(camera);
		InputEngineController.getInstance().getInputEventDispatcher().addInputEventListener(pml);

		paint.setColor(0xffffffff);
		paint.setStyle(Paint.Style.FILL);
		this.invalidate();

	}

	long lastDraw = System.currentTimeMillis();
	int avgFPS = 0;
	int FPS = 0;
	int FPScounter = 0;
	int frameCounter = 0;

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

			paint.setColor(0xff000000);
			canvas.drawRect(10, 0, 40, 20, paint);
			paint.setColor(0xffffffff);
			canvas.drawText(FPS + " FPS", 10, 10, paint);
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

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		camera.setResolution(w, h);
	}

}
