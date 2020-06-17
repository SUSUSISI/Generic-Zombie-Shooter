package test;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import genericzombieshooter.misc.progress.Progress;
import genericzombieshooter.misc.progress.Screen;
import genericzombieshooter.misc.progress.Status;
import kuusisto.tinysound.TinySound;

public class ProgressTest {
	private Progress progress;
	
	@BeforeClass
	public static void before() {
		TinySound.init();
	}

	@Test
	public void test() {
		progress = new Progress();
		pauseTest();
		resetTest();
		setTest();
		compareTest();
	}
	
	public void compareTest() {
		progress.setScreen(Screen.level);
		assertEquals(progress.compareWith(Screen.death),false);
		assertEquals(progress.compareWith(Screen.level),true);
		
		progress.setStatus(Status.intermission);
		assertEquals(progress.compareWith(Status.running),true);
		assertEquals(progress.compareWith(Status.intermission),true);
		assertEquals(progress.compareWith(Status.waveInProgress),false);
		
		progress.setStatus(Status.running);
		assertEquals(progress.compareWith(Status.running),true);
		assertEquals(progress.compareWith(Status.intermission),false);
		assertEquals(progress.compareWith(Status.stopped),false);
		
		progress.setStatus(Status.crashed);
		assertEquals(progress.compareWith(Status.stopped),true);
		assertEquals(progress.compareWith(Status.crashed),true);
		assertEquals(progress.compareWith(Status.running),false);
		
		progress.setStatus(Status.stopped);
		assertEquals(progress.compareWith(Status.stopped),true);
		assertEquals(progress.compareWith(Status.crashed),false);
		assertEquals(progress.compareWith(Status.running),false);
		
		
	}
	
	
	public void setTest() {
		progress.setStatus(Status.intermission);
		assertEquals(progress.getStatus(),Status.intermission);
		
		progress.setScreen(Screen.level);
		assertEquals(progress.getScreen(),Screen.level);
		assertEquals(progress.isPaused(),true);
		
		progress.setScreen(Screen.store);
		assertEquals(progress.getScreen(),Screen.store);
		assertEquals(progress.isPaused(),true);
		
		
		progress.pause();
		progress.setScreen(Screen.field);
		assertEquals(progress.getScreen(),Screen.field);
		assertEquals(progress.isPaused(),false);
		
		progress.resume();
		progress.setScreen(Screen.field);
		assertEquals(progress.getScreen(),Screen.field);
		assertEquals(progress.isPaused(),false);
		
		progress.setScreen(Screen.death);
		assertEquals(progress.getScreen(),Screen.death);
		assertEquals(progress.getStatus(),Status.running);
		
		progress.setScreen(Screen.start);
		assertEquals(progress.getScreen(),Screen.start);
		assertEquals(progress.getStatus(),Status.running);
	}
	
	
	public void pauseTest() {
		progress.pause();
		assertEquals(progress.isPaused(),true);
		progress.resume();
		assertEquals(progress.isPaused(),false);
	}
	
	public void resetTest() {
		progress.reset();
		assertEquals(progress.getScreen(),Screen.start);
		assertEquals(progress.getStatus(),Status.running);
		assertEquals(progress.isPaused(),false);
	}

}
