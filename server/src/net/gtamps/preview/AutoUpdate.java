package net.gtamps.preview;

import net.gtamps.game.TimeKeeper;

public class AutoUpdate implements Runnable {

	private static final int DEFAULT_TARGET_FPS = 30;

	private final PreviewPanel panel;
	private final TimeKeeper gameTime = new TimeKeeper();

	private boolean run = true;
	private Thread thread = null;
	private boolean isActive = false;

	private long threadMaxSleepMillis;

	public AutoUpdate(final PreviewPanel panel) {
		this.panel = panel;
		setTargetFps(DEFAULT_TARGET_FPS);
	}
	
	public float getTargetFps() {
		return (1000f / threadMaxSleepMillis);
	}
	
	public void setTargetFps(final float targetFps) {
		if (targetFps <= 0) {
			throw new IllegalArgumentException("'targetFps must be > 0'");
		}
		threadMaxSleepMillis = (long) (1000f / targetFps);
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void hardstop() {
		run = false;
		thread.interrupt();
	}

	@Override
	public void run() {
		isActive = true;
		while (run) {
			gameTime.startCycle();
			doCycle();
			gameTime.endCycle();
			sleepIfCycleTimeRemaining();
		}
		isActive = false;
	}

	private void doCycle() {

		panel.updateContent();

		// for fps debugging
		// lastUpdate += timeElapsedInSeceonds;
		// updates++;
		// if(lastUpdate>5f){
		// 	Logger.i().log(LogType.PHYSICS,
		// 	"Physics fps: "+((updates/lastUpdate)));
		// 	lastUpdate = 0f;
		// 	updates = 0;
		// }

	}

	private void sleepIfCycleTimeRemaining() {
		final long millisRemaining = threadMaxSleepMillis - gameTime.getLastActiveDurationMillis();
		try {
			if (millisRemaining > 0) {
				Thread.sleep(millisRemaining);
			}
		} catch (final InterruptedException e) {
			// reset interrupted status?
			// Thread.currentThread().interrupt();
		}
	}

	public boolean isActive() {
		return isActive;
	}

}
