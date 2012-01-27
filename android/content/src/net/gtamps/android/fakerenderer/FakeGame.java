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
	FakeCamera camera;
	private FakeWorld world;
	private Paint paint = new Paint();

	public FakeGame(Context context) {
		super(context);
		world = new FakeWorld(context);
		camera = new FakeCamera(context,world);
		ConnectionThread connection = new ConnectionThread(world);
		new Thread(connection).start();
		
		InputEngineController.getInstance().setLayout(new InputLayoutIngame());
		CameraMovementListener pml = new CameraMovementListener(camera);
		InputEngineController.getInstance().getInputEventDispatcher().addInputEventListener(pml);
		
		paint.setColor(0xffffffff);
		this.invalidate();

	}
	
	long lastDraw = System.currentTimeMillis();
	int avgFPS = 0;
	int FPS = 0;
	int FPScounter = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		
		if(world.getActiveView() == null){
			for(AbstractEntityView aev : world.getAllEntities()){
				if(aev.entity.getUid() == world.playerManager.getActivePlayer().getEntityUid()){
					world.setActiveView(aev);
				}
			}
		} else {
			camera.follow(world.getActiveView());	
		}
		
		camera.setCanvas(canvas);
		if(world.getTileMap()!=null){
			for (Tile t : world.getTileMap()) {
				camera.renderTile(t);
			}
		}
		for(AbstractEntityView ev : world.getAllEntities()){
			camera.renderEntityView((FakeEntityView) ev);
		}
		
		drawHUD(canvas);
		
		if(FPScounter<FPS_AVERAGE_AMOUNT){
			FPScounter++;
			avgFPS += (int) (1000/(System.currentTimeMillis()-lastDraw));
		} else {
			FPS = avgFPS/FPScounter;
			avgFPS = 0;
			FPScounter = 0;
		}
		canvas.drawText(FPS+" FPS", 10, 10, paint);
		lastDraw = System.currentTimeMillis();
		
		this.invalidate();
	}
	private void drawHUD(Canvas canvas) {
		
	}

}
