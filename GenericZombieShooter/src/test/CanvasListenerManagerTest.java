package test;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.junit.BeforeClass;
import org.junit.Test;

import genericzombieshooter.CanvasListenerManager;
import genericzombieshooter.misc.Globals;
import genericzombieshooter.misc.Images;
import genericzombieshooter.misc.progress.*;
import genericzombieshooter.listeners.*;
import kuusisto.tinysound.TinySound;


public class CanvasListenerManagerTest {
	private CanvasListenerManager clm;
	private JPanel canvas;
	
	@BeforeClass
	public static void init() {
		TinySound.init();
	}
	
	@Test
	public void test() {
		canvas = new JPanel();
		Globals.progress.reset();
		clm = new CanvasListenerManager(canvas, Globals.progress);
		
		updateTest();
		
		
	}
	
	// Purpose : To Check Observer works well.
	public void updateTest() {
		CanvasListener levelListener = new CanvasListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
                switch(key) {
            		case KeyEvent.VK_W : Globals.keys[0] = true; break;
            		case KeyEvent.VK_A : Globals.keys[1] = true; break;
            		case KeyEvent.VK_S : Globals.keys[2] = true; break;
            		case KeyEvent.VK_D : Globals.keys[3] = true; break;
                }
			}
		};
		CanvasListener fieldListener = new CanvasListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Globals.mousePos.x = e.getX() + (Images.CROSSHAIR.getWidth() / 2);
                Globals.mousePos.y = e.getY() + (Images.CROSSHAIR.getHeight() / 2);
			}
		};
		
		// Setting listeners on Screen.level and Screen.field

		clm.setListener(Screen.level, levelListener);
		clm.setListener(Screen.field, fieldListener);
		
		// Input : set screen to be Screen.start
		// Expected : Current Listener should be null
		Globals.progress.setScreen(Screen.start);
		assertEquals(clm.getCurrentListener(),null);
		
		// Input : set screen to be Screen.level
		// Expected : Current Listener should be levelListener
		Globals.progress.setScreen(Screen.level);
		assertEquals(clm.getCurrentListener(),levelListener);
		
		// Input : set screen to be Screen.field
		// Expected : Current Listener should be fieldListener
		Globals.progress.setScreen(Screen.field);
		assertEquals(clm.getCurrentListener(),fieldListener);
		
		// Input : set status to be Status.stopped
		// Expected : Current Listener should be null
		Globals.progress.setStatus(Status.stopped);
		assertEquals(clm.getCurrentListener(),null);
		
	}
	

}
