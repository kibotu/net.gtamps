package net.gtamps.android.simple3Drenderer;

import net.gtamps.android.core.net.unthreaded.ConnectionCenter;

public class GameLogicThread implements Runnable{

	
	private ConnectionCenter connectionCenter;

	public GameLogicThread(SimpleWorld world) {
		this.connectionCenter = new ConnectionCenter(world);
	}

	@Override
	public void run() {
		this.connectionCenter.establishConnection();
		while(true){
			this.connectionCenter.handleConnection();
			try {
				synchronized(this){
					wait(20);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
