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

	
	
	//As a test to increase coverage, there may be duplicate codes.
	@Test
	public void test() {
		progress = new Progress();
		pauseTest();
		resetTest();
		setTest();
		compareTest();
	}
	
	
	
	// Purpose : To check if the compareWith function works
	public void compareTest() {
		
		// Input : Screen.level
		// Expected : Returns true when only Screen.level is compared
		progress.setScreen(Screen.level);
		assertEquals(progress.compareWith(Screen.death),false);
		assertEquals(progress.compareWith(Screen.level),true);
		
		// Input : Status.intermission
		// Expected : Returns true when either Status.running or Status.intermission is compared
		progress.setStatus(Status.intermission);
		assertEquals(progress.compareWith(Status.running),true);
		assertEquals(progress.compareWith(Status.intermission),true);
		assertEquals(progress.compareWith(Status.waveInProgress),false);
		
		// Input : Status.running
		// Expected : Returns true when only Status.running is compared
		progress.setStatus(Status.running);
		assertEquals(progress.compareWith(Status.running),true);
		assertEquals(progress.compareWith(Status.intermission),false);
		assertEquals(progress.compareWith(Status.stopped),false);
		
		// Input : Status.crashed
		// Expected : Returns true when either Status.stopped or Status.crashed is compared
		progress.setStatus(Status.crashed);
		assertEquals(progress.compareWith(Status.stopped),true);
		assertEquals(progress.compareWith(Status.crashed),true);
		assertEquals(progress.compareWith(Status.running),false);
		
		// Input : Status.stopped
		// Expected : Returns true when only Status.stopped is compared
		progress.setStatus(Status.stopped);
		assertEquals(progress.compareWith(Status.stopped),true);
		assertEquals(progress.compareWith(Status.crashed),false);
		assertEquals(progress.compareWith(Status.running),false);

	}
	
	
	// Purpose : To check if the setStatus and setScreen functions work
	public void setTest() {
		
		// Input : Status.intermission
		// Expected : status becomes Status.intermission
		progress.setStatus(Status.intermission);
		assertEquals(progress.getStatus(),Status.intermission);
		
		// Input : Screen.level
		// Expected : screen becomes Screen.level, and progress becomes paused
		progress.setScreen(Screen.level);
		assertEquals(progress.getScreen(),Screen.level);
		assertEquals(progress.isPaused(),true);
		
		// Input : Screen.store
		// Expected : screen becomes Screen.store, and progress becomes paused
		progress.resume();
		progress.setScreen(Screen.store);
		assertEquals(progress.getScreen(),Screen.store);
		assertEquals(progress.isPaused(),true);
		
		// Input : Screen.field
		// Expected : screen becomes Screen.store, and progress becomes resumed
		progress.pause();
		progress.setScreen(Screen.field);
		assertEquals(progress.getScreen(),Screen.field);
		assertEquals(progress.isPaused(),false);
		
		// Input : Screen.field
		// Expected : screen becomes Screen.store, and progress becomes resumed
		progress.resume();
		progress.setScreen(Screen.field);
		assertEquals(progress.getScreen(),Screen.field);
		assertEquals(progress.isPaused(),false);
		
		// Input : Screen.death
		// Expected : screen becomes Screen.death, and status becomes Status.running
		progress.setScreen(Screen.death);
		assertEquals(progress.getScreen(),Screen.death);
		assertEquals(progress.getStatus(),Status.running);

		// Input : Screen.start
		// Expected : screen becomes Screen.start, and status becomes Status.running
		progress.setScreen(Screen.start);
		assertEquals(progress.getScreen(),Screen.start);
		assertEquals(progress.getStatus(),Status.running);
	}
	
	
	// Purpose : To check if the pause, resume functions work
	public void pauseTest() {
		
		// Input : progress.pause();
		// Expected : progress becomes paused
		progress.pause();
		assertEquals(progress.isPaused(),true);
		
		// Input : progress.resume();
		// Expected : progress becomes resumed
		progress.resume();
		assertEquals(progress.isPaused(),false);
	}
	
	// Purpose : To check if the reset function works
	public void resetTest() {
		
		// Input : progress.reset();
		// Expected : screen becomes Screen.start, status becomes Status.running, progress becomes resumed
		progress.reset();
		assertEquals(progress.getScreen(),Screen.start);
		assertEquals(progress.getStatus(),Status.running);
		assertEquals(progress.isPaused(),false);
	}

}
