package net.gtamps.game;

public class TimeKeeper {
	
	private final long startTime;
	private long currentCycleTime;
	private long lastActiveDuration;
	private long lastSleepDuration;
	private long lastTotalCycleDuration;
	
	private long lastStartCycle;
	private long lastEndCycle;
	
	private boolean active;
	
	public TimeKeeper() {
		this(System.nanoTime());
	}
	
	private TimeKeeper(final long startTime) {
		this.startTime = startTime;
		currentCycleTime = startTime;
		lastStartCycle = startTime;
		lastEndCycle = startTime;
		lastActiveDuration = 0;
		lastSleepDuration = 0;
		lastTotalCycleDuration = 0;
		active = false;
	}
	
	public void startCycle() {
		currentCycleTime = System.nanoTime() / 1000000;
		lastTotalCycleDuration = currentCycleTime - lastStartCycle;
		lastSleepDuration = lastStartCycle - lastEndCycle;
		lastStartCycle = currentCycleTime;
		active = true;
	}
	
	public void endCycle() {
		lastEndCycle = System.nanoTime() / 1000000;
		lastActiveDuration = lastEndCycle - lastStartCycle;
		active = false;
	}
	
	public boolean cycleIsActive() {
		return active;
	}
	
	public long getCurrentCycleTimeMillis() {
		return currentCycleTime;
	}
	
	public long getGameDurationMillis() {
		return (currentCycleTime - startTime);
	}
	
	public long getLastCycleDurationMillis() {
		return lastTotalCycleDuration;
	}
	
	public float getLastCycleDurationSeconds() {
		return (lastTotalCycleDuration / 1000f);
	}
	
	public long getLastActiveDurationMillis() {
		return lastActiveDuration;
	}
	
	public long getLastSleepDurationMillis() {
		return lastSleepDuration;
	}
 
}
