package genericzombieshooter;

import java.util.HashMap;

import javax.swing.JPanel;

import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.progress.Observer;
import genericzombieshooter.listeners.*;
import genericzombieshooter.misc.progress.Screen;
import genericzombieshooter.misc.progress.Status;
import genericzombieshooter.misc.progress.Subject;

public class CanvasListenerManager implements Observer{
	private JPanel canvas;
	private Screen currentScreen;
	private CanvasListener baseListener = null;
	private HashMap<Screen, CanvasListener> listeners = new HashMap<Screen, CanvasListener>();
	
	
	public CanvasListenerManager(JPanel canvas, Subject subject) { 
		this.canvas = canvas; 
		subject.attach(this);
	}
	
	public void setListener(Screen screen, CanvasListener canvasListener) {
		if(listeners.containsKey(screen))
			listeners.replace(screen,canvasListener);
		else
			listeners.put(screen, canvasListener);
	}
	
	private void detachListener(CanvasListener canvaslistener) {
		canvas.removeKeyListener(canvaslistener);
		canvas.removeMouseListener(canvaslistener);
		canvas.removeMouseMotionListener(canvaslistener);
		canvas.removeMouseWheelListener(canvaslistener);
	}
	
	private void attachListener(CanvasListener canvaslistener) {
		canvas.addKeyListener(canvaslistener);
		canvas.addMouseListener(canvaslistener);
		canvas.addMouseMotionListener(canvaslistener);
		canvas.addMouseWheelListener(canvaslistener);
	}
	
	public void setBaseListener(CanvasListener canvasListener) {
		if(baseListener != null)
			detachListener(baseListener);
		else {
			baseListener = canvasListener;
			attachListener(baseListener);
		}
	}

	@Override
	public void update() {
		if(Globals.progress.compareWith(Status.stopped)) {
			if(listeners.containsKey(currentScreen))
				detachListener(listeners.get(currentScreen));
			if(baseListener != null)
				detachListener(baseListener);
		} else if (currentScreen != Globals.progress.getScreen()) {
			Screen nextScreen = Globals.progress.getScreen();
			if(listeners.containsKey(currentScreen)) {
				System.out.println("detaching Listener : " + currentScreen);
				detachListener(listeners.get(currentScreen));
			}
			if(listeners.containsKey(nextScreen)) {
				System.out.println("attaching Listener : " + nextScreen);
				attachListener(listeners.get(nextScreen));
			} 
			currentScreen = nextScreen;
		}
	}
}
