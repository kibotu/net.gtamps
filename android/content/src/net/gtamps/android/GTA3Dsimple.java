package net.gtamps.android;


import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.simple3Drenderer.SimpleRenderer;
import net.gtamps.android.utils.AndroidLogger;
import net.gtamps.shared.Utils.Logger;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class GTA3Dsimple extends Activity{
	
	private GLSurfaceView glView; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		Logger.setLogger(AndroidLogger.INSTANCE);
		
		glView = new GLSurfaceView(this); 
		glView.setOnTouchListener(InputEngineController.getInstance());
		glView.setOnKeyListener(InputEngineController.getInstance());
		glView.setRenderer(new SimpleRenderer(this)); // Use a custom renderer
		glView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		this.setContentView(glView);
	}

	// Call back when the activity is going into the background
	@Override
	protected void onPause() {
		super.onPause();
		glView.onPause();
	}

	// Call back after onPause()
	@Override
	protected void onResume() {
		super.onResume();
		glView.onResume();
	}
}
