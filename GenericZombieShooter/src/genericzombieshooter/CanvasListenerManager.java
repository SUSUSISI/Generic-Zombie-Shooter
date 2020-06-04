package genericzombieshooter;

import javax.swing.JPanel;

import genericzombieshooter.misc.Observer;
import genericzombieshooter.misc.Subject;

public class CanvasListenerManager implements Observer{
	private JPanel canvas;
	
	
	public CanvasListenerManager(JPanel canvas, Subject subject) { 
		this.canvas = canvas; 
		subject.attach(this);
		}
	

	@Override
	public void update() {
		
	}

	
	
}
