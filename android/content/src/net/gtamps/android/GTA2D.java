package net.gtamps.android;

import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.fakerenderer.FakeGame;
import android.app.Activity;
import android.os.Bundle;

public class GTA2D extends Activity{
	
	FakeGame view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new FakeGame(this.getApplicationContext());
		setContentView(view);
		
		view.setOnTouchListener(InputEngineController.getInstance());
		view.setOnKeyListener(InputEngineController.getInstance());
		
	}
}
