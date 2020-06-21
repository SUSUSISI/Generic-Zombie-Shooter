package genericzombieshooter.structures.weapons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.geom.Point2D;

import org.junit.Test;

import genericzombieshooter.actors.Player;
import genericzombieshooter.misc.Globals;
import kuusisto.tinysound.TinySound;

public class TeleporterTest {
	private static Teleporter teleporter;
	Player player = new Player(100, 100, 48, 48);
	Point2D.Double pos = new Point2D.Double(100, 48);
		
	/**
	*Purpose: Constructor test
	*Input: Teleporter() constructor
	*Expected: Not Null
	*/
	@Test
	public void constructorTest() {
		teleporter = new Teleporter();
		assertNotNull(teleporter);
	}
	
	/**
	*Purpose: Check weapon price of Teleporter
	*Input: getWeaponPrice()
	*Expected:
	*	Return 4000
	*/
	@Test
	public void getWeaponPriceTest() {
		int weaponPrice = teleporter.getWeaponPrice();
		assertEquals(4000, weaponPrice);
	}
	
	/**
	*Purpose: Check ammo price of Teleporter
	*Input: getAmmoPrice()
	*Expected:
	*	Return 500
	*/
	@Test
	public void getAmmoPriceTest() {
		int ammoPrice = teleporter.getAmmoPrice();
		assertEquals(500, ammoPrice);
	}
	
	/**
	*Purpose: Check ammo pack amount of Teleporter
	*Input: getAmmoPackAmount()
	*Expected:
	*	Return 1
	*/
	@Test
	public void getAmmoPackAmountTest() {
		int ammoPackAmount = teleporter.getAmmoPackAmount();
		assertEquals(1, ammoPackAmount);
	}
	
	/**
	*Purpose: Test Teleporter can fire more than ammoLeft
	*Input: 1 
	*Expected:
	*	Teleporter default ammo = 1
	*	fire 1 time, and then check Teleporter whether can fired
	*	return false
	*/
	@Test
	public void fireTest() {
		TinySound.init();
		for(int i = 0; i < (60 * 1000) / Globals.SLEEP_TIME; i++) teleporter.cool();
		assertEquals(true, teleporter.canFire());
		teleporter.fire((double)48, pos, player);
		assertEquals(false, teleporter.canFire());
	}
}
