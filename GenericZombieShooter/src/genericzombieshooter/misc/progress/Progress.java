package genericzombieshooter.misc.progress;

import genericzombieshooter.misc.Sounds;

public class Progress extends Subject{

	
	private Status status = Status.stopped;
	private boolean paused = false;
	private Screen screen = Screen.start;
	
	public void reset() {
		setStatus(Status.running);
		setScreen(Screen.start);
		resume();
	}
	
	public void pause() {
		Sounds.PAUSE.play();
		paused = true;
	}
	public void resume() {
		Sounds.UNPAUSE.play();
		paused = false;
	}
	public boolean isPaused() {
		return paused;
	}
	
	public Screen getScreen() {
		return this.screen;
	}
	public Status getStatus() {
		return this.status;
	}
	public void setStatus(Status status) {
		this.status = status;
		notify_();
	}
	public void setScreen(Screen screen) {
		if(this.screen != screen){
			if(screen == Screen.store || screen == Screen.level)
				pause();
			else if (screen == Screen.field && isPaused())
				resume();
			else if (screen == Screen.death || screen == Screen.start)
				setStatus(Status.running);
			this.screen = screen;
		}
		notify_();
	}
	
	public boolean compareWith(Status status) {
		if ( status == Status.running ) {
			if(this.status != Status.stopped && this.status != Status.crashed)
				return true;
		} else if ( status == Status.stopped && this.status == Status.crashed)
			return true;
		
		return this.status == status;
	}
	
	public boolean compareWith(Screen screen) {
		return this.screen == screen;
	}
	

	
}

