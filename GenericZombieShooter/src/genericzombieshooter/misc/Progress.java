package genericzombieshooter.misc;

public class Progress extends Subject{
	private boolean running; // Whether or not the game is currently running.
	private boolean started;
	private boolean crashed; // Tells the game whether or not there was a crash.
	private boolean paused;
	private boolean storeOpen;
	private boolean levelScreenOpen;
	private boolean deathScreen;
	private boolean waveInProgress; // Whether the player is fighting or waiting for another wave.
    
	
	
	public boolean isRunning() { return running; }
	public boolean isStarted() { return started; }
	public boolean isCrashed() { return crashed; }
	public boolean isPaused() { return paused; }
	public boolean isStoreOpen() { return storeOpen; }
	public boolean isLevelScreenOpen() { return levelScreenOpen; }
	public boolean isDeathScreen() { return deathScreen; }
	public boolean isWaveInProgress() { return waveInProgress; }
	
	
	public void setRunning(boolean running) { this.running = running; notify_(); }
	public void setStarted(boolean started) { this.started = started; notify_(); }	
	public void setCrashed(boolean crashed) { this.crashed = crashed; notify_(); }
	public void setPaused(boolean paused) { this.paused = paused; notify_(); }
	public void setStoreOpen(boolean storeOpen) { this.storeOpen = storeOpen; notify_(); }
	public void setLevelScreenOpen(boolean levelScreenOpen) { this.levelScreenOpen = levelScreenOpen; notify_(); }
	public void setDeathScreen(boolean deathScreen) { this.deathScreen = deathScreen; notify_(); }
	public void setWaveInProgress(boolean waveInProgress) { this.waveInProgress = waveInProgress; notify_(); }
	
}
