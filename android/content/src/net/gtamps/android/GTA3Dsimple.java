package net.gtamps.android;



import net.gtamps.android.core.input.InputEngineController;
import net.gtamps.android.core.net.AbstractConnectionManager;
import net.gtamps.android.core.net.threaded.ConnectionThread;
import net.gtamps.android.core.net.threaded.ThreadedConnectionManager;
import net.gtamps.android.core.net.unthreaded.ConnectionCenter;
import net.gtamps.android.core.net.unthreaded.NetworkInterface;
import net.gtamps.android.core.sound.SoundEngine;
import net.gtamps.android.simple3Drenderer.GameLogicThread;
import net.gtamps.android.simple3Drenderer.SimpleRenderer;
import net.gtamps.android.simple3Drenderer.SimpleWorld;
import net.gtamps.android.utils.AndroidLogger;
import net.gtamps.shared.Utils.Logger;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class GTA3Dsimple extends Activity{
	
	private static final boolean THREADED_NETWORKING = true;;
	private GLSurfaceView glView;
	private SimpleWorld world;
	private ConnectionCenter connectionCenter; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		Logger.setLogger(AndroidLogger.INSTANCE);
		
		
		//Logic and Networking:
		if(THREADED_NETWORKING){
			AbstractConnectionManager.setImplementingClass(ThreadedConnectionManager.class);			
		} else {
			AbstractConnectionManager.setImplementingClass(NetworkInterface.class);
		}
		
		this.world = new SimpleWorld(getApplicationContext());
		
		//Graphics
		glView = new GLSurfaceView(this); 
//		glView.getHolder().setFixedSize(512, 300);
		glView.setOnTouchListener(InputEngineController.getInstance());
		glView.setOnKeyListener(InputEngineController.getInstance());
		glView.setRenderer(new SimpleRenderer(this,this.world)); // Use a custom renderer
		glView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		this.setContentView(glView);
		
		SoundEngine se = new SoundEngine(getApplicationContext());
		
		if(THREADED_NETWORKING){
			new Thread(new ConnectionThread(this.world)).start();
		} else {
			mainLoop();
		}
	}

	private void mainLoop() {
		new Thread(new GameLogicThread(world)).start();
	}

	// Call back when the activities is going into the background
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
