package net.gtamps.android.fakerenderer;

import java.util.LinkedList;

import net.gtamps.android.R;
import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.input.layout.InputLayoutIngame;
import net.gtamps.android.core.net.AbstractEntityView;
import net.gtamps.android.core.net.IWorld;
import net.gtamps.android.core.net.MessageHandler;
import net.gtamps.android.game.content.scenes.inputlistener.CameraMovementListener;
import net.gtamps.android.game.content.scenes.inputlistener.PlayerMovementListener;
import net.gtamps.shared.serializer.ConnectionManager;
import net.gtamps.shared.serializer.communication.NewMessage;
import net.gtamps.shared.serializer.communication.NewSendable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class FakeGame extends View {
	FakeCamera camera;
	private LinkedList<Tile> tilesInMap = new LinkedList<Tile>();
	private IWorld world;
	private float x = 0;
	private Paint paint = new Paint();

	public FakeGame(Context context) {
		super(context);
		camera = new FakeCamera(context);
		world = new FakeWorld(context);
		ConnectionThread connection = new ConnectionThread(world);
		new Thread(connection).start();
		
		InputEngineController.getInstance().setLayout(new InputLayoutIngame());
		CameraMovementListener pml = new CameraMovementListener(camera);
		InputEngineController.getInstance().getInputEventDispatcher().addInputEventListener(pml);
		
		this.invalidate();

	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(world.getActiveView() != null){
			camera.follow(world.getActiveView());
		}
		
		camera.setCanvas(canvas);
		for (Tile t : tilesInMap) {
			camera.renderTile(t);
		}
		for(AbstractEntityView ev : world.getAllEntities()){
			camera.renderEntityView((FakeEntityView) ev);
		}
		
		
		this.invalidate();
	}

}
